package com.cgiser.moka.dao.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.StringTokenizer;

public class Passport {

	public static String keys[] = { "vkalu3 q35a*", "34nvaioshoid",
			"vj4na3hga;3p", "EG30ka3Eg3E%", ")63rh*&@vae'", "v,opw4otjg'4",
			"D1a4FVlVjYva", "JPVwwW2NUAE5", "BjpaPA5Uzdv3", "v3q8h&3an3pn",
			"fa3out4ga)32", ")7lj2q48d3@g", "WMDZ1iUW4MaQ", "Op2345jq0924",
			"oiqeuw508tjf", "*jfqihF9jQ94", "MA00843QJJAA", "09AU8SJG3I4T",
			"VAWEUJe9hfa8", "34t34gjqpi34", "087ashbaoih4", "/fa[wo4t23-+",
			"av0834uwtn%a", "v0a9w4jnt[ae", "pmaetn2UfHpo", "23foinawegHw" };
	public static String keys_upper[] = { "MPOI#fnmPI3:", "moiNVOINETQ:A",
			"&JFkjd9*#jff", "(2fa;kzne3in", "oicaj3i[nEFf", ")(U#jigfNhge",
			"09j0njEFIjn2", "oijJOIFA8#!0", "&912JFN ODIJ", "L%1n*nk*623;",
			"Noi237988&2Z", "JnfpOfpehfpw", "VMIODjh0eh8*", "(*KJF2Nnda#e",
			"onjew098fqhj", "*IJHOhf098hf", "(*&92n 2oeur", ")(*U#0fnoPIH",
			"(*Y#nfpOIHEd", "oiwon Eifa22", "982h(*3h9@sw", "_(*Yehg09whe",
			"983hfeOIH+Wf", "Joiwj&2983fs", "(*#ohsfd#F1f", "9*hoinhsdfas" };
	public static Random random = new Random();

	private static String encrypt(String str) {
		int iKey = Math.abs(random.nextInt() % (keys.length));
		char cKey = (char) (iKey + 97);
		char cKeyUpper = (char) (iKey + 65);
		byte[] ori = str.getBytes();
		byte[] key = keys[iKey].getBytes();
		for (int i = 0; i < ori.length; i++) {
			ori[i] ^= key[i % (key.length - 1)];
		}

		String sEncode = new String(MyBase64.encode(ori));

		byte[] bEncode = sEncode.getBytes();
		int equalCount = 0;
		while (true) {
			if (bEncode[bEncode.length - equalCount - 1] == '=') {
				equalCount++;
			} else {
				break;
			}
		}
		for (int i = 0; i < equalCount; i++) {
			int pos = Math.abs(random.nextInt()
					% (bEncode.length > 2 ? bEncode.length - 2 : 1));
			for (int j = 0; j < bEncode.length - pos - 1; j++) {
				bEncode[bEncode.length - j - 1] = bEncode[bEncode.length - j
						- 2];
			}
			bEncode[pos] = ',';
		}
		if (random.nextInt() % 2 == 0) {
			return cKey + new String(bEncode);
		} else {
			return cKeyUpper + new String(bEncode);
		}
	}

	private static String decrypt(String str) {
		if (str == null || str.length() < 4) {
			return "";
		}
		int equalCount = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ',') {
				equalCount++;
			}
		}
		str = str.replaceAll(",", "");
		for (int i = 0; i < equalCount; i++) {
			str += "=";
		}
		str = str.replace('*', '=');
		int iKey = 0;
		if (str.charAt(0) >= 'a') {
			iKey = str.charAt(0) - 97;
		} else {
			iKey = str.charAt(0) - 65;
		}
		byte[] ori = MyBase64.decode(str.substring(1));
		byte[] key = keys[iKey].getBytes();
		for (int i = 0; i < ori.length; i++) {
			ori[i] ^= key[i % (key.length - 1)];
		}
		return new String(ori);
	}

	public static String passport_encrypt(String password) {
		return encrypt(password);
	}

	public static String passport_decrypt(String password) {
		return decrypt(password);
	}
	
	/**
	 * ��useriden�����id
	 * @param userIden
	 * @return
	 */
	public static long idenDecryptToId(String userIden){
		long id=0;
		try{
			String userId=decrypt(userIden);
			id=Long.valueOf(userId);
		}catch(NumberFormatException e){
			id=-1;
		}catch(Exception ex){
			id=-1;
		}
		return id;
	}
	
	public static String cookie_encrypt(String password) {
		StringBuffer a= new StringBuffer("");
		try {
			byte [] b = encrypt(password).getBytes("UTF-8");
			for(int i=0;i<b.length;i++){
				a.append(String.valueOf(b[i]));
				a.append('-');
			}
//			a.append(String.valueOf(b));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a.toString();
	}

	public static String cookie_decrypt(String password) {
		StringBuffer sb = new StringBuffer("");
		if(password.charAt(0)<=57){
			StringTokenizer st = new StringTokenizer(password,"-");
			while(st.hasMoreTokens()){
				sb.append(String.valueOf((char)(Integer.parseInt(st.nextToken()))));
				//String.
			}
			//password.
			return decrypt(sb.toString());
		}else{
			return decrypt(password);
		}
		
	}

	public static void main(String[] args) {
		String a = passport_encrypt("100000000001");
		System.out.println(a);
		String b= "sZ3FndWV6VQlYVlFh";
		
		System.out.println(passport_decrypt(b));
		//System.out.println(a);
		/*System.out.println(cookie_decrypt("qfHEAAAgEA2F6enB9"));
		System.out.println(cookie_decrypt(cookie_encrypt("3000000000024605")));
		for(int i=0;i<=9999;i++){
			if(!"100000000010".equals(passport_decrypt("qfHEAAAgEA2F6enB9"))){
				System.out.println("======error==="+i+"=====");
			}
		}
		//System.out.println(passport_decrypt(a));
		for (int i = 0; i <= 4999; i++) {
			passport_decrypt(passport_encrypt(a));
			if (i % 4999 == 0) {
				System.out.println(i + " times.");
			}
		}
		*/
	}
}
