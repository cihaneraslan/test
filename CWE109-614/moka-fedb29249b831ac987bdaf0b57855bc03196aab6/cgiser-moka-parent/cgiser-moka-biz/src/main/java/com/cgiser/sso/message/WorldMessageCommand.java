package com.cgiser.sso.message;

import org.apache.commons.lang.StringUtils;
import org.xsocket.connection.INonBlockingConnection;

import com.cgiser.moka.dao.util.DigestUtils;

public class WorldMessageCommand extends Command implements ICommand {
	//命令信息号为1001
	@Override
    public void execute(INonBlockingConnection nbc, int cmd, Object buffer){
        if(buffer instanceof Packet){
            Packet packet = (Packet)buffer;
            String user = packet.readString("UTF-8");
            String content = packet.readString("UTF-8");
            if(StringUtils.isEmpty(user)||StringUtils.isEmpty(content)){
            	packet.clear();
            	return;
            }
            Packet senddata = new Packet(200);
            senddata.writeInt(1001);
            senddata.writeString(user, "UTF-8");//用户名
            senddata.writeString(content, "UTF-8");//内容
            getSocketHandler().bindUserAndNbc(DigestUtils.digest(user), nbc.getId());
            getSocketHandler().sendMessageToAll(senddata);
            senddata.clear();
            packet.clear();
        }
    }
}
