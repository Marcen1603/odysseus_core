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
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.LogicalOperatorCategory;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, doc="TODO", name = "LinearRegressionMerge", category={LogicalOperatorCategory.PROBABILISTIC})
public class LinearRegressionMergeAO extends UnaryLogicalOp {

	/**
     * 
     */
	private static final long serialVersionUID = 3075895311156052010L;
	/** The dependent attributes. */
	private List<SDFAttribute> dependentAttributes;
	/** The explanatory attributes. */
	private List<SDFAttribute> explanatoryAttributes;

	/**
	 * Default constructor.
	 */
	public LinearRegressionMergeAO() {
		super();
	}

	/**
	 * Clone constructor.
	 * 
	 * @param linearRegressionMergeAO
	 *            The object to copy from
	 */
	public LinearRegressionMergeAO(final LinearRegressionMergeAO linearRegressionMergeAO) {
		super(linearRegressionMergeAO);
		this.dependentAttributes = new ArrayList<SDFAttribute>(linearRegressionMergeAO.dependentAttributes);
		this.explanatoryAttributes = new ArrayList<SDFAttribute>(linearRegressionMergeAO.explanatoryAttributes);
	}

	/**
	 * Sets the value of the dependentAttributes property.
	 * 
	 * @param dependentAttributes
	 *            The dependent attributes
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "DEPENDENT", isList = true, optional = false)
	public final void setDependentAttributes(final List<SDFAttribute> dependentAttributes) {
		this.dependentAttributes = dependentAttributes;
	}

	/**
	 * Gets the value of the dependentAttributes property.
	 * 
	 * @return the dependent attributes
	 */
	@GetParameter(name = "DEPENDENT")
	public final List<SDFAttribute> getDependentAttributes() {
		if (this.dependentAttributes == null) {
			this.dependentAttributes = new ArrayList<SDFAttribute>();
		}
		return this.dependentAttributes;
	}

	/**
	 * Sets the value of the explanatoryAttributes property.
	 * 
	 * @param explanatoryAttributes
	 *            The explanatory attributes
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "EXPLANATORY", isList = true, optional = false)
	public final void setExplanatoryAttributes(final List<SDFAttribute> explanatoryAttributes) {
		this.explanatoryAttributes = explanatoryAttributes;
	}

	/**
	 * Gets the value of the explanatoryAttributes property.
	 * 
	 * @return the explanatory attributes
	 */
	@GetParameter(name = "EXPLANATORY")
	public final List<SDFAttribute> getExplanatoryAttributes() {
		if (this.explanatoryAttributes == null) {
			this.explanatoryAttributes = new ArrayList<SDFAttribute>();
		}
		return this.explanatoryAttributes;
	}

	/**
	 * Gets the position of the regression coefficients in the input schema.
	 * 
	 * @return The position of the regression coefficients
	 */
	public final int getRegressionCoefficientsPos() {
		final SDFSchema schema = this.getInputSchema();
		return schema.indexOf(schema.findAttribute("$coefficients"));
	}

	/**
	 * Gets the position of the residual in the input schema.
	 * 
	 * @return The position of the residual
	 */
	public final int getResidualPos() {
		final SDFSchema schema = this.getInputSchema();
		return schema.indexOf(schema.findAttribute("$residual"));
	}

	/**
	 * 
	 * @return The attribute positions of all dependent attributes
	 */
	public final int[] determineDependentList() {
		return SchemaUtils.getAttributePos(this.getInputSchema(), this.getDependentAttributes());
	}

	/**
	 * 
	 * @return The attribute positions of all explanatory attributes
	 */
	public final int[] determineExplanatoryList() {
		return SchemaUtils.getAttributePos(this.getInputSchema(), this.getExplanatoryAttributes());
	}

	@Override
	public final LinearRegressionMergeAO clone() {
		return new LinearRegressionMergeAO(this);
	}

}
