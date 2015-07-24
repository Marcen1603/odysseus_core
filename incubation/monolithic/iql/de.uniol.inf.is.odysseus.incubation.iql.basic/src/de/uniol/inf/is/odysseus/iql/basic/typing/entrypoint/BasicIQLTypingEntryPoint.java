package de.uniol.inf.is.odysseus.iql.basic.typing.entrypoint;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.uniol.inf.is.odysseus.iql.basic.typing.builder.BasicIQLTypeBuilder;

@Singleton
public class BasicIQLTypingEntryPoint extends AbstractIQLTypingEntryPoint<BasicIQLTypeBuilder> {

	@Inject
	public BasicIQLTypingEntryPoint(BasicIQLTypeBuilder creator) {
		super(creator);
	}



}
