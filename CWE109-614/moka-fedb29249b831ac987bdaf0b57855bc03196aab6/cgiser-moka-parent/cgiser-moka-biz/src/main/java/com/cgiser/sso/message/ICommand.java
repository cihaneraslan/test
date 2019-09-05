/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cgiser.sso.message;

import org.xsocket.connection.INonBlockingConnection;

/**
 *
 * @author zkpursuit
 */
public interface ICommand {
    void execute(INonBlockingConnection nbc, int cmd, Object buffer);
    void setSocketHandler(XSocketDataHandler handler);
    XSocketDataHandler getSocketHandler();
}
