package de.uniol.inf.is.odysseus.parser.cql2.generator.builder;

import com.google.inject.AbstractModule;

public class PQLBuilderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AbstractPQLOperatorBuilder.class).to(PQLOperatorBuilder.class).asEagerSingleton();
		
	}
	
}
