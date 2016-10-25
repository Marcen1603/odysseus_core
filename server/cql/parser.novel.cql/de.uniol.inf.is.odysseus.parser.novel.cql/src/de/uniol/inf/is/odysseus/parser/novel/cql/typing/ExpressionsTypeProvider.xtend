package de.uniol.inf.is.odysseus.parser.novel.cql.typing

import de.uniol.inf.is.odysseus.parser.novel.cql.cql.And
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.BoolConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Comparision
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Equality
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Expression
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.IntConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Minus
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.MulOrDiv
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.NOT
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Or
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Plus
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.StringConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Attribute
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Model

import static extension org.eclipse.emf.ecore.util.EcoreUtil.*
import static extension org.eclipse.xtext.EcoreUtil2.*

class ExpressionsTypeProvider 
{

	public static val stringType = new StringType
	public static val intType	 = new IntType
	public static val boolType	 = new BoolType

	def dispatch ExpressionsType typeFor(Expression e)
	{
		switch e
		{
			StringConstant : stringType
			Minus,
			MulOrDiv,
			Plus,
			IntConstant : intType 
			NOT, 
			Comparision,
			Equality,
			BoolConstant,
			And,
			Or : boolType
			Attribute:
			{
								
			}
		}
	}
	
	def dispatch ExpressionsType typeFor(Plus e)
	{
		val left = e.left?.typeFor
		val right = e.right?.typeFor
		if (left== stringType ||  right == stringType) 
			stringType
		else
			intType
	}
	
	def dispatch ExpressionsType typeFor(Attribute e)
	{
		val elements = attributesDefinedBefore(e)
		for(a : elements)
		{
			if(a.name == e.name)
			{
				
			}
		}
		
		boolType
	}
	
	def static attributesDefinedBefore(Expression e)
	{
		val allElements = e.getContainerOfType(typeof(Model)).statements
		val contained = allElements.findFirst[isAncestor(it, e)]
		allElements.subList(0, allElements.indexOf(contained)).typeSelect(typeof(Attribute))
	}
	
//	def dispatch ExpressionsType typeFor(Minus e)
//	{
//		if (e.left?.typeFor == stringType ||  e.right?.typeFor == stringType) 
//			stringType
//		else
//			intType
//	}
	
	
}


