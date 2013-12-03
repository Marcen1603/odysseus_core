package de.uniol.inf.is.odysseus.peer.distribute.registry;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartModificator;

public final class QueryPartModificatorRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartModificatorRegistry.class);
	
	private static QueryPartModificatorRegistry instance;
	
	private final Map<String, IQueryPartModificator> modificatorMap = Maps.newHashMap();

	public static QueryPartModificatorRegistry getInstance() {
		if( instance == null ) {
			instance = new QueryPartModificatorRegistry();
		}
		return instance;
	}
	
	public void add( IQueryPartModificator modificator ) {
		Preconditions.checkNotNull(modificator, "Query part modificator to add to registry must not be null!");
		Preconditions.checkArgument(!contains(modificator.getName()), "Query part modificator %s already registered", modificator.getClass());
		
		modificatorMap.put(modificator.getName().toUpperCase(), modificator);
		LOG.debug("Query part modificator added : {}", modificator.getName().toUpperCase());
	}
	
	public void remove( IQueryPartModificator modificator ) {
		Preconditions.checkNotNull(modificator, "Query part modificator to remove from registry must not be null!");
		
		String modificatorName = modificator.getName().toUpperCase();
		if( modificatorMap.containsKey(modificatorName)) {
			modificatorMap.remove(modificatorName);
			LOG.debug("Query part modificator removed : {}", modificatorName);
		} else {
			LOG.warn("Tried to remove query part modificator which was not registered before: {}", modificatorName);
		}
	}
	
	public boolean contains( String name) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of query part modificator must not be null or empty!");
		
		return modificatorMap.containsKey(name.toUpperCase());
	}
	
	public ImmutableCollection<String> getNames() {
		return ImmutableList.copyOf(modificatorMap.keySet());
	}
	
	public IQueryPartModificator get( String name ) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of query part modificator to get must not be null or empty!");
		
		return modificatorMap.get(name.toUpperCase());
	}
}
