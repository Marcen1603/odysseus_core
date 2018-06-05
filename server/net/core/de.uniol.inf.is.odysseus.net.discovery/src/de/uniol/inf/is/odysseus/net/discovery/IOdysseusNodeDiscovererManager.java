package de.uniol.inf.is.odysseus.net.discovery;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

public interface IOdysseusNodeDiscovererManager {

	public void add(IOdysseusNodeDiscoverer discoverer);
	public void remove(IOdysseusNodeDiscoverer discoverer);
	public Optional<IOdysseusNodeDiscoverer> get(String discovererName);

	public ImmutableCollection<String> getNames();
	public boolean existsName(String discovererName);

}