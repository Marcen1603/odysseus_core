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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="CONVERSATIONREACH", minInputPorts=1, maxInputPorts=2, category={LogicalOperatorCategory.BASE}, doc="Allows to calculate the Conversation Reach of a topic.")
public class ConversationReachAO extends BinaryLogicalOp 
{
	private static final long serialVersionUID = 3784372082965254689L;

	private List<String> concreteTopic;
	private List<String> allTopics;
	private SDFAttribute incomingText;
	private SDFAttribute userIDs;
	private double thresholdValue;
	
	private String nameOfThisKpi = "Conversation Reach";
		
	public ConversationReachAO()
	{
		super();
	}
	
	public ConversationReachAO(ConversationReachAO conversationReachAO)
	{
		super(conversationReachAO);
		this.allTopics = conversationReachAO.allTopics;
		this.concreteTopic = conversationReachAO.concreteTopic;
		this.incomingText = conversationReachAO.incomingText;
		this.userIDs = conversationReachAO.userIDs;
		this.thresholdValue = conversationReachAO.thresholdValue;
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos){
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		SDFAttribute output = new SDFAttribute(null, this.nameOfThisKpi, SDFDatatype.DOUBLE, null, null, null);
		outputAttributes.add(output);
		setOutputSchema(SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(0)));
		
		return getOutputSchema();
	}
	
	@Parameter(name="thresholdValue", type = DoubleParameter.class, optional = false)
	public void setThresholdValue(double thresholdValue) {
		this.thresholdValue = thresholdValue;
	}
	
	@Parameter(name="concreteTopic", type = StringParameter.class, isList=true, optional = false)
	public void setConcreteTopics(List<String> concreteTopic) {
		this.concreteTopic = concreteTopic;
	}
	
	@Parameter(name="allTopics", type = StringParameter.class, isList=true, optional = false)
	public void setAllTopics(List<String> allTopics){
		this.allTopics = allTopics;
	}
	
	@Parameter(name="incomingText", type = ResolvedSDFAttributeParameter.class, optional = false)
	public void setIncomingText(SDFAttribute incomingText) {
		this.incomingText = incomingText;
	}
	
	@Parameter(name="userIDs", type = ResolvedSDFAttributeParameter.class, optional = false)
	public void setUserIDs(SDFAttribute userIDs) {
		this.userIDs = userIDs;
	}
	
	public List<String> getConcreteTopic() {
		return this.concreteTopic;
	}

	public List<String> getAllTopics() {
		return this.allTopics;
	}

	public SDFAttribute getIncomingText() {
		return this.incomingText;
	}
	
	public SDFAttribute getUserIDs() {
		return this.userIDs;
	}
	
	public double getThresholdValue() {
		return this.thresholdValue;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ConversationReachAO(this);
	}
}
