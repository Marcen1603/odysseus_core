package de.uniol.inf.is.odysseus.temporaltypes.types;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * This class lists all available temporal datatypes.
 * 
 * @author Tobias Brandt
 *
 */
public class TemporalDatatype extends SDFDatatype {

	private static final long serialVersionUID = 4911387440959109155L;

	private static final Collection<SDFConstraint> temporalContraintInfo = new ArrayList<>(1);

	public static final String TEMPORAL_CONTRAINT = "isTemporal";

	public TemporalDatatype(String URI) {
		super(URI);
	}

	public TemporalDatatype(String datatypeName, KindOfDatatype type, SDFSchema schema) {
		super(datatypeName, type, schema, true);
	}

	public TemporalDatatype(String datatypeName, KindOfDatatype type, SDFDatatype subType) {
		super(datatypeName, type, subType, true);
	}

	/**
	 * Use this to get the standard temporal marker for attributes
	 * 
	 * @return The standard constraint to mark an attribute as temporal
	 */
	public static Collection<SDFConstraint> getTemporalConstraint() {
		if (temporalContraintInfo.size() == 0) {
			SDFConstraint temporalConstraint = new SDFConstraint(TEMPORAL_CONTRAINT, true);
			temporalContraintInfo.add(temporalConstraint);
		}
		return temporalContraintInfo;
	}

	/**
	 * Checks if an attribute is temporal, i.e., if it is marked as temporal.
	 * 
	 * @param attribute
	 *            The attribute to check
	 * @return true, if it is temporal, false otherwise
	 */
	public static boolean isTemporalAttribute(SDFAttribute attribute) {
		if (attribute.getDtConstraint(TemporalDatatype.TEMPORAL_CONTRAINT) != null
				&& attribute.getDtConstraint(TemporalDatatype.TEMPORAL_CONTRAINT).getValue() instanceof Boolean
				&& (Boolean) (attribute.getDtConstraint(TemporalDatatype.TEMPORAL_CONTRAINT).getValue())) {
			return true;
		}
		return false;
	}
	
	/**
	 * The search needs to be done on the input schema, because the temporal
	 * constraints are added to the input schema, not to the attributes in the
	 * expression. That's why this method searches for the same attribute in the
	 * schema.
	 */
	public static SDFAttribute getAttributeFromSchema(SDFSchema inputSchema, SDFAttribute attributeToSearch) {
		for (SDFAttribute attribute : inputSchema.getAttributes()) {
			if (attribute.getAttributeName().equals(attributeToSearch.getAttributeName())) {
				return attribute;
			}
		}
		return attributeToSearch;
	}
	
	/**
	 * Checks if an expression contains a temporal attribute
	 * 
	 * @param expression
	 *            The expression to check
	 * @return True, if is has a temporal attribute, false otherwise
	 */
	public static boolean expressionHasTemporalAttribute(SDFExpression expression, SDFSchema inputSchema) {
		for (SDFAttribute attribute : expression.getAllAttributes()) {

			SDFAttribute attributeFromSchema = TemporalDatatype.getAttributeFromSchema(inputSchema, attribute);
			if (TemporalDatatype.isTemporalAttribute(attributeFromSchema)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The search needs to be done on the input schema, because the temporal
	 * constraints are added to the input schema, not to the attributes in the
	 * expression. That's why this method searches for the same attribute in the
	 * schema.
	 */
	public static SDFAttribute getAttributeFromSchema(SDFSchema inputSchema, SDFAttribute attributeToSearch) {
		SDFAttribute findAttribute = inputSchema.findAttribute(attributeToSearch.getAttributeName());
		if (findAttribute != null) {
			return findAttribute;
		}
		return attributeToSearch;
	}

	/**
	 * Checks if an expression contains a temporal attribute
	 * 
	 * @param expression
	 *            The expression to check
	 * @return True, if is has a temporal attribute, false otherwise
	 */
	public static boolean expressionHasTemporalAttribute(SDFExpression expression, SDFSchema inputSchema) {
		for (SDFAttribute attribute : expression.getAllAttributes()) {

			SDFAttribute attributeFromSchema = TemporalDatatype.getAttributeFromSchema(inputSchema, attribute);
			if (TemporalDatatype.isTemporalAttribute(attributeFromSchema)) {
				return true;
			}
		}
		return false;
	}

}
