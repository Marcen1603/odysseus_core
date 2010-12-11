package de.uniol.inf.is.odysseus.datamining.clustering.builder;

import de.uniol.inf.is.odysseus.datamining.builder.AbstractDataMiningAOBuilder;
import de.uniol.inf.is.odysseus.datamining.builder.AttributeOutOfRangeException;
import de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator.SimpleSinglePassKMeansAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class SimpleSinglePassKMeansAOBuilder extends
		AbstractClusteringAOBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6320176622859137201L;
	private static final String CLUSTERCOUNT = "CLUSTERCOUNT";
	private IntegerParameter clusterCount;
	
	private static final String BUFFERSIZE = "BUFFERSIZE";
	private IntegerParameter bufferSize;
	
	public SimpleSinglePassKMeansAOBuilder() {
		clusterCount = new IntegerParameter(CLUSTERCOUNT, REQUIREMENT.MANDATORY);
		bufferSize = new IntegerParameter(BUFFERSIZE, REQUIREMENT.MANDATORY);
		setParameters(bufferSize);
		setParameters(clusterCount);
	}
	
	@Override
	protected ILogicalOperator createOperatorInternal() {
		SimpleSinglePassKMeansAO simpleSinglePassKMeansAO = new SimpleSinglePassKMeansAO();
		simpleSinglePassKMeansAO.setAttributes(new SDFAttributeList(attributes.getValue()));
		simpleSinglePassKMeansAO.setClusterCount(clusterCount.getValue());
		simpleSinglePassKMeansAO.setBufferSize(bufferSize.getValue());
		return simpleSinglePassKMeansAO;
	}

	@Override
	protected boolean internalValidation() {
		if(clusterCount.getValue() <= 0){
			addError(new AttributeOutOfRangeException(clusterCount.getName(), "has to be greater then zero"));
			return false;
		} else if(bufferSize.getValue() < clusterCount.getValue()){
			addError(new AttributeOutOfRangeException(bufferSize.getName(), "has to be equal or greater then the value of "+clusterCount.getName()));
			return false;
		}
		return super.internalValidation();
	}
	
}
