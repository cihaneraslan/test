package com.cgiser.moka.message.netty;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerNettyImpl implements IServer {

	private ServerChannelPipelineFactory pipelineFactory;
	private Channel channel;
	private static final Logger logger = LoggerFactory
			.getLogger(ServerNettyImpl.class.getName());

	@Override
	public void start() {
//		DatagramChannelFactory udpChannelFactory = new NioDatagramChannelFactory(
//				Executors.newCachedThreadPool());
		ChannelFactory factory =new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool());
		ConnectionlessBootstrap bootstrap = new ConnectionlessBootstrap(
				factory);
		bootstrap.setOption("reuseAddress", false);
		bootstrap.setOption("child.reuseAddress", false);
		bootstrap.setOption("readBufferSize", 1024);
		bootstrap.setOption("writeBufferSize", 1024);
		bootstrap.setPipelineFactory(this.pipelineFactory);
		SocketAddress serverAddress = new InetSocketAddress(5000);
		this.channel = bootstrap.bind(serverAddress);
		logger.info("server start on " + serverAddress);
	}

	@Override
	public void restart() {
		this.stop();
		this.start();
	}

	@Override
	public void stop() {
		if (this.channel != null) {
			this.channel.close().addListener(ChannelFutureListener.CLOSE);
		}
	}

	public ServerChannelPipelineFactory getPipelineFactory() {
		return pipelineFactory;
	}

	public void setPipelineFactory(ServerChannelPipelineFactory pipelineFactory) {
		this.pipelineFactory = pipelineFactory;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
