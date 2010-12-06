package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;

import de.uniol.inf.is.odysseus.datamining.logicaloperator.AbstractDataMiningAO;
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
		super();
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


		return outputSchema;
	}

	@Override
	public SDFAttributeList getOutputSchema(int port) {
		if (port == 0) {
			return getOutputSchema();
		} else {
			SDFAttributeList clusterSchema = new SDFAttributeList();
			SDFAttribute idA = new SDFAttribute("leadercluster", "cluster_id");
			idA.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			clusterSchema.add(idA);
			SDFAttribute idCount = new SDFAttribute("leadercluster", "cluster_count");
			idCount.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
			clusterSchema.add(idCount);
			clusterSchema.addAll(attributes.clone());
			return clusterSchema;
		}
	}

	@Override
	public LeaderAO clone() {
		return new LeaderAO(this);
	}

	public void setThreshold(Double threshold) {

		this.threshold = threshold;
	}

}
