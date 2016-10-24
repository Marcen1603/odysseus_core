package de.uniol.inf.is.odysseus.parser.novel.cql.typing

import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Expression
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.StringConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.IntConstant

class ExpresionTypeProvider 
{

	public static val stringType = new StringType
//	public static val intType	 = new IntType
//	public static val boolType	 = n


	def dispatch ExpressionType typeFor(Expression e)
	{
		switch e
		{
			StringConstant : stringType
//			IntConstant : 
		}
	}
	
}
