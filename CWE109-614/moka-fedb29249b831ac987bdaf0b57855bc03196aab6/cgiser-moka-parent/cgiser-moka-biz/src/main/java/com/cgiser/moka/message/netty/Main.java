package com.cgiser.moka.message.netty;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext(
				"classpath:/bean/moka-applicationContext.xml");
		IServer server = (IServer) context.getBean("serverNettyImpl");
		server.start();
	}
}
