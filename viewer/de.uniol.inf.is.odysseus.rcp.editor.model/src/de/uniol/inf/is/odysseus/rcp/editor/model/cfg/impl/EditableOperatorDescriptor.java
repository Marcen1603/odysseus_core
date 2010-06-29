package de.uniol.inf.is.odysseus.rcp.editor.model.cfg.impl;

import de.uniol.inf.is.odysseus.rcp.editor.model.cfg.IOperatorDescriptor;

public class EditableOperatorDescriptor implements IOperatorDescriptor {

	private String name;
	private String className;
	private String label;
	private String group;

	public EditableOperatorDescriptor(String name, String className) {
		setName(name);
		setClassName(className);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getClassName() {
		return className;
	}

	public void setName(String name) {
		this.name = name;
		if( this.label == null ) 
			this.label = name;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("name=");
		sb.append(getName());
		sb.append(" class=");
		sb.append(getClassName());
		sb.append(" label=");
		sb.append(getLabel());
		sb.append("]");
		return sb.toString();
	}
}
