package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

public class CEPTreeItem {
	
	private String name;
	
	private Object content;

	private ArrayList<CEPTreeItem> children;

	CEPTreeItem parent;

	@SuppressWarnings("unchecked")
	CEPTreeItem(StateMachineInstance content){
		this.content = content;
		this.parent = null;
		this.name = "Instance " + ((StateMachineInstance) content).hashCode();
		this.children = new ArrayList<CEPTreeItem> ();
	}
	
	@SuppressWarnings("unchecked")
	CEPTreeItem(CepOperator content){
		this.content = content;
		this.parent = null;
		this.name = "Machine " + ((CepOperator) content).hashCode();
		this.children = new ArrayList<CEPTreeItem> ();
	}
	
	CEPTreeItem(){
		this.content = null;
		this.parent = null;
		this.children = new ArrayList<CEPTreeItem> ();
	}

	public Object getContent() {
		return content;
	}

	public ArrayList<CEPTreeItem> getChildren() {
		return children;
	}

	public CEPTreeItem getParent() {
		return parent;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public void add(CEPTreeItem child) {
		this.children.add(child);
	}

	public void setParent(CEPTreeItem parent) {
		this.parent = parent;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}
	
}
