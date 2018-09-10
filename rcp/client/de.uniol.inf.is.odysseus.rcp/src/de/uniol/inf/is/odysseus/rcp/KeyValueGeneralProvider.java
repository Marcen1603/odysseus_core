package de.uniol.inf.is.odysseus.rcp;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;

public class KeyValueGeneralProvider extends AbstractKeyValueGeneralUpdaterProvider {

	private static final Logger LOG = LoggerFactory.getLogger(KeyValueGeneralProvider.class);

	@Override
	public String getTitle() {
		return "Key-values";
	}

	@Override
	protected Map<String, String> getKeyValuePairs(IPhysicalOperator operator) {
		if( operator instanceof IPhysicalOperatorKeyValueProvider ) {
			IPhysicalOperatorKeyValueProvider provider = (IPhysicalOperatorKeyValueProvider)operator;
			
			Optional<Map<String, String>> optKeys = tryGetKeyValuesFromProvider(provider);
			if( optKeys.isPresent() ) {
				return Maps.newHashMap(optKeys.get()); // copy to be safe from later changes from the operator
			}
		}
		return Maps.newHashMap();
	}

	private static Optional<Map<String, String>> tryGetKeyValuesFromProvider( IPhysicalOperatorKeyValueProvider provider ) {
		try {
			return Optional.fromNullable(provider.getKeyValues());
		} catch( Throwable t ) {
			LOG.error("Could not get the keys and values from physical operator keyvalue provider", t);
			return Optional.absent();
		}
	}
}
