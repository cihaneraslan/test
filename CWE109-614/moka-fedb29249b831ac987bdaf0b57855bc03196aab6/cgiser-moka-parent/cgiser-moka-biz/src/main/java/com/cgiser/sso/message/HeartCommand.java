package com.cgiser.sso.message;

import org.xsocket.connection.INonBlockingConnection;

public class HeartCommand extends Command implements ICommand {
	@Override
    public void execute(INonBlockingConnection nbc, int cmd, Object buffer){
        if(buffer instanceof Packet){
        	Packet packet = (Packet)buffer;
			Short a = packet.readShort();
			Packet senddata = new Packet(200);
			senddata.writeInt(1020);
			senddata.writeString("heartbeat", "UTF-8");
			getSocketHandler().sendMessageToNbc(nbc.getId(), senddata);
			senddata.clear();
			packet.clear();
            

        }
    }
}
