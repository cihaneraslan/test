package com.cgiser.moka.message.netty;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;

public class MessageUtil {

	public static String readString(ChannelBuffer buffer) {
		short len = buffer.readShort();
		return new String(buffer.readBytes(len).toString());
	}


	public static void writeString(ChannelBuffer buffer, String str) {
		byte[] str_bytes = str.getBytes();
		short len = (short) (str_bytes.length);
		buffer.writeShort(len);
		buffer.writeBytes(str_bytes);
	}


	public static String readString(ChannelBuffer buffer, String charset) {
		short len = buffer.readShort();
		return buffer.readBytes(len).toString(Charset.forName("utf-8"));
	}


	public static void writeString(ChannelBuffer buffer, String str, String charset) {
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
