package com.cgiser.sso.message;


import java.lang.reflect.Constructor;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zkpursuit
 */
public class Tool {
    public static Object createClassByName(String name, Object args[]) throws Exception {
        Class ObjectClass = Class.forName(name);
        return createClass(ObjectClass, args);
    }
    public static Object createClass(Class ObjectClass, Object args[]) throws Exception//自动找到合适的构造方法并构造
    {
        Class[] argsClass = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argsClass[i] = args[i].getClass();
        }
        try {
            Constructor cons = ObjectClass.getConstructor(argsClass);
            return cons.newInstance(args);
        } catch (Exception e) {
            return ObjectClass.newInstance();
        }
    }
}