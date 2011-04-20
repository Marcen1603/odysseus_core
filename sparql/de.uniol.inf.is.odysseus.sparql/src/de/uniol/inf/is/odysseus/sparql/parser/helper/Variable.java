package de.uniol.inf.is.odysseus.sparql.parser.helper;

public class Variable implements INode{

	private String name;
	
	public Variable(String name){
		// remove ? and $ variable names
		if(name.startsWith("?") || name.startsWith("$")){
			this.name = name.substring(1);
		}
		else{
			this.name = name;
		}
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
		if(name.startsWith("?") || name.startsWith("$")){
			this.name = name.substring(1);
		}
		else{
			this.name = name;
		}
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
