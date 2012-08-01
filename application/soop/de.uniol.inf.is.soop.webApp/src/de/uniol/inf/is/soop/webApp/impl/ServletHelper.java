package de.uniol.inf.is.soop.webApp.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ServletHelper {
	
	String template = null;
	
	public ServletHelper(){
		
		template = readTemplate("page");
		
	}

	public String render(String content, String title, String error) {

		 String output = template.replace("##content##", content);
		 output = output.replace("##title##", (title != null) ? title: "SOOP");
		 
		if (error != null) output = setError(output, error);
		
		return output;

	}

	public String getTokenFromCookie(HttpServletRequest req) {

		String token = null;

		Cookie[] cookies = req.getCookies();

		for (Cookie c : cookies) {
			if (c.getName().equals("token")) {
				token = c.getValue();
				break;
			}
		}

		return token;
	}
	
	public String readTemplate(String tmpl) {
		StringBuilder text = new StringBuilder();
		String NL = System.getProperty("line.separator");
		Scanner scanner;

		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("/resources/templates/"+tmpl+".html");
		
		if (is instanceof InputStream){
			scanner = new Scanner(is);

			try {
				while (scanner.hasNextLine()) {
					text.append(scanner.nextLine() + NL);
				}
			} finally {
				scanner.close();
			}
		} else {
			System.out.println("Template not found: " + tmpl);
		}
			
		

		return text.toString();
	}
	
	public String setError(String data, String error){
		if (error != null) 
			return data.replace(
					"<div id=\"pagecontent\">", 
					"<div id=\"errornotice\">" 
					+ error + 
					"</div><div id=\"pagecontent\">");
		else return data;
	}
	
}
