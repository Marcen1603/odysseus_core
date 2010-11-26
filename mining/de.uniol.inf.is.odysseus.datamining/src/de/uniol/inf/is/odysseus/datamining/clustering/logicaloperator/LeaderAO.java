package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;

import de.uniol.inf.is.odysseus.datamining.logicaloperator.AbstractDataMiningAO;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class LeaderAO extends AbstractDataMiningAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5809997584476169607L;
	private Double threshold;

	public Double getThreshold() {
		return threshold;
	}

	public LeaderAO() {

	}

	public LeaderAO(LeaderAO o) {
		super(o);
		this.threshold = o.threshold;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		SDFAttributeList outputSchema = new SDFAttributeList();
		SDFAttribute id = new SDFAttribute("cluster_id");
		id.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		outputSchema.add(id);
		outputSchema.addAll(getInputSchema());
// Outputschema für Cluster
//		SDFAttribute size = new SDFAttribute("cluster_size");
//		id.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
//		outputSchema.add(size);
//		outputSchema.addAll(attributes);

		return outputSchema;
	}

	@Override
	public LeaderAO clone() {
		return new LeaderAO(this);
	}

	public void setThreshold(Double threshold) {

		this.threshold = threshold;
	}

}
