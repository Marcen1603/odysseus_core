package de.uniol.inf.is.odysseus.keyperformanceindicators.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="AUDIENCEENGAGEMENT", minInputPorts=1, maxInputPorts=2, category={LogicalOperatorCategory.BASE}, doc="Allows to calculate the SoV.")
public class AudienceEngagementAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 3532434142413852187L;
		
	private List<String> concreteTopics;
	private List<String> allTopics;
	private SDFAttribute incomingText;
	private double thresholdValue;
	private int countOfAllTopics = 0;
	
	private String nameOfThisKpi = "AudienceEngagement";
	
	public AudienceEngagementAO()
	{
		super();
	}
	
	public AudienceEngagementAO(AudienceEngagementAO audienceEngagementAO)
	{
		super(audienceEngagementAO);
		this.concreteTopics = audienceEngagementAO.concreteTopics;
		this.allTopics = audienceEngagementAO.allTopics;
		this.incomingText = audienceEngagementAO.incomingText;
		this.thresholdValue = audienceEngagementAO.thresholdValue;
		this.countOfAllTopics = audienceEngagementAO.countOfAllTopics;
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos){
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		SDFAttribute output = new SDFAttribute(null, this.nameOfThisKpi, SDFDatatype.DOUBLE, null, null, null);
		outputAttributes.add(output);		
		return SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(0));
	}
	
	@Parameter(name="concreteTopics", type = StringParameter.class, isList = true, optional = false)
	public void setConcreteTopics(List<String> concreteTopics) {
		this.concreteTopics = concreteTopics;
	}
	
	@Parameter(name="allTopics", type = StringParameter.class, isList = true, optional = false)
	public void setAllTopics(List<String> allTopics) {
		this.allTopics = allTopics;
	}
	
	@Parameter(name="thresholdValue", type = DoubleParameter.class, optional = false)
	public void setThresholdValue(double thresholdValue){
		this.thresholdValue = thresholdValue;
	}
	
	@Parameter(name="incomingText", type = ResolvedSDFAttributeParameter.class, optional = false)
	public void setIncomingText(SDFAttribute incomingText) {
		this.incomingText = incomingText;
	}
	
	@Parameter(name="countOfAllTopics", type = IntegerParameter.class, optional = false)
	public void setCountOfAllTopics(int countOfAllTopics){
		this.countOfAllTopics = countOfAllTopics;
	}
		
	
	public List<String> getConcreteTopics() {
		return concreteTopics;
	}

	public List<String> getAllTopics() {
		return allTopics;
	}

	public SDFAttribute getIncomingText() {
		return incomingText;
	}

	public double getThresholdValue() {
		return thresholdValue;
	}
	
	public int getCountOfAllTopics(){
		return this.countOfAllTopics;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new AudienceEngagementAO(this);
	}

}
