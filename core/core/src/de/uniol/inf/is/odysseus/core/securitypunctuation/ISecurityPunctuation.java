package de.uniol.inf.is.odysseus.core.securitypunctuation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ISecurityPunctuation {
	
	public Boolean evaluate(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema);
	
	public void union(ISecurityPunctuation sp2);
	public void intersect(ISecurityPunctuation sp2);

	public SDFSchema getSchema();
	
	public Object getAttribute(String key);	
	public Long getLongAttribute(String key);
	public Integer getIntegerAttribute(String key);
	public String getStringAttribute(String key);
	public String[] getStringArrayAttribute(String string);

	public int getNumberofAttributes();
}
