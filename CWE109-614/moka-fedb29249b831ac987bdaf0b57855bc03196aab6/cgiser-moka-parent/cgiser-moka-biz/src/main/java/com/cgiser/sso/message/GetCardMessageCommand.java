package com.cgiser.sso.message;

import org.xsocket.connection.INonBlockingConnection;

public class GetCardMessageCommand extends Command implements ICommand {

	@Override
	public void execute(INonBlockingConnection nbc, int cmd, Object buffer) {
		// TODO Auto-generated method stub
		 if(buffer instanceof Packet){
	            Packet packet = (Packet)buffer;
	            getSocketHandler().sendMessageToAll(packet);
	            packet.clear();
	        }
	}

}
