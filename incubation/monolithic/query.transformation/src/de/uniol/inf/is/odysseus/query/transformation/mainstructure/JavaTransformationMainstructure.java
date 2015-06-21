package de.uniol.inf.is.odysseus.query.transformation.mainstructure;

public class JavaTransformationMainstructure extends AbstractTransformationMainstructure{

	private final String programmLanguage = "Java";
	
	
	@Override
	public String getProgramLanguage() {
		return programmLanguage;
	}


	@Override
	public String getImports() {

		return "";
	}

	@Override
	public String getClassTop() {
		StringBuilder classTop = new StringBuilder();
		classTop.append("class HelloWorldApp {\n");
		classTop.append("public static void main(String[] args) {\n");
		return classTop.toString();
	}

	@Override
	public String getClassBottom() {
		StringBuilder classBottom = new StringBuilder();
		classBottom.append("}\n");
		classBottom.append("}\n");
		return classBottom.toString();
	}
	
}
