package de.uniol.inf.is.odysseus.keyperformanceindicators.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="KEYPERFORMANCEINDICATORS", minInputPorts=1, maxInputPorts=1, category={LogicalOperatorCategory.BASE}, doc="Allows KeyPerformanceIndicators for social media on input streams.")
public class KeyPerformanceIndicatorsAO extends UnaryLogicalOp 
{
	private static final long serialVersionUID = -4196540875708298361L;
	private int totalInputPorts = 1;
	private String kpiName;
	private double thresholdValue = 0;
	private List<String> subsetOfTerms = new ArrayList<String>();
	private List<String> totalQuantityOfTerms = new ArrayList<String>();
	private SDFAttribute incomingText;
	private SDFAttribute userNames;

	public KeyPerformanceIndicatorsAO()
	{
		super();
	}
	
	
	public KeyPerformanceIndicatorsAO(KeyPerformanceIndicatorsAO keyPerformanceIndicatorsAO)
	{
		super(keyPerformanceIndicatorsAO);
		
		this.kpiName = keyPerformanceIndicatorsAO.kpiName;
		this.subsetOfTerms = keyPerformanceIndicatorsAO.subsetOfTerms;
		this.totalQuantityOfTerms = keyPerformanceIndicatorsAO.totalQuantityOfTerms;
		this.incomingText = keyPerformanceIndicatorsAO.incomingText;
		this.userNames = keyPerformanceIndicatorsAO.userNames;
		this.totalInputPorts = keyPerformanceIndicatorsAO.totalInputPorts;
	}
	
	
	@Override 
	public SDFSchema getOutputSchemaIntern(int pos){	
		
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		SDFAttribute output = new SDFAttribute(null, this.kpiName, SDFDatatype.DOUBLE, null, null, null);
		outputAttributes.add(output);
		setOutputSchema(SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema(0)));
			
		return getOutputSchema();
	}
		
	@Parameter(name="thresholdValue", type = DoubleParameter.class, optional = false)
	public void setThresholdValue(double thresholdValue) {
		this.thresholdValue = thresholdValue;
	}
	
	@Parameter(name="subsetOfTerms", type = StringParameter.class, isList=true, optional = false)
	public void setSubsetOfTerms(List<String> subsetOfTerms) {
		this.subsetOfTerms = subsetOfTerms;
	}
	
	@Parameter(name="totalQuantityOfTerms", type = StringParameter.class, isList=true, optional = false)
	public void setTotalQuantityOfTerms(List<String> totalQuantityOfTerms) {
		this.totalQuantityOfTerms = totalQuantityOfTerms;
	}
	
	@Parameter(name="kpiName", type = StringParameter.class, optional = false)
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
		
	@Parameter(name="userNames", type = ResolvedSDFAttributeParameter.class, optional = true)
	public void setUserIDs(SDFAttribute userNames) {
		this.userNames = userNames;
	}
	
	@Parameter(name = "incomingText", type=ResolvedSDFAttributeParameter.class, optional= false, doc="")
	public void setIncomingText(SDFAttribute incomingText) {
		this.incomingText = incomingText;
	}
	
	public int getInputPorts()
	{
		return this.totalInputPorts;
	}
	
	public int getOutputPorts()
	{
		return 0;
	}
	
   public String getKpiName() {
		return this.kpiName;
	}	
	
	public double getThresholdValue() {
		return this.thresholdValue;
	}

	public List<String> getSubsetOfTerms() {
		return this.subsetOfTerms;
	}

	public List<String> getTotalQuantityOfTerms() {
		return this.totalQuantityOfTerms;
	}

	public SDFAttribute getIncomingText() {
		return this.incomingText;
	}

	public SDFAttribute getUserNames() {
		return this.userNames;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new KeyPerformanceIndicatorsAO(this);
	}

	@Override
	public boolean isValid(){
		if(this.kpiName.equals("conversationreach") && this.userNames == null){
			System.out.println("Error. No user ids found!");
			return false;
		}
		return true;
	}
}
