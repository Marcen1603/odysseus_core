package de.uniol.inf.is.odysseus.nlp.filter.filters;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.FilterExpression;
import de.uniol.inf.is.odysseus.nlp.filter.IFilter;
import de.uniol.inf.is.odysseus.nlp.filter.MalformedExpression;


/**
 * SimpleFilter allows simple expressions to filter NLP-processed streams.
 */
public class SimpleFilter implements IFilter {
	private FilterExpression expression;
	
	/**
	 * Creates new SimpleFilter
	 * @param expression the filter-expression (eg. ["lemma"="be"], so all texts containin is,were,... are filtered)
	 * @throws MalformedExpression 
	 */
	public SimpleFilter(String expression) throws MalformedExpression{
		this.expression = new FilterExpression(expression);
	}
	
	
	@Override
	public boolean filter(Annotated annotated) {
		return expression.validate(annotated);
	}
	
}
