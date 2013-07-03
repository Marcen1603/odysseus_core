package de.uniol.inf.is.odysseus.probabilistic.continuous.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "LinearRegressionMerge")
public class LinearRegressionMergeAO extends UnaryLogicalOp {

	/**
     * 
     */
	private static final long serialVersionUID = 3075895311156052010L;
	private List<SDFAttribute> dependentAttributes;
	private List<SDFAttribute> explanatoryAttributes;

	public LinearRegressionMergeAO() {
		super();
	}

	public LinearRegressionMergeAO(final LinearRegressionMergeAO linearRegressionMergeAO) {
		super(linearRegressionMergeAO);
		this.dependentAttributes = new ArrayList<SDFAttribute>(linearRegressionMergeAO.dependentAttributes);
		this.explanatoryAttributes = new ArrayList<SDFAttribute>(linearRegressionMergeAO.explanatoryAttributes);
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "DEPENDENT", isList = true, optional = false)
	public final void setDependentAttributes(final List<SDFAttribute> dependentAttributes) {
		this.dependentAttributes = dependentAttributes;
	}

	@GetParameter(name = "DEPENDENT")
	public final List<SDFAttribute> getDependentAttributes() {
		if (this.dependentAttributes == null) {
			this.dependentAttributes = new ArrayList<SDFAttribute>();
		}
		return this.dependentAttributes;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "EXPLANATORY", isList = true, optional = false)
	public final void setExplanatoryAttributes(final List<SDFAttribute> explanatoryAttributes) {
		this.explanatoryAttributes = explanatoryAttributes;
	}

	@GetParameter(name = "EXPLANATORY")
	public final List<SDFAttribute> getExplanatoryAttributes() {
		if (this.explanatoryAttributes == null) {
			this.explanatoryAttributes = new ArrayList<SDFAttribute>();
		}
		return this.explanatoryAttributes;
	}

	public final int getRegressionCoefficientsPos() {
		SDFSchema schema = this.getInputSchema();
		return schema.indexOf(schema.findAttribute("$coefficients"));
	}

	public final int[] determineDependentList() {
		return SchemaUtils.getAttributePos(getInputSchema(), getDependentAttributes());
	}

	public final int[] determineExplanatoryList() {
		return SchemaUtils.getAttributePos(getInputSchema(), getExplanatoryAttributes());
	}

	@Override
	public final LinearRegressionMergeAO clone() {
		return new LinearRegressionMergeAO(this);
	}

}
