package com.cgiser.moka;

import com.cgiser.moka.dao.util.DigestUtils;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	System.out.println(DigestUtils.digest(new String("111".getBytes())).toUpperCase());
    }
}
