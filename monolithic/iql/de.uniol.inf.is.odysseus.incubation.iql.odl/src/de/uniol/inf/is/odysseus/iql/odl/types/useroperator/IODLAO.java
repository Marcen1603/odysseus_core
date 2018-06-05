package de.uniol.inf.is.odysseus.iql.odl.types.useroperator;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface IODLAO extends ILogicalOperator {
	
	public Map<String, List<Object>> getMetadata();
	
	public void addMetadata(String key, Object value);

}
