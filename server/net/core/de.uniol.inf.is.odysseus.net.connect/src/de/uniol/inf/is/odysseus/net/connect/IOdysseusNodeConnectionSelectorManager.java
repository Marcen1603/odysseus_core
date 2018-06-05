package de.uniol.inf.is.odysseus.net.connect;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

public interface IOdysseusNodeConnectionSelectorManager {

	public ImmutableCollection<String> getSelectorNames();
	public Optional<IOdysseusNodeConnectionSelector> getSelector( String selectorName );
	
}
