package de.uniol.inf.is.odysseus.query.transformation.java.mainstructure;

public class JavaMainstructure{

	private final String programmLanguage = "Java";
	
	
	public String getClassTop() {
		StringBuilder classTop = new StringBuilder();
		classTop.append("class HelloWorldApp {\n");
		classTop.append("public static void main(String[] args) {\n");
		return classTop.toString();
	}


	public String getClassBottom() {
		StringBuilder classBottom = new StringBuilder();
		classBottom.append("}\n");
		classBottom.append("}\n");
		return classBottom.toString();
	}
	
}
