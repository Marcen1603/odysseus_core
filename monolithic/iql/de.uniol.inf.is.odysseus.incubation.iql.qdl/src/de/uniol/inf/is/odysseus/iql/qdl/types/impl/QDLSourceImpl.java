package de.uniol.inf.is.odysseus.iql.qdl.types.impl;

import de.uniol.inf.is.odysseus.iql.qdl.types.source.Source;

public class QDLSourceImpl extends AbstractSubscribable implements Source{
	private String name;
	
	public QDLSourceImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getAttribute(String attribute) {
		return attribute;
	}

}
