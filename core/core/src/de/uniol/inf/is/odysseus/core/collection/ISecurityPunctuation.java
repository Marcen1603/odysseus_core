package de.uniol.inf.is.odysseus.core.collection;

import java.util.List;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ISecurityPunctuation {
	
	public Boolean evaluateAll(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema);
	
	public ISecurityPunctuation union(ISecurityPunctuation sp2);
}
