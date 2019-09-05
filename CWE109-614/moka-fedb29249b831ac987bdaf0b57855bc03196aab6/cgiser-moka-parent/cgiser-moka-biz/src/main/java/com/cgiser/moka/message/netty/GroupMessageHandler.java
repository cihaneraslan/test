package com.cgiser.moka.message.netty;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;

public class GroupMessageHandler extends MessageHandler implements IHandler {
	@Override
	public void excute(Channel channel, ChannelBuffer buffer) {
		String user = this.readString(buffer, "UTF-8");
		String content = this.readString(buffer, "UTF-8");
		if (StringUtils.isEmpty(user) || StringUtils.isEmpty(content)) {
			return;
		}
		MemCachedManager roleCachedManager = (MemCachedManager) HttpSpringUtils
				.getBean("roleCachedManager");
		Role role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(user));
		if (role == null) {
			return;
		}
		LegionManager legionManager = (LegionManager) HttpSpringUtils
				.getBean("legionManager");
		RoleManager roleManager = (RoleManager) HttpSpringUtils
				.getBean("roleManager");
		Legioner legioner = legionManager.getLegioner(role.getRoleId());
		if (legioner == null) {
			return;
		}
		ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
		responseBuffer.writeInt(1003);
		this.writeString(responseBuffer, user, "UTF-8");
		this.writeString(responseBuffer, content, "UTF-8");
		List<Legioner> legioners = legionManager.getLegioner(legioner
				.getLegionId(), 1, 80);
		Role toRole;
		for (int i = 0; i < legioners.size(); i++) {
			toRole = roleManager.getRoleById(legioners.get(i).getRoleId());
			this.getMessageManager().sendMessageToRole(toRole.getRoleName(),
					responseBuffer.copy());
		}
	}
}
