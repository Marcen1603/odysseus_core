package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class NamedInterfaceRegistry<T extends INamedInterface> {

	private static final Logger LOG = LoggerFactory.getLogger(NamedInterfaceRegistry.class);
	
	private final Map<String, T> interfaceMap = Maps.newHashMap();

	public final void add( T interfaceContribution ) {
		Preconditions.checkNotNull(interfaceContribution, "Interface contribution to add to registry must not be null!");
		Preconditions.checkArgument(!contains(interfaceContribution.getName()), "Interface contribution %s already registered", interfaceContribution.getClass());
		
		interfaceMap.put(interfaceContribution.getName().toUpperCase(), interfaceContribution);
		LOG.debug("Query part allocator added : {}", interfaceContribution.getName().toUpperCase());
	}
	
	public final void remove( T allocator ) {
		Preconditions.checkNotNull(allocator, "Interface contribution to remove from registry must not be null!");
		
		String allocatorName = allocator.getName().toUpperCase();
		if( interfaceMap.containsKey(allocatorName)) {
			interfaceMap.remove(allocatorName);
			LOG.debug("Interface contribution removed : {}", allocatorName);
		} else {
			LOG.warn("Tried to remove Interface contribution which was not registered before: {}", allocatorName);
		}
	}
	
	public final boolean contains( String name) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of Interface contribution must not be null or empty!");
		
		return interfaceMap.containsKey(name.toUpperCase());
	}
	
	public final ImmutableCollection<String> getNames() {
		return ImmutableList.copyOf(interfaceMap.keySet());
	}
	
	public final T get( String name ) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of Interface contribution to get must not be null or empty!");
		
		return interfaceMap.get(name.toUpperCase());
	}
}
