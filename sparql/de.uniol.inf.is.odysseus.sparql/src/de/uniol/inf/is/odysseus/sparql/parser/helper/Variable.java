package de.uniol.inf.is.odysseus.sparql.parser.helper;

public class Variable implements INode{

	private String name;
	
	public Variable(String name){
		this.name = name;
	}
	
	@Override
	public boolean isVariable() {
		return true;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
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
