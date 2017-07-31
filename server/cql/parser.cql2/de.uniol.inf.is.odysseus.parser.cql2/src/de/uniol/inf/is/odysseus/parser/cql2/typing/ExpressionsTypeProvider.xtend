package de.uniol.inf.is.odysseus.parser.cql2.typing

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AndPredicate
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AttributeRef
import de.uniol.inf.is.odysseus.parser.cql2.cQL.BoolConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Comparision
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Equality
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Minus
import de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NOT
import de.uniol.inf.is.odysseus.parser.cql2.cQL.OrPredicate
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Plus
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant
import java.util.Collection
import java.util.List

class ExpressionsTypeProvider {

	public static val stringType = new StringType
	public static val intType = new IntType
	public static val boolType = new BoolType
	public static val floatType = new FloatType

	public static var List<String> aliases = newArrayList
	public static var Collection<SDFSchema> outputschemas;

	def dispatch ExpressionsType typeFor(Expression e) {
		switch e {
			StringConstant: stringType
			IntConstant: intType
			FloatConstant: floatType
			NOT,
			Comparision,
			Equality,
			BoolConstant,
			AndPredicate,
			OrPredicate: boolType
		}
	}

	def dispatch ExpressionsType typeFor(Plus e) {
		val left = e.left?.typeFor
		val right = e.right?.typeFor
		if ((left == floatType && right != boolType && right != stringType) ||
			(right == floatType && left != boolType && left != stringType
			))
			floatType
		else if (left == intType && right == intType)
			intType
	}

	def dispatch ExpressionsType typeFor(MulOrDiv e) {
		val left = e.left?.typeFor
		val right = e.right?.typeFor
		if ((left == floatType && right != boolType && right != stringType) ||
			(right == floatType && left != boolType && left != stringType
			))
			floatType
		else if (left == intType && right == intType)
			intType
	}

	def dispatch ExpressionsType typeFor(Minus e) {
		val left = e.left?.typeFor
		val right = e.right?.typeFor
		if ((left == floatType && right != boolType && right != stringType) ||
			(right == floatType && left != boolType && left != stringType
			))
			floatType
		else if (left == intType && right == intType)
			intType
	}

	def dispatch ExpressionsType typeFor(AttributeRef e) {
		if(outputschemas === null) return null
		for (SDFSchema s : outputschemas)
			for (SDFAttribute a : s.attributes) {
//				println("alises= " + aliases.toString)
				var name = a.attributeName
//				println("name= " + name)
//				if (aliases.contains(name)) { // TODO does not detect an alias
//					println(name + ' is an alias')
//					println('real name is -> ' + name)
//				}
				if (name.equals((e.value as Attribute).name))
					return typeFor(a.datatype)
			}
		return null
	}

	def dispatch ExpressionsType typeFor(SDFDatatype e) {
		switch e.URI.toUpperCase {
			case "INTEGER": intType
			case "STRING": stringType
			case "BOOLEAN": boolType
			case "DOUBLE": floatType
			default: stringType
		}
	}
	
	static def setOutputSchema(Collection<SDFSchema> schema) { 
		outputschemas = schema
	}
	
}
