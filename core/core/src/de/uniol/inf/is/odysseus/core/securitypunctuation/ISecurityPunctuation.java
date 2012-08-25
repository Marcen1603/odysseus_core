package de.uniol.inf.is.odysseus.core.securitypunctuation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ISecurityPunctuation {
	
	public Boolean evaluateAll(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema);
	
	public ISecurityPunctuation union(ISecurityPunctuation sp2);

	public Object getAttribute(String key);	
	public Long getLongAttribute(String key);
	public Integer getIntegerAttribute(String key);
	public String getStringAttribute(String key);
}
