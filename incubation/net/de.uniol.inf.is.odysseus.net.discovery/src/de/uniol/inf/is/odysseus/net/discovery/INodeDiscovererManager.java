package de.uniol.inf.is.odysseus.net.discovery;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

public interface INodeDiscovererManager {

	public void add(INodeDiscoverer discoverer);
	public void remove(INodeDiscoverer discoverer);
	public Optional<INodeDiscoverer> get(String discovererName);

	public ImmutableCollection<String> getNames();
	public boolean existsName(String discovererName);

}