package de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint;

import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLTypeBuilder;

public abstract class AbstractIQLTypingEntryPoint<C extends IIQLTypeBuilder> implements IIQLTypingEntryPoint {
	
	protected C builder;
	
	public AbstractIQLTypingEntryPoint(C creator) {
		this.builder = creator;
	}

}
