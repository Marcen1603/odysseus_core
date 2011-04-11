package de.uniol.inf.is.odysseus.sparql.parser.helper;

public class BlankNode implements INode{
	
	public static final BlankNode NIL = new BlankNode("NIL");

	private String name;
	
	public BlankNode (String name){
		this.name = name;
	}
	
	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public boolean isIRI() {
		return false;
	}

	@Override
	public boolean isBlankNode() {
		return true;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {		
		this.name=name;
	}

}
