package de.uniol.inf.is.odysseus.broker.evaluation.metric;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class MetricMeasureAO extends AbstractLogicalOperator{

	private static final long serialVersionUID = 4253188596531819983L;
	/** The queue schema. */
	private SDFAttributeList schema = new SDFAttributeList();	
	
	private String onAttribute;	

	public MetricMeasureAO(String onAttribute){
		this.onAttribute = onAttribute;
	}
	
	public MetricMeasureAO(MetricMeasureAO original){
		this.onAttribute = original.onAttribute;
		this.schema = original.schema;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.schema;
	}
	
	public void setOutputSchema(SDFAttributeList schema){
		this.schema = schema;
	}		

	public int getOnAttribute() {
		if(this.schema==null){
			return -1;
		}else{
			for(int i=0;i<this.schema.size();i++){			
				if(this.schema.getAttribute(i).getAttributeName().equals(this.onAttribute)){
					return i;
				}
			}
		}
		return -1;		
	}			
		
	@Override
	public MetricMeasureAO clone() {	
		return new MetricMeasureAO(this);
	}
}
