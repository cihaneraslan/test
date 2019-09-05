/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgiser.sso.message;

import java.nio.ByteBuffer;

public class NotificationDispatcher {

    private Packet packet;

    public NotificationDispatcher() {
    }

    public void analyse(ByteBuffer buffer) {
        packet = Packet.wrap(buffer);
        packet.flip();
        int len = packet.readInt();
        int cmd = packet.readShort();
        dispatch(cmd);
    }

    public void dispatch(int cmd) {
        
    }
}