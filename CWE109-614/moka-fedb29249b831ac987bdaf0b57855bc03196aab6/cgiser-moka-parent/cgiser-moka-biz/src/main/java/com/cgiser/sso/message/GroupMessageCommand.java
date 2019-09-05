package com.cgiser.sso.message;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.xsocket.connection.INonBlockingConnection;

import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;

public class GroupMessageCommand extends Command implements ICommand {
	//命令信息号为1003
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
            getSocketHandler().bindUserAndNbc(DigestUtils.digest(user), nbc.getId());
            Role role = super.getRoleManager().getRoleByName(user);
            Legioner legioner = super.getLegionManager().getLegioner(role.getRoleId());
            if(legioner==null){
            	packet.clear();
            	return;
            }
            List<Legioner> legioners = super.getLegionManager().getLegioner(legioner.getLegionId(), 1, 80);
            Packet senddata = new Packet(200);
            senddata.writeInt(1003);
            senddata.writeString(user, "UTF-8");
            senddata.writeString(content, "UTF-8");
            Role toRole;
            for(int i=0;i<legioners.size();i++){
            	toRole = super.getRoleManager().getRoleById(legioners.get(i).getRoleId());
            	getSocketHandler().sendMessageToUser(toRole.getRoleName(), senddata);
            }
            
            senddata.clear();
            packet.clear();
        }
    }
}
