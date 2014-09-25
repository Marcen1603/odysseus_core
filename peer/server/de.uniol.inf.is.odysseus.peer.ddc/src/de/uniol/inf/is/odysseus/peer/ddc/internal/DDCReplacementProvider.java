package de.uniol.inf.is.odysseus.peer.ddc.internal;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.script.parser.IReplacementProvider;

/**
 * Connection class between DDC and OdysseusScriptParser
 * 
 * @author Timo
 */
public class DDCReplacementProvider implements IReplacementProvider {

	private static final String DDC_SEPERATOR = ",";
	
	private static final Logger LOG = LoggerFactory.getLogger(DDCReplacementProvider.class);

	private static IDistributedDataContainer ddc;

	// called by OSGi-DS
	public static void bindDDC(IDistributedDataContainer serv) {
		ddc = serv;
	}

	// called by OSGi-DS
	public static void unbindDDC(IDistributedDataContainer serv) {
		if (ddc == serv) {
			ddc = null;
		}
	}

	@Override
	public Collection<String> getReplacementKeys() {
		Set<DDCKey> keys = ddc.getKeys();
		return toStringKeys(keys);
	}

	private static Collection<String> toStringKeys(Set<DDCKey> keys) {
		Collection<String> stringKeys = Lists.newArrayListWithCapacity(keys.size());
		for (DDCKey key : keys) {
			stringKeys.add(key.toString());
		}
		return stringKeys;
	}

	@Override
	public String getReplacementValue(String key) {
		key = key.toLowerCase();
		
		String[] keyDimensions = key.split(DDC_SEPERATOR);
		
		try {
			if(keyDimensions.length>1) {
				return ddc.get(new DDCKey(keyDimensions)).getValue();
			}
			else {
				return ddc.get(new DDCKey(key)).getValue();
			}
		} catch (MissingDDCEntryException e) {
			LOG.error("Could not get key '{}' from DDC", key, e);
			return "";
		}
	}

}
