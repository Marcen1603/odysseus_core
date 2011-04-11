package de.uniol.inf.is.odysseus.sparql.parser.helper;

public class Literal implements INode{

	private String name;
	
	private String language;
	private String datatype;
	
	public Literal(String name){
		this.name = name;
	}
	
	public Literal(){
	}
	
	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isLiteral() {
		return true;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Override
	public boolean isIRI() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBlankNode() {
		// TODO Auto-generated method stub
		return false;
	}


}
