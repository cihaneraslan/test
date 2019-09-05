package com.cgiser.sso.message;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.IConnection.FlushMode;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.moka.dao.util.DigestUtils;

public class XSocketDataHandler implements IDataHandler, IConnectHandler, IDisconnectHandler {
	Logger logger = LoggerFactory.getLogger(XSocketDataHandler.class);
	private Map<String,INonBlockingConnection>  newsessions = Collections.synchronizedMap(new HashMap<String, INonBlockingConnection>());
//    private Set<INonBlockingConnection> sessions = Collections.synchronizedSet(new HashSet<INonBlockingConnection>());
    private HashMap<Integer, ICommand> map = new HashMap<Integer, ICommand>();
    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    private MemCachedManager memCachedManager;
    public boolean onData(INonBlockingConnection nbc) throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
        try {
            int abailable = nbc.available();
            if (abailable > 0) {
                if (nbc.indexOf("<policy-file-request/>", "UTF-8") != -1) {
                    nbc.read(buffer);
                    buffer.clear();
                    String xml = "<cross-domain-policy>";
                    xml = xml + "<allow-access-from domain=\"*\" to-ports=\"5000,8080,8088,8090\" />";
                    xml = xml + "</cross-domain-policy>";
                    nbc.write(xml + "\0");
                    return true;
                }
                nbc.read(buffer);
                Packet packet = new Packet(buffer);
                packet.flip();
//                nbc.setEncoding("UTF-8");  
                //设置是否自动清空缓存  
                nbc.setAutoflush(true); 
                nbc.setFlushmode(FlushMode.ASYNC);
                int dataLength = packet.readInt();//dataLength
                int cmd = packet.readShort();
                //String userName = packet.readString();
                //userManager.getUserbyUserName(userName);
                ICommand command = (ICommand)map.get(cmd);
                if(command != null){
                    command.execute(nbc, cmd, packet);
                }else{
                	packet.clear();
                	logger.error("消息号为" + cmd + "的Command不存在");
                }
            }
        } catch (Exception ex) {
        	logger.error("ReceiveDataException: " + ex.getMessage());
        }
        return true;
    }
    
    public void bindUserAndNbc(String userIden,String id){
    	try {
//            synchronized (newsessions) {
//                //sessions.add(nbc);
//            	if(newsessions.containsKey(id)){
//
//            	}
//            }
//        	//memCachedManager.put(nbc.getId(), nbc);
    		Object nbcId = memCachedManager.get(userIden);
    		if(nbcId!=null){
    			if(nbcId.equals(id)){
    				return;
    			}
        		if(newsessions.containsKey(nbcId)){
        	        try {
            			INonBlockingConnection nbc = newsessions.get(nbcId);
            			Packet senddata = new Packet(200);
        				senddata.writeInt(1005);
        				senddata.writeInt(404);
        	            this.send(nbc, senddata);
        	            synchronized (newsessions) {
        	                nbc.close();
        	                newsessions.remove(nbc.getId());
        	            }
        	            logger.debug("onDisconnect");
        	        } catch (Exception ex) {
        	        	logger.error("onDisconnect" + ex.getMessage());
        	        }
        		}
    		}
    		if(nbcId!=null){
    			memCachedManager.delete(userIden);
    		}
    		memCachedManager.set(userIden, 0, id);
        } catch (Exception ex) {
            System.out.println("onConnect: " + ex.getMessage());
        }
    	
    }
    public void registerCommand(int cmd, Class CommandClass) throws Exception{
        ICommand command = (ICommand)CommandClass.newInstance();
        command.setSocketHandler(this);
        map.put(cmd, command);
    }

    public void unregisterCommand(int cmd) {
        map.remove(cmd);
    }
    public void excudeCammand(int cmd,Object buffer){
    	 ICommand command = (ICommand)map.get(cmd);
         if(command != null){
             command.execute(null, cmd, buffer);
         }else{
         	logger.error("消息号为" + cmd + "的Command不存在");
         }
    }
    public boolean onConnect(INonBlockingConnection nbc) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
        try {
            synchronized (newsessions) {
                //sessions.add(nbc);
                newsessions.put(nbc.getId(), nbc);
            }
            Packet senddata = new Packet(200);
			senddata.writeInt(1020);
			senddata.writeString("heartbeat", "UTF-8");
            this.send(nbc, senddata);
        	//memCachedManager.put(nbc.getId(), nbc);
            logger.debug("用户登录 " +  nbc.getRemoteAddress().getHostAddress()+nbc.getRemoteAddress().getHostName() + " �Ѿ�����...");
        } catch (Exception ex) {
        	logger.debug("onConnect: " + ex.getMessage());
        }
        return true;
    }

    public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
        try {
            synchronized (newsessions) {
                nbc.close();
                newsessions.remove(nbc.getId());
                //memCachedManager.remove(nbc.getId());
            }
            logger.info("onDisconnect");
        } catch (Exception ex) {
        	logger.info("onDisconnect" + ex.getMessage());
        }
        return true;
    }
    public void sendMessageToUser(String roleName,Object data){
    	try{
	    	Object nbcIdObj = memCachedManager.get(DigestUtils.digest(roleName));
	    	if(nbcIdObj==null){
	    		logger.error("sendMessageToUser: " +roleName);
	    	}else{
	    		if(newsessions.containsKey(nbcIdObj.toString())){
	    			INonBlockingConnection nbConn = (INonBlockingConnection)newsessions.get(nbcIdObj);
	                if (nbConn.isOpen()) {
	                    if(data instanceof Packet){
	                        send(nbConn, (Packet)data);
	                    }else{
	                        send(nbConn, (ByteBuffer)data);
	                    }
	                }
	    		}
	    	}
    	}catch (Exception e) {
    		logger.error("sendMessageToUser: " +roleName+ e.getMessage());
		}
    }
    public void sendMessageToNbc(String nbcIdObj,Object data){
    	try{
	    	if(newsessions.containsKey(nbcIdObj)){
				INonBlockingConnection nbConn = (INonBlockingConnection)newsessions.get(nbcIdObj);
	            if (nbConn.isOpen()) {
	                if(data instanceof Packet){
	                    send(nbConn, (Packet)data);
	                }else{
	                    send(nbConn, (ByteBuffer)data);
	                }
	            }
			}
    	}catch (Exception e) {
			// TODO: handle exception
		}
    }
    public void sendMessageToAll(Object data) {
    	
        try {
            synchronized (newsessions) {
            	Set<String> keys = newsessions.keySet();
                //Iterator<INonBlockingConnection> iter = newsessions.keySet();
            	Collection<INonBlockingConnection> c = newsessions.values();
            	logger.debug("用户在线"+String.valueOf(c.size()));
            	Iterator it = c.iterator();
                while (it.hasNext()) {
                    INonBlockingConnection nbConn = (INonBlockingConnection) it.next();
                    if (nbConn.isOpen()) {
                        if(data instanceof Packet){
                            send(nbConn, (Packet)data);
                        }else{
                            send(nbConn, (ByteBuffer)data);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        	logger.error("sendMessageToAll: " + ex.getMessage());
        }
    }

    public int send(INonBlockingConnection nbc, Packet packet) throws IOException {
        return send(nbc, packet.byteBuffer());
    }

    public int send(INonBlockingConnection nbc, ByteBuffer buffer) throws IOException {
        int dataLen = buffer.limit() - buffer.remaining();

        if (buffer.position() > 0) {
            buffer.flip();
        }

        ByteBuffer bts = ByteBuffer.allocate(dataLen + 4);
        bts.putInt(dataLen);
        bts.put(buffer);

        if (bts.position() > 0) {
            bts.flip();
        }
        int l = nbc.write(bts);
        nbc.flush();

        bts.clear();
//        buffer.clear();
        return l;
    }

	public MemCachedManager getMemCachedManager() {
		return memCachedManager;
	}

	public void setMemCachedManager(MemCachedManager memCachedManager) {
		this.memCachedManager = memCachedManager;
	}
}
