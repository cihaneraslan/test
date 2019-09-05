package com.cgiser.moka.message.netty;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.cgiser.moka.manager.MessageManager;

public class MessageHandler implements IHandler {
	private MessageManager messageManager;

	@Override
	public void excute(Channel channel, ChannelBuffer buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public MessageManager getMessageManager() {
		// TODO Auto-generated method stub
		return messageManager;
	}

	@Override
	public void setMessageManager(MessageManager messageManager) {
		// TODO Auto-generated method stub
		this.messageManager = messageManager;
	}

	@Override
	public String readString(ChannelBuffer buffer) {
		short len = buffer.readShort();
		return new String(buffer.readBytes(len).toString());
	}

	@Override
	public void writeString(ChannelBuffer buffer, String str) {
		byte[] str_bytes = str.getBytes();
		short len = (short) (str_bytes.length);
		buffer.writeShort(len);
		buffer.writeBytes(str_bytes);
	}

	@Override
	public String readString(ChannelBuffer buffer, String charset) {
		short len = buffer.readShort();
		return buffer.readBytes(len).toString(Charset.forName("utf-8"));
	}

	@Override
	public void writeString(ChannelBuffer buffer, String str, String charset) {
		try {
			byte[] str_bytes = str.getBytes(charset);
			short len = (short) (str_bytes.length);
			buffer.writeShort(len);
			buffer.writeBytes(str_bytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

}
