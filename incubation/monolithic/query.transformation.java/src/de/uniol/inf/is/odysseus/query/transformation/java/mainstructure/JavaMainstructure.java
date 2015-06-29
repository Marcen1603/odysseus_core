package de.uniol.inf.is.odysseus.query.transformation.java.mainstructure;

public class JavaMainstructure{

	public String getClassTop() {
		StringBuilder classTop = new StringBuilder();
		classTop.append("\n");
		classTop.append("class Main {\n");
		classTop.append("public static void main(String[] args) throws IOException {\n");
		return classTop.toString();
	}


	public String getClassBottom() {
		StringBuilder classBottom = new StringBuilder();
		classBottom.append("}\n");
		classBottom.append("}\n");
		return classBottom.toString();
	}
	
	public String getPackage(){
		StringBuilder code = new StringBuilder();
		code.append("package main;");
		code.append("\n");
		code.append("\n");
		return code.toString();
	}
	
}
