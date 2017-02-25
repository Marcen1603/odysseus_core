package de.uniol.inf.is.odysseus.nlp.filter.filters;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.FilterExpression;
import de.uniol.inf.is.odysseus.nlp.filter.IFilter;
import de.uniol.inf.is.odysseus.nlp.filter.MalformedExpression;

public class SimpleFilter implements IFilter {
	private FilterExpression expression;
	
	public SimpleFilter(String expression) throws MalformedExpression{
		this.expression = new FilterExpression(expression);
	}
	@Override
	public boolean filter(Annotated annotated) {
		return expression.validate(annotated);
	}
	
}
