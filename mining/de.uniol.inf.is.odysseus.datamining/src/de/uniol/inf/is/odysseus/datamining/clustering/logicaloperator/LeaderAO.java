package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class LeaderAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5809997584476169607L;
	private Double threshold;
	private SDFAttributeList attributes;
	
	public Double getThreshold() {
		return threshold;
	}

	public SDFAttributeList getAttributes() {
		return attributes;
	}



	public LeaderAO() {

	}

	public LeaderAO(LeaderAO o) {
		this.threshold = o.threshold;
		this.attributes = o.attributes.clone();
	}

	public int[] determineRestrictList(){
		return calcRestrictList(this.getInputSchema(), attributes);
	}

	
	
	public static int[] calcRestrictList(SDFAttributeList in, SDFAttributeList out){
		int[] ret = new int[out.size()];
		int i=0;
		for (SDFAttribute a:out){
			int j = 0;
			int k = i;
			for(SDFAttribute b:in){
				if (b.equals(a)){
					ret[i++] = j;
				}
				++j;
			}
			if (k ==i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}
	
	
	@Override
	public SDFAttributeList getOutputSchema() {
		SDFAttributeList outputSchema = new SDFAttributeList();
		SDFAttribute id = new SDFAttribute("cluster_id");
		id.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		outputSchema.add(id);
		
		SDFAttribute size = new SDFAttribute("cluster_size");
		id.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		outputSchema.add(size);
		outputSchema.addAll(attributes);
		
		return outputSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new LeaderAO(this);
	}

	public void setAttributes(SDFAttributeList attributes) {
		this.attributes = attributes;

	}

	public void setThreshold(Double threshold) {

		this.threshold = threshold;
	}

}
