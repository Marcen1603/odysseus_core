package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class CacheModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ICacheService.class).to(CacheService.class).in(Singleton.class);
	}

}
