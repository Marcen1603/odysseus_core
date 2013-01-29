package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticPredicate extends
		AbstractPredicate<ProbabilisticTuple<?>> implements
		IProbabilisticPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3159284040915771680L;
	protected SDFProbabilisticExpression expression;

	protected int[] attributePositions;

	final List<SDFAttribute> neededAttributes;

	protected boolean[] fromRightChannel;

	protected Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

	protected SDFSchema leftSchema;
	protected SDFSchema rightSchema;

	/**
	 * 
	 * @param expression
	 */
	public ProbabilisticPredicate(SDFExpression expression) {
		this(new SDFProbabilisticExpression(expression));
	}

	/**
	 * 
	 * @param expression
	 */
	public ProbabilisticPredicate(SDFProbabilisticExpression expression) {
		this.expression = expression;
		this.neededAttributes = expression.getAllAttributes();
	}

	/**
	 * 
	 * @param predicate
	 */
	public ProbabilisticPredicate(ProbabilisticPredicate predicate) {
		this.attributePositions = predicate.attributePositions == null ? null
				: (int[]) predicate.attributePositions.clone();
		this.fromRightChannel = predicate.fromRightChannel == null ? null
				: (boolean[]) predicate.fromRightChannel.clone();
		this.expression = predicate.expression == null ? null
				: predicate.expression.clone();
		this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(
				predicate.replacementMap);
		this.neededAttributes = new ArrayList<SDFAttribute>(
				predicate.neededAttributes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.probabilistic.base.predicate.IProbabilisticPredicate
	 * #init(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		init(leftSchema, rightSchema, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.predicate.IPredicate#evaluate(java.lang
	 * .Object)
	 */
	@Override
	public boolean evaluate(ProbabilisticTuple<?> input) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(this.attributePositions[i]);
		}
		((SDFProbabilisticExpression) this.expression).bindDistributions(input
				.getDistributions());
		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.predicate.IPredicate#evaluate(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean evaluate(ProbabilisticTuple<?> left,
			ProbabilisticTuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = r.getAttribute(this.attributePositions[i]);
		}
		Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
		additionalContent.putAll(left.getAdditionalContent());
		additionalContent.putAll(right.getAdditionalContent());

		int length = 0;
		if (left.getDistributions() != null) {
			length += left.getDistributions().length;
		}
		if (right.getDistributions() != null) {
			length += right.getDistributions().length;
		}
		NormalDistributionMixture[] distributions = new NormalDistributionMixture[length];
		if (left.getDistributions() != null) {
			System.arraycopy(left.getDistributions(), 0, distributions, 0,
					left.getDistributions().length);
		}
		if (right.getDistributions() != null) {
			System.arraycopy(right.getDistributions(), 0, distributions, length
					- right.getDistributions().length,
					right.getDistributions().length);
		}
		this.expression.bindDistributions(distributions);
		this.expression.bindAdditionalContent(additionalContent);
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate#
	 * getAttributes()
	 */
	@Override
	public List<SDFAttribute> getAttributes() {
		return Collections.unmodifiableList(this.expression.getAllAttributes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ProbabilisticPredicate)) {
			return false;
		}
		return this.expression
				.equals(((ProbabilisticPredicate) other).expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 23 * this.expression.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.probabilistic.base.predicate.IProbabilisticPredicate
	 * #replaceAttribute(de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute)
	 */
	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		replacementMap.put(curAttr, newAttr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate#clone()
	 */
	@Override
	public ProbabilisticPredicate clone() {
		return new ProbabilisticPredicate(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.expression.toString();
	}

	/**
	 * 
	 * @param schema
	 * @param attr
	 * @return
	 */
	private int indexOf(SDFSchema schema, SDFAttribute attr) {
		SDFAttribute cqlAttr = getReplacement(attr);
		Iterator<SDFAttribute> it = schema.iterator();
		for (int i = 0; it.hasNext(); ++i) {
			SDFAttribute a = it.next();
			if (cqlAttr.equalsCQL(a)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	private SDFAttribute getReplacement(SDFAttribute a) {
		SDFAttribute ret = a;
		SDFAttribute tmp = null;
		while ((tmp = replacementMap.get(ret)) != null) {
			ret = tmp;
		}
		return ret;
	}

	/**
	 * 
	 * @param leftSchema
	 * @param rightSchema
	 * @param checkRightSchema
	 */
	private void init(SDFSchema leftSchema, SDFSchema rightSchema,
			boolean checkRightSchema) {
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;

		List<SDFAttribute> neededAttributes = expression.getAllAttributes();
		this.attributePositions = new int[neededAttributes.size()];
		this.fromRightChannel = new boolean[neededAttributes.size()];

		int i = 0;
		for (SDFAttribute curAttribute : neededAttributes) {

			int pos = indexOf(leftSchema, curAttribute);
			if (pos == -1) {
				if (rightSchema == null && checkRightSchema) {
					throw new IllegalArgumentException("Attribute "
							+ curAttribute + " not in " + leftSchema
							+ " and rightSchema is null!");
				}
				if (checkRightSchema) {
					pos = indexOf(rightSchema, curAttribute);
					if (pos == -1) {
						throw new IllegalArgumentException("Attribute "
								+ curAttribute + " not in " + rightSchema);
					}
				}
				this.fromRightChannel[i] = true;
			}
			this.attributePositions[i++] = pos;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
