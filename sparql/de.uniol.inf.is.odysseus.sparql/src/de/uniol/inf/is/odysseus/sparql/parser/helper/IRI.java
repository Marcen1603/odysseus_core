package de.uniol.inf.is.odysseus.sparql.parser.helper;

public class IRI implements INode {

	String iri = null;
	public IRI(String iri){
		this.iri = iri;
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
	public String getName() {
		return this.getIRI();
	}

	@Override
	public void setName(String name) {
		this.setIRI(name);
	}

	@Override
	public boolean isIRI() {
		return true;
	}

	public String getIRI(){
		return this.iri;
	}
	
	public void setIRI(String iri){
		this.iri = iri;
	}


	@Override
	public boolean isBlankNode() {
		// TODO Auto-generated method stub
		return false;
	}
}
