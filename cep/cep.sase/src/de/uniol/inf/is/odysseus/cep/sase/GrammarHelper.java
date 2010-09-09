package de.uniol.inf.is.odysseus.cep.sase;

import java.util.Scanner;

public class GrammarHelper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		while (true){
			Scanner in = new Scanner(System.in);
			System.out.print(">");
			String toConvert  = in.nextLine();
			StringBuffer out = new StringBuffer();
			
			for (int i=0;i<toConvert.length();i++){
				String c = ""+toConvert.charAt(i);
				if (c.toLowerCase() != c.toUpperCase()) {
					out.append("('").append(c.toUpperCase()).append("'|'").append(c.toLowerCase()).append("')");
				}else{
					out.append("('").append(c).append("')");
				}
			}
			System.out.println(out.toString());
		}
	}

}
