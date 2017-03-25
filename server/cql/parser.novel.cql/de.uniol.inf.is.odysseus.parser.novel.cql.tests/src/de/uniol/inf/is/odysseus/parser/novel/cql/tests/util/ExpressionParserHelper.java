package de.uniol.inf.is.odysseus.parser.novel.cql.tests.util;

import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionParser;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class ExpressionParserHelper implements IExpressionParser
{

	@Override
	public IExpression<?> parse(String expressionStr, List<SDFSchema> schema) throws ParseException {
		return null;
	}

	@Override
	public IExpression<?> parse(String expressionStr, SDFSchema schema) throws ParseException {
		return null;
	}

	@Override
	public IExpression<?> parse(String expressionStr) throws ParseException 
	{
		return null;//expressionStr.contains('DolToEur') ? ;
	}

}
