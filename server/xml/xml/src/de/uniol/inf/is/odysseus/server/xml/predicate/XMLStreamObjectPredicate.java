package de.uniol.inf.is.odysseus.server.xml.predicate;

import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

public class XMLStreamObjectPredicate extends AbstractPredicate<XMLStreamObject<?>>
{

	private static final long serialVersionUID = -726725755622885866L;
	final SDFExpression expression;
	final List<SDFAttribute> neededAttributes;

	/**
	 *
	 * Class constructor.
	 *
	 * @param expression
	 */
	public XMLStreamObjectPredicate(SDFExpression expression)
	{
		this.expression = expression;
		this.neededAttributes = expression.getAllAttributes();
	}

	/**
	 *
	 * Class constructor.
	 *
	 * @param other
	 */
	public XMLStreamObjectPredicate(XMLStreamObjectPredicate other)
	{
		this.expression = other.expression == null ? null : other.expression.clone();
		this.neededAttributes = this.expression.getAllAttributes();
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public Boolean evaluate(XMLStreamObject<?> input)
	{
		Object[] values = new Object[this.neededAttributes.size()];
		for (int i = 0; i < values.length; ++i)
		{
			values[i] = input.getAttribute(this.neededAttributes.get(i).getURI());
		}
		// this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return ((Boolean) this.expression.getValue());
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public Boolean evaluate(XMLStreamObject<?> left, XMLStreamObject<?> right)
	{
		Object[] values = new Object[this.neededAttributes.size()];
		for (int i = 0; i < values.length; ++i)
		{
			values[i] = left.getAttribute(this.neededAttributes.get(i).getURI());
			if (values[i] == null)
			{
				values[i] = right.getAttribute(this.neededAttributes.get(i).getURI());
			}
		}
		this.expression.bindVariables(values);
		return ((Boolean) this.expression.getValue());
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public AbstractPredicate<XMLStreamObject<?>> clone()
	{
		return new XMLStreamObjectPredicate(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<XMLStreamObject<?>> and(IPredicate<XMLStreamObject<?>> predicate)
	{
		if (predicate instanceof XMLStreamObjectPredicate)
		{
			SDFExpression expr = ((XMLStreamObjectPredicate) predicate).expression;
			AndOperator and = new AndOperator();
			and.setArguments(new IExpression<?>[]{this.expression.getMEPExpression(), expr.getMEPExpression()});
			IAttributeResolver resolver = new DirectAttributeResolver(expression.getAttributeResolver().getSchema(), expr.getAttributeResolver().getSchema());
			return new XMLStreamObjectPredicate(new SDFExpression(and.toString(), resolver, this.expression.getExpressionParser()));
		}
		return super.and(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<XMLStreamObject<?>> or(IPredicate<XMLStreamObject<?>> predicate)
	{
		if (predicate instanceof XMLStreamObjectPredicate)
		{
			SDFExpression expr = ((XMLStreamObjectPredicate) predicate).expression;
			OrOperator or = new OrOperator();
			or.setArguments(new IExpression<?>[]{this.expression.getMEPExpression(), expr.getMEPExpression()});
			IAttributeResolver resolver = new DirectAttributeResolver(expression.getAttributeResolver().getSchema(), expr.getAttributeResolver().getSchema());
			return new XMLStreamObjectPredicate(new SDFExpression(or.toString(), resolver, this.expression.getExpressionParser()));
		}
		return super.or(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<XMLStreamObject<?>> not()
	{
		NotOperator not = new NotOperator();
		not.setArguments(new IExpression<?>[]{this.expression.getMEPExpression()});
		return new XMLStreamObjectPredicate(new SDFExpression(not.toString(), expression.getAttributeResolver(), this.expression.getExpressionParser()));
	}

	@SuppressWarnings("boxing")
	public static void main(String[] args)
	{
		/*String predicate = "a*10-100 > b";
		SDFExpression expression = new SDFExpression(predicate, null, MEP.getInstance());
		XMLStreamObjectPredicate pre = new XMLStreamObjectPredicate(expression);
		XMLStreamObject<IMetaAttribute> input = XMLStreamObject<IMetaAttribute>.createInstance();
		input.setAttribute("a", 10);
		input.setAttribute("b", 8);
		System.out.println(pre.evaluate(input));*/
	}
}
