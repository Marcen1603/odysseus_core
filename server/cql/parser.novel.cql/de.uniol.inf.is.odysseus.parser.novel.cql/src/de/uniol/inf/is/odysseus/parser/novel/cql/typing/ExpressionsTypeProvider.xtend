package de.uniol.inf.is.odysseus.parser.novel.cql.typing

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.And
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.BoolConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Comparision
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Equality
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.FloatConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.IntConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Minus
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.MulOrDiv
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.NOT
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Or
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Plus
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StringConstant

class ExpressionsTypeProvider 
{

	public static val stringType = new StringType
	public static val intType	 = new IntType
	public static val boolType	 = new BoolType
	public static val floatType  = new FloatType

	def dispatch ExpressionsType typeFor(Expression e)
	{
		switch e
		{
			StringConstant : stringType
			IntConstant : intType
			FloatConstant : floatType
			NOT, 
			Comparision,
			Equality,
			BoolConstant,
			And,
			Or : boolType
		}
	}
	
	def dispatch ExpressionsType typeFor(Plus e)
	{
		val left = e.left?.typeFor
		val right = e.right?.typeFor
		if ((left == floatType && right != boolType && right!= stringType) 
			||  (right == floatType && left != boolType && left != stringType
			)
		) 
			floatType
		else if (left == intType && right == intType) 
			intType
	}
	
	def dispatch ExpressionsType typeFor(MulOrDiv e)
	{
		val left = e.left?.typeFor
		val right = e.right?.typeFor
		if ((left == floatType && right != boolType && right!= stringType) 
			||  (right == floatType && left != boolType && left != stringType
			)
		) 
			floatType
		else if (left == intType && right == intType) 
			intType
	}
	
	def dispatch ExpressionsType typeFor(Minus e)
	{
		val left = e.left?.typeFor
		val right = e.right?.typeFor
		if ((left == floatType && right != boolType && right!= stringType) 
			||  (right == floatType && left != boolType && left != stringType
			)
		) 
			floatType
		else if (left == intType && right == intType) 
			intType
	}
	
	
	
	def dispatch ExpressionsType typeFor(Attribute e)
	{
		
	}
	
	def dispatch ExpressionsType typeFor(SDFDatatype e)
	{
		
		if(e.string)
			stringType
		else if(e.integer)
			intType

	}
}