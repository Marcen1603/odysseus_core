package de.uniol.inf.is.odysseus.relational_interval.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.relational_interval.replacement.ReplacementRegistry;

@LogicalOperator(name = "Replacement", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can be used if a value is espected but was not delivered timely. Different methods to determine the missing value are available.", category = { LogicalOperatorCategory.PROCESSING })
public class ReplacementAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -8189563624334302716L;

	private long interval;
	private SDFAttribute timestampAttribute;
	private SDFAttribute qualityAttribute;
	private SDFAttribute valueAttribute;
	private String replacementMethod;

	public ReplacementAO(ReplacementAO op) {
		super(op);
		this.interval = op.interval;
		this.timestampAttribute = op.timestampAttribute;
		this.qualityAttribute = op.qualityAttribute;
		this.valueAttribute = op.valueAttribute;
		this.replacementMethod = op.replacementMethod;
	}

	public ReplacementAO() {
	}

	@Parameter(type = LongParameter.class, doc="Size of the intervals")
	public void setInterval(long interval) {
		this.interval = interval;	}

	public long getInterval() {
		return interval;
	}

	@Parameter(type=ResolvedSDFAttributeParameter.class, doc="The attribute with the timestamp attribute that should be updated.", optional = true)
	public void setTimestampAttribute(SDFAttribute timestampAttribute) {
		this.timestampAttribute = timestampAttribute;
	}
	
	public SDFAttribute getTimestampAttribute() {
		return timestampAttribute;
	}
	
	@Parameter(type=ResolvedSDFAttributeParameter.class, doc="The attribute with the quality attribute that should be updated.", optional = true)
	public void setQualityAttribute(SDFAttribute qualityAttribute) {
		this.qualityAttribute = qualityAttribute;
	}
	
	public SDFAttribute getQualityAttribute() {
		return qualityAttribute;
	}
	
	@Parameter(type=ResolvedSDFAttributeParameter.class, doc="The attribute with the value attribute.", optional = true)
	public void setValueAttribute(SDFAttribute valueAttribute) {
		this.valueAttribute = valueAttribute;
	}
	
	public SDFAttribute getValueAttribute() {
		return valueAttribute;
	}
	
	@Parameter(type=StringParameter.class, doc="The replacement method for missing value.")
	public void setReplacementMethod(String replacementMethod) {
		this.replacementMethod = replacementMethod;
	}
	
	public List<String> getReplacementMethods(){
		return ReplacementRegistry.getKeys();
	}
	
	public String getReplacementMethod() {
		return replacementMethod;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ReplacementAO(this);
	}
	
	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (interval <= 0){
			isValid = false;
			addError(
					"Interval must be greater zero!");		
		}
		
		// TODO Check for different methods the different required parameters
		
		return isValid;
	}

}
