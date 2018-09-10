package de.uniol.inf.is.odysseus.script.parser;

import java.util.Collection;

public interface IReplacementProvider {

	public Collection<String> getReplacementKeys();
	public String getReplacementValue( String replacementKey );
	
}
