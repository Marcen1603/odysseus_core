package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;

import de.uniol.inf.is.odysseus.datamining.logicaloperator.AbstractDataMiningAO;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class SimpleSinglePassKMeansAO extends AbstractDataMiningAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 288269790951499746L;
	private int clusterCount;
	private int bufferSize;

	
	public int getClusterCount() {
		return clusterCount;
	}

	protected SimpleSinglePassKMeansAO(SimpleSinglePassKMeansAO copy) {
		super(copy);
		this.clusterCount = copy.getClusterCount();
		this.bufferSize = copy.bufferSize;
	}
	
	public SimpleSinglePassKMeansAO() {	
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		
		SDFAttributeList outputSchema = new SDFAttributeList();
		SDFAttribute id = new SDFAttribute("cluster_id");
		id.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		outputSchema.add(id);
		outputSchema.addAll(getInputSchema());
		return outputSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new SimpleSinglePassKMeansAO(this);
	}

	public void setClusterCount(int clusterCount) {
				this.clusterCount = clusterCount;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = 	bufferSize;	
	}

	public int getBufferSize() {
		return bufferSize;
	}

}
