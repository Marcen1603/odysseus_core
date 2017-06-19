package de.uniol.inf.is.odysseus.parser.novel.cql.typing

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AndPredicate
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeRef
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.BoolConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Comparision
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Equality
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.FloatConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.IntConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Minus
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.MulOrDiv
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.NOT
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.OrPredicate
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Plus
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StringConstant
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
				var name = a.attributeName
				if (aliases.contains(name)) { // TODO does not detect an alias
					println(name + ' is an alias')
					println('real name is -> ' + name)
				}
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
