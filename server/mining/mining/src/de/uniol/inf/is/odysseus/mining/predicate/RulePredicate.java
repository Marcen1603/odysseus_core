package de.uniol.inf.is.odysseus.mining.predicate;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mining.frequentitem.AssociationRule;
import de.uniol.inf.is.odysseus.relational.base.predicate.ForPredicate.Type;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class RulePredicate extends AbstractPredicate<Tuple<?>> implements IRelationalPredicate<Tuple<?>> {


	private static final long serialVersionUID = -1526635263167088936L;

	private SDFExpression expression;
	private SDFAttribute listAttribute;
	private int index;
	private RelationalPredicate relationalPredicate;
	private SDFSchema innerSchema;
	private Type type = Type.ALL;
	private String predicate;

	public RulePredicate(Type type, SDFAttribute listAttribute, String predicate) {
		this.type = type;
		this.predicate = predicate;
		this.listAttribute = listAttribute;
	}

	/**
	 * @param forPredicate
	 */
	public RulePredicate(RulePredicate rp) {
		this.expression = rp.expression;
		this.listAttribute = rp.listAttribute;
		this.index = rp.index;
		this.relationalPredicate = rp.relationalPredicate;
		this.innerSchema = rp.innerSchema;
		this.type = rp.type;
		this.predicate = rp.predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.predicate.IPredicate#evaluate(java.lang
	 * .Object)
	 */
	@Override
	public boolean evaluate(Tuple<?> input) {
		AssociationRule<ITimeInterval> rule = input.getAttribute(index);
		List<Tuple<ITimeInterval>> premiseTuples = rule.getPremise().getPattern();
		List<Tuple<ITimeInterval>> consequenceTuples = rule.getConsequence().getPattern();
		if (type == Type.ALL) {
			// if all, both must be true
			return (evaluateAll(premiseTuples) && evaluateAll(consequenceTuples));
		}else{
			// type == ANY --> one of it must only true
			return (evaluateAny(premiseTuples) || evaluateAny(consequenceTuples));
		}		
	}
	
	
	private boolean evaluateAll(List<Tuple<ITimeInterval>> tuples){
		for (Tuple<?> tuple : tuples) {
			boolean res = this.relationalPredicate.evaluate(tuple);
			if (!res) {
				// for all : if one is false, all are false
				return false;
			}
		}
		// all true -> result is true
		return true;
	}
	
	private boolean evaluateAny(List<Tuple<ITimeInterval>> tuples){
		for (Tuple<?> tuple : tuples) {
			boolean res = this.relationalPredicate.evaluate(tuple);
			if (!res) {
				// for any : if one is true, all are true
				return true;
			}
		}
		// all false -> result is false
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.predicate.IPredicate#evaluate(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		return evaluate(left);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate
	 * #init(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		this.index = leftSchema.indexOf(listAttribute);
		this.innerSchema = listAttribute.getSubSchema();
		
		DirectAttributeResolver resolver = new DirectAttributeResolver(innerSchema);
		SDFExpression expression = new SDFExpression("", predicate, resolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		
		
		this.relationalPredicate = new RelationalPredicate(expression);
		this.relationalPredicate.init(innerSchema, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate
	 * #replaceAttribute(de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute)
	 */
	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate#clone()
	 */
	@Override
	public RulePredicate clone() {
		return new RulePredicate(this);
	}

}
