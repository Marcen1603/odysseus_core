package de.uniol.inf.is.odysseus.core.securitypunctuation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ISecurityPunctuation {
	
	public Boolean evaluate(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema);

	public ISecurityPunctuation processSP(ISecurityPunctuation sp2);
	public ISecurityPunctuation union(ISecurityPunctuation sp2);
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2);
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2, Long newTS);

	public SDFSchema getSchema();
	
	public Object getAttribute(String key);	
	public Long getLongAttribute(String key);
	public Integer getIntegerAttribute(String key);
	public String getStringAttribute(String key);
	public String[] getStringArrayAttribute(String string);
	public ArrayList<String> getStringArrayListAttribute(String string);

	public int getNumberofAttributes();
	public Boolean isEmpty();
}
