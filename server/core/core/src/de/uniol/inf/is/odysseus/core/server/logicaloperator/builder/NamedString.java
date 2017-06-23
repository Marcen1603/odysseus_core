package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.IClone;

public class NamedString implements IClone {
	
	private String name;
	private String content;
	
	public NamedString(String name, String content) {
		this.name = name;
		this.content = content;
	}
	
	public NamedString(NamedString other) {
		this.name = other.getName();
		this.content = other.getContent();
	}

	public String getName() {
		return this.name;
	}
	
	public String getContent() {
		return this.content;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("['");
		stringBuilder.append(this.content);
		stringBuilder.append("','");
		stringBuilder.append(this.name);
		return stringBuilder.toString();
	}
	
	@Override
	public NamedString clone() {
		return new NamedString(this);
	}
}
