package com.cgiser.moka.message.netty;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.LocationManager;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.RoleLocation;

public class CgiserChannelGroup  {
	private final ConcurrentMap<String, Integer> roleChannel = new ConcurrentHashMap<String, Integer>();
	private final ConcurrentMap<Integer, String> channelRole = new ConcurrentHashMap<Integer, String>();
	private final DefaultChannelGroup allChannels = new DefaultChannelGroup("allUser");
	private final ConcurrentMap<String, Set<String>> cityRole = new ConcurrentHashMap<String, Set<String>>();
	
    public boolean addChannel(Role role,Channel channel) {
    	LocationManager locationManager = (LocationManager)HttpSpringUtils.getBean("locationManager");
    	RoleLocation roleLocation = locationManager.getRoleLocation(role.getRoleId());
    	String cityName = "火星";
    	if(roleLocation!=null){
    		cityName = roleLocation.getCity();
    	}
    	Set<String> city = cityRole.get(DigestUtils.digest(cityName));
		if(city==null){
			city = new HashSet<String>();
			cityRole.put(DigestUtils.digest(role.getCity()), city);
		}
		city.add(role.getRoleName());
    	if(allChannels.add(channel)){
    		roleChannel.put(role.getRoleName(), channel.getId());
    		channelRole.put(channel.getId(),role.getRoleName());
    		return true;
    	}else{
    		return false ;
    	}
    }
    public ConcurrentMap<String, Integer> getroleChannel(){
    	return roleChannel;
    }
    public ConcurrentMap<Integer, String> getchannelRole(){
    	return channelRole;
    }
    public boolean add(Channel channel) {
    	return allChannels.add(channel);
    }
    public void bindChannel(Role role,Channel channel){
    	Integer channelId = roleChannel.get(role.getRoleName());
    	if(channelId!=null){
    		if(!channel.getId().equals(channelId)){
    			Channel cha = allChannels.find(channelId);
    			if(cha!=null){
        			ChannelBuffer buffer = new DynamicChannelBuffer(200);
					buffer.writeInt(MessageType.SYSTEM.getCode());
					buffer.writeInt(404);
					byte[] recByte=buffer.copy().toByteBuffer().array();
					ChannelBuffer responseBuffer = new DynamicChannelBuffer(4+recByte.length);
					responseBuffer.writeInt(recByte.length);
					responseBuffer.writeBytes(recByte);
					cha.write(responseBuffer);
    			}

    		}
    	}
    	roleChannel.put(role.getRoleName(), channel.getId());
		channelRole.put(channel.getId(), role.getRoleName());
		Set<String> city = cityRole.get(DigestUtils.digest(role.getCity()));
		if(city==null){
			city = new HashSet<String>();
			cityRole.put(DigestUtils.digest(role.getCity()), city);
		}
		city.add(role.getRoleName());
    }
    public Channel getChannelByRole(Role role){
    	if(this.roleChannel.containsKey(role.getRoleName())){
        	return allChannels.find(roleChannel.get(role.getRoleName()));
    	}else{
    		return null;
    	}
    }
    public Set<String> getRoleNamesByCity(String city){
    	return cityRole.get(DigestUtils.digest(city));
    }
    public String getRoleNameByChannelId(Integer id){
    	return channelRole.get(id);
    }
    public Channel getChannelByRoleName(String roleName){
    	Integer chanelId = roleChannel.get(roleName);
    	if(chanelId!=null){
    		return allChannels.find(chanelId);
    	}else{
    		roleChannel.remove(roleName);
    	}
    	return null;
    }
    public static boolean isOnLine(String roleName){
    	Channel channel = ReceiverHandler.allChannels.getChannelByRoleName(roleName);
    	if(channel!=null){
    		return channel.isOpen()&&channel.isConnected();
    	}else{
    		return false;
    	}
    }
    /**
     * city角色原來所在的city
     * @param city
     * @param role
     */
    public void updateRoleCity(String city,Role role){
    	Set<String> group = cityRole.get(DigestUtils.digest(city));
    	Set<String> groupNow = cityRole.get(DigestUtils.digest(role.getCity()));
    	if(groupNow==null){
    		groupNow = new HashSet<String>();
			cityRole.put(DigestUtils.digest(role.getCity()), groupNow);
		}
    	groupNow.add(role.getRoleName());
    	if(group==null){
    		group = new HashSet<String>();
			cityRole.put(DigestUtils.digest(role.getCity()), group);
    	}else{
    		group.remove(role.getRoleName());
    	}
    	
    }
    public void removeChannel(Channel channel){
//    	String roleName = this.channelRole.get(channel.getId());
//    	RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
//    	Role role = roleManager.getRoleByName(roleName);
//    	Set<String> group = cityRole.get(DigestUtils.digest(role.getCity()));
//    	if(group!=null){
//    		group.remove(roleName);
//    	}
    	allChannels.remove(channel);
//    	this.roleChannel.remove(this.channelRole.get(channel.getId()));
//    	this.channelRole.remove(channel.getId());
    }
    public void write(Object message){
    	allChannels.write(message);
    }

}
