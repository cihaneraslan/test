package com.cgiser.sso.message;

import org.apache.commons.lang.StringUtils;
import org.xsocket.connection.INonBlockingConnection;

import com.cgiser.moka.dao.util.DigestUtils;

public class SingleMessageCommand extends Command implements ICommand {
	//命令信息号为1002
	@Override
    public void execute(INonBlockingConnection nbc, int cmd, Object buffer){
        if(buffer instanceof Packet){
            Packet packet = (Packet)buffer;
            String roleName1 = packet.readString("UTF-8");
            String roleName2 = packet.readString("UTF-8");
            if(StringUtils.isEmpty(roleName1)||StringUtils.isEmpty(roleName2)){
            	packet.clear();
            	return;
            }
            String content = packet.readString("UTF-8");
            Packet senddata = new Packet(200);
            Packet data = new Packet(200);
            senddata.writeInt(1002);
            senddata.writeString(roleName1, "UTF-8");
            senddata.writeString(roleName2, "UTF-8");
            senddata.writeString(content, "UTF-8");
            data.writeInt(1002);
            data.writeString(roleName1, "UTF-8");
            data.writeString(roleName2, "UTF-8");
            data.writeString(content, "UTF-8");
//          getSocketHandler().sendMessageToAll(senddata);
            try {
            	getSocketHandler().bindUserAndNbc(DigestUtils.digest(roleName1), nbc.getId());
//            	nbc.getRemoteAddress().ge;
            	//通过getSocketHandler得到，所有的链接客户端，然后根据ip，或者名字找到客户？
				getSocketHandler().sendMessageToUser(roleName2, senddata);
				getSocketHandler().sendMessageToUser(roleName1, data);
			} catch (Exception e) {
				data.clear();
	            senddata.clear();
	            packet.clear();
				e.printStackTrace();
			}
			data.clear();
            senddata.clear();
            packet.clear();
        }
    }
}
