package com.cgiser.moka.message.netty;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class ServerChannelPipelineFactory implements ChannelPipelineFactory {

    private ReceiverHandler receiverHandler;  
    @Override  
    public ChannelPipeline getPipeline() throws Exception   
    {  
        ChannelPipeline pipeline=Channels.pipeline();  
        pipeline.addLast("handler", this.receiverHandler);  
        return pipeline;  
    }
	public ReceiverHandler getReceiverHandler() {
		return receiverHandler;
	}
	public void setReceiverHandler(ReceiverHandler receiverHandler) {
		this.receiverHandler = receiverHandler;
	}  

}
