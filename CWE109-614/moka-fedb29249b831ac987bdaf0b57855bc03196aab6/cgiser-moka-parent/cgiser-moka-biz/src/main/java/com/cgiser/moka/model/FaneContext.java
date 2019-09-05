package com.cgiser.moka.model;

import java.util.Random;

public class FaneContext {
	public static Integer[][] type = {{5,2000},{4,4000},{3,6000},{2,8000}};
	public static Integer[][] award = {{30,40,40,40,40,40,100},{32,47,49,55,55,55,100},
		{26,47,51,63,68,70,100},{10,40,47,67,83,90,100}};
	
//	public static Integer[] other = {};
	public static void main(String[] args) {
		while(true){
			System.out.println(new Random().nextInt(3));
		}
		
	}
}
