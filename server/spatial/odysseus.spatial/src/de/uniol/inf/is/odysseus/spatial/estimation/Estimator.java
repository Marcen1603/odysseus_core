package de.uniol.inf.is.odysseus.spatial.estimation;

import java.util.Set;

public interface Estimator {
	
	public Set<String> estimateObjectsToPredict(String centerObjectId, double radius);

}
