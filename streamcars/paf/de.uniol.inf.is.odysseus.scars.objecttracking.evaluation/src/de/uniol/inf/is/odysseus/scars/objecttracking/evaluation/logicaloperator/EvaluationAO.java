package de.uniol.inf.is.odysseus.scars.objecttracking.evaluation.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class EvaluationAO<M extends IProbability> extends AbstractLogicalOperator{

	private static final long serialVersionUID = 7650711998042333164L;
	
	private String associationObjListPath;
	private String filteringObjListPath; 
	private String brokerObjListPath;

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(2);
	}

	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void initPaths(String associationObjListPath, String filteringObjListPaths, String brokerObjListPath) {
		this.associationObjListPath = associationObjListPath;
		this.filteringObjListPath = filteringObjListPaths;
		this.brokerObjListPath = brokerObjListPath;
	}

	public int[] getAssociationObjListPath() {
		return OrAttributeResolver.getAttributePath(this.getInputSchema(0), this.associationObjListPath);
	}
	
	public int[] getFilteringObjListPath() {
		return OrAttributeResolver.getAttributePath(this.getInputSchema(1), this.filteringObjListPath);
	}
	
	public int[] getBrokerObjListPath() {
		return OrAttributeResolver.getAttributePath(this.getInputSchema(2), this.brokerObjListPath);
	}

}
