package de.uniol.inf.is.odysseus.relational_interval.logicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(name = "FastMedian", minInputPorts = 1, maxInputPorts = 1, doc = "Calculate the median for one attribute in the input tuples", category = { LogicalOperatorCategory.ADVANCED })
public class RelationalFastMedianAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -9112124571817836462L;

	private SDFAttribute medianAttribute;
	private boolean numericalMedian = false;
	private boolean useHistogram = false;
	private long roundingFactor = 0;
	private boolean appendGlobalMedian = false;
	private List<Double> percentiles = null;
	private List<SDFAttribute> groupingAttributes;

	public RelationalFastMedianAO(RelationalFastMedianAO op) {
		super(op);
		if (op.groupingAttributes != null) {
			this.groupingAttributes = new LinkedList<>();
			this.groupingAttributes.addAll(op.groupingAttributes);
		}
		this.medianAttribute = op.medianAttribute;
		this.numericalMedian = op.numericalMedian;
		this.useHistogram = op.useHistogram;
		this.roundingFactor = op.roundingFactor;
		this.percentiles = op.percentiles;
		this.appendGlobalMedian = op.appendGlobalMedian;
	}

	public RelationalFastMedianAO() {
	}

	@Override
	public RelationalFastMedianAO clone() {
		return new RelationalFastMedianAO(this);
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}

	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	@Parameter(name = "appendGlobalMedian", optional = true, type = BooleanParameter.class, doc = "If a GROUP_BY element is given, the global median (i.e. median without respecting groups) will be annotated to each element.")
	public void setAppendGlobalMedian(boolean appendGlobalMedian) {
		this.appendGlobalMedian = appendGlobalMedian;
	}

	public boolean isAppendGlobalMedian() {
		return appendGlobalMedian;
	}

	@Parameter(name = "Attribute", optional = false, type = ResolvedSDFAttributeParameter.class)
	public void setMedianAttribute(SDFAttribute attribute) {
		this.medianAttribute = attribute;
	}

	public SDFAttribute getMedianAttribute() {
		return medianAttribute;
	}

	@Parameter(name = "numerical", optional = true, type = BooleanParameter.class)
	public void setIsNumericalMedian(boolean numericalMedian) {
		this.numericalMedian = numericalMedian;
	}
	
	public boolean getIsNumericalMedian() {
		return numericalMedian;
	}

	public boolean isNumericalMedian() {
		return numericalMedian;
	}

	@Parameter(name = "percentiles", optional = true, type = DoubleParameter.class, isList = true)
	public void setPercentiles(List<Double> percentiles) {
		this.percentiles = percentiles;
	}

	public List<Double> getPercentiles() {
		return percentiles;
	}

	@Parameter(name = "histogram", optional = true, type = BooleanParameter.class)
	public void setUseHistogram(boolean useHistogram) {
		this.useHistogram = useHistogram;
	}

	public boolean isUseHistogram() {
		return useHistogram;
	}

	@Parameter(name = "roundingFactor", optional = true, type = LongParameter.class)
	public void setRoundingFactor2(long roundingFactor) {
		this.roundingFactor = roundingFactor;
	}
	
	// needed by PQL-Generator
	public long getRoundingFactor2() {
		return this.roundingFactor;
	}

	public long getRoundingFactor() {
		return roundingFactor;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> outattr = new LinkedList<>();
		if (groupingAttributes != null) {
			outattr.addAll(groupingAttributes);
		}
		if (percentiles != null) {
			for (Double d : percentiles) {
				SDFAttribute pAttr = new SDFAttribute(
						medianAttribute.getSourceName(), 
								medianAttribute.getAttributeName()+"_p_"+d,
						medianAttribute);
				outattr.add(pAttr);
			}
		} else {
			outattr.add(medianAttribute);
		}
		if (appendGlobalMedian) {
			SDFAttribute globaleMedianAttribute = new SDFAttribute(
					medianAttribute.getSourceName(), "global_"
							+ medianAttribute.getAttributeName(),
					medianAttribute);
			outattr.add(globaleMedianAttribute);
		}
		SDFSchema output = SDFSchemaFactory.createNewWithAttributes(outattr, getInputSchema(0));
		return output;
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (roundingFactor > 0 && !useHistogram) {
			addError(
					"RoundingFactor can only be used in histogram version");
			isValid = false;
		}
		if (numericalMedian && percentiles != null) {
			addError(
					"You can only use percentiles or numerical median!");
			isValid = false;
		}
		if (!useHistogram && percentiles != null){
			addError(
					"Percentiles only allowed for histogram version!");
			isValid = false;			
		}
		return isValid && super.isValid();
	}

}
