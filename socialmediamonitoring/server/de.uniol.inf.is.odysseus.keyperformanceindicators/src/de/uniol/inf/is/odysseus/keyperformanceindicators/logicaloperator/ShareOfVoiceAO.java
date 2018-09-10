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

@LogicalOperator(name="SHAREOFVOICE", minInputPorts=1, maxInputPorts=2, category={LogicalOperatorCategory.BASE}, doc="Allows to calculate the SoV.")
public class ShareOfVoiceAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -3161264549254599052L;
	
	private List<String> ownCompany;
	private List<String> allCompanies;
	private SDFAttribute incomingText;
	private double thresholdValue;
	
	private String nameOfThisKpi = "ShareOfVoice";
	
	public ShareOfVoiceAO()
	{
		super();
	}
	
	public ShareOfVoiceAO(ShareOfVoiceAO shareOfVoiceAO)
	{
		super(shareOfVoiceAO);
		this.ownCompany = shareOfVoiceAO.ownCompany;
		this.allCompanies = shareOfVoiceAO.allCompanies;
		this.incomingText = shareOfVoiceAO.incomingText;
		this.thresholdValue = shareOfVoiceAO.thresholdValue;
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos)
	{
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		SDFAttribute output = new SDFAttribute(null, this.nameOfThisKpi, SDFDatatype.DOUBLE, null, null, null);
		outputAttributes.add(output);
		setOutputSchema(SDFSchemaFactory.createNewWithAttributes(outputAttributes,getInputSchema(0)));
		
		return getOutputSchema();
	}
		
	@Parameter(name="ownCompany", type = StringParameter.class, isList = true, optional = false)
	public void setOwnCompany(List<String> ownCompany) {
		this.ownCompany = ownCompany;
	}
	
	@Parameter(name="allCompanies", type = StringParameter.class, isList = true, optional = false)
	public void setAllCompanies(List<String> allCompanies) {
		this.allCompanies = allCompanies;
	}
	
	@Parameter(name="thresholdValue", type = DoubleParameter.class, optional = false)
	public void setThresholdValue(double thresholdValue){
		this.thresholdValue = thresholdValue;
	}
	
	@Parameter(name="incomingText", type = ResolvedSDFAttributeParameter.class, optional = false)
	public void setIncomingText(SDFAttribute incomingText) {
		this.incomingText = incomingText;
	}

	public List<String> getOwnCompany() {
		return this.ownCompany;
	}

	public List<String> getAllCompanies() {
		return this.allCompanies;
	}

	public SDFAttribute getIncomingText() {
		return this.incomingText;
	}
	
	public double getThresholdValue(){
		return this.thresholdValue;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ShareOfVoiceAO(this);
	}
}
