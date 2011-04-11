package de.uniol.inf.is.odysseus.sparql.parser.helper;

public class Triple {
	private INode subject;
	private INode predicate;
	private INode object;
	
	public Triple(){
		this.subject = null;
		this.object = null;
		this.predicate = null;
	}
	
	public Triple(INode subject, INode predicte, INode object){
		this.subject = subject;
		this.predicate = predicte;
		this.object = object;
	}
	
	
	
	public INode getSubject() {
		return subject;
	}

	public void setSubject(INode subject) {
		this.subject = subject;
	}

	public INode getPredicate() {
		return predicate;
	}

	public void setPredicate(INode predicate) {
		this.predicate = predicate;
	}

	public INode getObject() {
		return object;
	}

	public void setObject(INode object) {
		this.object = object;
	}

}
