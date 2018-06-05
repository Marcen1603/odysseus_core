package de.uniol.inf.is.odysseus.rcp.login.cfg;

import java.util.Collection;

import com.google.common.base.Optional;

public interface ILoginConfigurationProvider {

	public Optional<String> get( String key );
	public void set( String key, String value );
	
	public Collection<String> getKeys();
	
}
