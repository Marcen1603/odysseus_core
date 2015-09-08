package de.uniol.inf.is.odysseus.iql.basic.service;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class BasicIQLServiceObserver extends AbstractIQLServiceObserver{

	@Inject
	public BasicIQLServiceObserver() {
		super();
	}

	@Override
	protected Collection<IIQLService> getServices() {
		Collection<IIQLService> result = new HashSet<>();
		result.addAll(IQLServiceBinding.getInstance().getServices());
		return result;
	}

}
