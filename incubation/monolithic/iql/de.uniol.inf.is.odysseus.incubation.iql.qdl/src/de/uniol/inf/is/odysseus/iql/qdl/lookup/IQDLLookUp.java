package de.uniol.inf.is.odysseus.iql.qdl.lookup;

import java.util.Collection;

import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;

public interface IQDLLookUp extends IIQLLookUp {
	public static String AUTO_CREATE = "auto_create";

	Collection<String> getMetadataKeys();

	Collection<String> getMetadataValues(String key);

}
