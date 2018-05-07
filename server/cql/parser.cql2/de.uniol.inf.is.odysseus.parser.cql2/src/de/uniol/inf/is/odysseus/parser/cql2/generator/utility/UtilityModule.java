package de.uniol.inf.is.odysseus.parser.cql2.generator.utility;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class UtilityModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IUtilityService.class).to(UtilityService.class).in(Singleton.class);;
	}

	
}
