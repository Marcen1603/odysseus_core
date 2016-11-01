package de.uniol.inf.is.odysseus.parser.novel.cql.typing

import de.uniol.inf.is.odysseus.parser.novel.cql.cql.And
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Attribute
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.BoolConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Comparision
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Equality
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Expression
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.ExpressionsModel
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.FloatConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.IntConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Minus
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.MulOrDiv
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.NOT
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Or
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Plus
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.StringConstant

import static org.eclipse.emf.ecore.util.EcoreUtil.*

import static extension org.eclipse.xtext.EcoreUtil2.*
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.impl.AttributeImpl

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
//		val elements = attributesDefinedBefore(e)
//		println(elements)
//		for(a : elements)
//		{
//			if(a.name == e.name)
//			{
//				
//			}
//		}
		boolType
	}
	
	def static attributesDefinedBefore(Expression e)
	{
		val allElements = e.getContainerOfType(typeof(ExpressionsModel)).elements
		
		println("size: "+ allElements.size + " , list:" + allElements) //TODO Remove after debugging
		         
		val contained = allElements.findFirst[isAncestor(it, e)]
		println("contained: " + contained)		         
		
		val sublist = allElements.subList(0, allElements.indexOf(contained))
		
		println("sub list: " + sublist)
	}
	
//	def dispatch ExpressionsType typeFor(Minus e)
//	{
//		if (e.left?.typeFor == stringType ||  e.right?.typeFor == stringType) 
//			stringType
//		else
//			intType
//	}
	
	
}


