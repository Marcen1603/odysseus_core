package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public abstract class AbstractClusteringAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5930667720134167936L;
	protected SDFAttributeList attributes;
	protected AbstractClusteringAO() {
		
	}
	public AbstractClusteringAO(AbstractClusteringAO copy) {
		super(copy);
		this.attributes = copy.getAttributes().clone();
	}
	
	public SDFAttributeList getAttributes() {
		return attributes;
	}

	public int[] determineRestrictList() {
		return calcRestrictList(this.getInputSchema(), attributes);
	}

	public static int[] calcRestrictList(SDFAttributeList in,
			SDFAttributeList out) {
		int[] ret = new int[out.size()];
		int i = 0;
		for (SDFAttribute a : out) {
			int j = 0;
			int k = i;
			for (SDFAttribute b : in) {
				if (b.equals(a)) {
					ret[i++] = j;
				}
				++j;
			}
			if (k == i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}

	public void setAttributes(SDFAttributeList attributes) {
		this.attributes = attributes;

	}

	@Override
	public SDFAttributeList getOutputSchema() {

		SDFAttributeList outputSchema = new SDFAttributeList();
		SDFAttribute id = new SDFAttribute("cluster_id");
		id.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		outputSchema.add(id);
		outputSchema.addAll(getInputSchema().clone());
		return outputSchema;
	}
	
	@Override
	public SDFAttributeList getOutputSchema(int port) {
		if (port == 0) {
			return getOutputSchema();
		} else {
			SDFAttributeList clusterSchema = new SDFAttributeList();
			SDFAttribute idA = new SDFAttribute("cluster_id");
			idA.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			clusterSchema.add(idA);
			SDFAttribute idCount = new SDFAttribute("cluster_count");
			idCount.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
			clusterSchema.add(idCount);
			clusterSchema.addAll(attributes.clone());
			return clusterSchema;
		}
	}
}
