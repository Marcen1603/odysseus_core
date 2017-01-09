/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.generator

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AccessFramework
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Aggregation
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.And
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeRef
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.BoolConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Bracket
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ChannelFormat
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Comparision
 
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Equality
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ExpressionsModel
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.FloatConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.IntConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Minus
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.MulOrDiv
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.NOT
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Or
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Plus
 
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Statement

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StringConstant
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.Set
import java.util.stream.Collectors
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGenerator2
import org.eclipse.xtext.generator.IGeneratorContext
import java.util.Arrays
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Create
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StreamTo
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Drop

//import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Create

//TODO Documentation
/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class CQLGenerator implements IGenerator2
{

	var Set<SDFAttribute> outerattributes = new HashSet
	var Set<SDFAttribute> innerattributes = new HashSet
	var Set<SDFSchema>    innerschema;
	var Set<SDFSchema>    outerschema;
	var Map<String, String> sinks = new HashMap	
	var Map<String, String> streamto = new HashMap	
	var count_ID = 0
	var predicate = ''

	override afterGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context)  {}
	override beforeGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {}
	
	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) 
	{
		var i = 0 ;
		for(e : resource.allContents.toIterable.filter(typeof(Statement)))
		{
			fsa.generateFile(
				""+ i++,	
				e.parseStatement()
			)
		}
	}
	
	def CharSequence parseStatement(Statement stmt)
	{
		switch stmt.type
		{
			Select :
			{
				return parseSelect1(stmt.type as Select)
			}
			Create : 
			{
			return parseCreate(stmt.type as Create)
			}
			StreamTo		 : return parseStreamtoStatement(stmt.type as StreamTo)
			Drop			 : return parseDrop(stmt.type as Drop)
		}
		
	}
	
	def CharSequence parseDrop(Drop drop) 
	{
		//TODO Implement Drop
		return ''
	}
	
	def CharSequence parseSelect1(Select stmt)
	{
		var s = parseSelect(stmt)
		var result = "operator_"+ getID() +" = "
		if(stmt.distinct != null) 
			return result += "DISTINCT(" + s + ")"
		else
			return result += s
	}
	
	def CharSequence parseSelect(Select stmt)
	{
		predicate = ''
		
		if(null == stmt.predicates)
			return parseSelectWithoutPredicate(stmt)
		else
			return parseSelectWithPredicate(stmt)
	}
	
	private def CharSequence parseSelectWithoutPredicate(Select stmt)
	{

		if(!stmt.attributes.empty)//SELECT attr1, ...
			{
			    var String operator = null
			    var attributes = stmt.attributes.stream.map(e|e.name).collect(Collectors.toList)
				if(!stmt.aggregations.empty)
				{
					var result = buildAggregateOP(stmt.aggregations, stmt.order, stmt.sources)
					attributes = result.get(0) as List<String>
				    operator   = result.get(1).toString
				
					//FIXME Not necessary, it has been done before!
					attributes.addAll(
						stmt.attributes.stream.map(e|e.name).collect(Collectors.toList)
					)
				}												
				
				return '''«buildProjectOP(attributes, buildJoin(null, stmt.sources, operator))»'''
			}
			else//SELECT * ..
			{
				if(stmt.aggregations.empty)
				{
					if(stmt.sources.size == 1)
					{
						return '''«buildProjectOP(stmt.sources.get(0))»'''
					}
					return '''«buildJoin(stmt.sources)»'''
				}
				var operator = buildAggregateOP(stmt.aggregations, stmt.order, stmt.sources)
				return '''«operator.get(1).toString»'''
			}
	}
	
	private def CharSequence parseSelectWithPredicate(Select stmt)
	{
		
	    var String operator = null
	    var attributes = stmt.attributes.stream.map(e|e.name).collect(Collectors.toList)
		if(!stmt.aggregations.empty)
		{
			var result = buildAggregateOP(stmt.aggregations, stmt.order, stmt.sources)
			attributes = result.get(0) as List<String>
		    operator   = result.get(1).toString
		
			//Not necessary, it has been done before!
			attributes.addAll(
				stmt.attributes.stream.map(e|e.name).collect(Collectors.toList)
			)
			
		}
		
		var List<Expression> predicates = newArrayList
		predicates.add(stmt.predicates.elements.get(0))
		if(stmt.having != null)
		{
			predicates.add(stmt.having.elements.get(0))
		}
		if(stmt.sources.size > 1 && !stmt.attributes.empty)// SELECT * FROM src1 / src1, .. src2 WHERE ...;
		{
			return '''«buildSelectOP(predicates, buildProjectOP(attributes, buildJoin(null, stmt.sources, operator)))»'''	
		}// SELECT * FROM src1, src2, ... WHERE ...; | SELECT attr1, ... FROM src1 / src1, ... WHERE ...;
		if(stmt.attributes.empty)
		{
			return '''«buildSelectOP(predicates, buildJoin(null, stmt.sources, operator))»'''
		}
		return '''«buildSelectOP(predicates, buildProjectOP(attributes, buildJoin(null, stmt.sources, operator)))»'''
	}
	
	def Object[] buildAggregateOP(List<Aggregation> list, List<Attribute> list2, List<Source> srcs)
	{
		return buildAggregateOP(list, list2, buildJoin(srcs))
	}
	
	def Object[] buildAggregateOP(List<Aggregation> list, List<Attribute> list2, Source src)
	{
		return buildAggregateOP(list, list2, checkForNestedStatement(src))
	}
	
	def Object[] buildAggregateOP(List<Aggregation> aggAttr, List<Attribute> orderAttr, CharSequence input)
	{
		var argsstr 				 = ''
		var List<String> args    = newArrayList()
		var List<String> aliases = newArrayList()
		for(var i = 0; i < aggAttr.length; i++)
		{
			args.add(aggAttr.get(i).name)
			args.add(aggAttr.get(i).attribute.name)
			var alias = ''
			if(aggAttr.get(i).alias != null)
				alias= aggAttr.get(i).alias.name
			else
				alias = aggAttr.get(i).name + '_' + aggAttr.get(i).attribute.name
			args.add(alias)
			aliases.add(alias)
//			aliases.add(aggAttr.get(i).attribute.name)
			args.add(getDataTypeFrom(aggAttr.get(i).attribute))
			args.add(',')
			argsstr += generateKeyValueString(args)
			if(i != aggAttr.length - 1) argsstr += ','
			args.clear
		}
		//Generates the group by argument that is formed like ['attr1', attr2', ...]
		var groupby = ''
		if(!orderAttr.empty)
		{
			groupby += ',GROUP_BY=['
			groupby += generateListString(
				orderAttr.stream.map(e|e.name).collect(Collectors.toList)
			) + ']'
		}
		return (#[aliases, '''AGGREGATE({AGGREGATIONS=[«argsstr»]«groupby»}, «input»)'''])
	}
	
	def String getDataTypeFrom(Attribute attribute) 
	{
		if(attribute == null) throw new NullPointerException("given attribute was null")
		for(SDFAttribute a : innerattributes)
			if(a.attributeName.equals(attribute.name))
				return a.datatype.toString
		for(SDFAttribute a : outerattributes)
			if(a.attributeName.equals(attribute.name))
				return a.datatype.toString
		throw new IllegalArgumentException("given attribute " + attribute.name + "unknown")		
	}
		
	def CharSequence parseCreate(Create stmt)
	{
		var symbol1 = ' := '
		//var symbol1 = if(isView) ' := ' else ' = '
		var ch = stmt.channelformat
		if(ch!= null)
		{
			if(ch.stream != null)
			{
				return '''«ch.stream.name + symbol1»«buildAccessOP(ch)»'''
			}
			if(ch.view.select != null) 
			{
				var str = parseSelect1(ch.view.select)
					.toString
//					.replaceAll("\\s*[\\r\\n]+\\s*", "")
//					.trim()
//					.replace(" ","")
				return str.replace(("operator_" + count_ID + " = "), (ch.view.name + ":="))
			}
//			var str = parseCreateStatement(ch.view.create).toString
//			return str.replace(str.substring(0, str.indexOf(":")), ch.view.name + " ")
		}
		else
		{
			var af = stmt.accessframework
			if(af.type.equals("STREAM"))
			{
				return '''«af.name + symbol1»«buildAccessOP(af)»'''
			}
			buildSenderOP(af)
			return ''''''
		}
	}
		
	def parseStreamtoStatement(StreamTo stmt)
	{
		if(stmt.statement != null)
		{
			var s = parseSelect1(stmt.statement).toString
			streamto.put(stmt.name, s.subSequence(s.indexOf('=') + 1, s.length).toString)		
		}
		else
		{
			streamto.put(stmt.name, stmt.inputname)
		}
		if(!sinks.keySet.contains(stmt.name)) 
		{ 
			return ''
		}
		return '''output«getID() + " := " + sinks.get(stmt.name).replace("_INPUT_", streamto.get(stmt.name))»'''			
	}
		
	def CharSequence buildAccessOP(ChannelFormat channel)
	{
		var wrapper     = 'GenericPush'
		var protocol    = 'SizeByteBuffer'
		var transport   = 'NonBlockingTcp'
		var dataHandler = 'Tuple'		
		var args 		= generateKeyValueString(
							channel.stream.attributes.map[e|e.name],
							channel.stream.datatypes.map[e|e.value],
							','
						  )
						  
		return '''ACCESS
				  (
					{	  
						source      = '«getKeyword(0) +  channel.stream.name»', 
						wrapper     = '«wrapper»',
						protocol    = '«protocol»',
						transport   = '«transport»',
						dataHandler = '«dataHandler»',
						schema = [«args»],
						options =[['port', '«channel.stream.port»'],
								  ['host', '«channel.stream.host»']]
					 }
				   )'''
	}	
		
	def CharSequence buildAccessOP(AccessFramework access)
	{
		var wrapper     = access.wrapper
		var protocol    = access.protocol
		var transport   = access.transport
		var dataHandler = access.datahandler
		var args 		= generateKeyValueString(
							access.attributes.map[e|e.name],
							access.datatypes.map[e|e.value],
							','
						  )
						  
		var options 	= generateKeyValueString(
							access.keys,
							access.values,
							','
						  )						  
						  
		return '''ACCESS
				  (
					{	  
						source      = '«getKeyword(0) +  access.name»', 
						wrapper     = '«wrapper»',
						protocol    = '«protocol»',
						transport   = '«transport»',
						dataHandler = '«dataHandler»',
						schema = [«args»],
						options =[«options»]
					 }
				   )'''
	}		
	
	def buildSenderOP(AccessFramework access) 
	{
		var wrapper     = access.wrapper
		var protocol    = access.protocol
		var transport   = access.transport
		var dataHandler = access.datahandler
						  
		var options 	= generateKeyValueString(
							access.keys,
							access.values,
							','
						  )						  
						  
		var str = '''SENDER
				  (
					{	  
						sink        = '«getKeyword(0) +  access.name»', 
						wrapper     = '«wrapper»',
						protocol    = '«protocol»',
						transport   = '«transport»',
						dataHandler = '«dataHandler»',
						options =[«options»]
					 }, _INPUT_
				   )'''	

		if(streamto.keySet.contains(access.name))
		{
			return str.replace("_INPUT_", streamto.get(access.name))
		}
		sinks.put(access.name, str);
		return ''
	}

//	/**
//	 * Builds a join operator with a given {@link ExpressionsModel} and list of {@link Source}
//	 * elements. If the predicate is null, there will be no join predicate in the operation.
//	 * If the list contains only one source, an {@link IllegalArgumentException} will be thrown.
//	 */
	def CharSequence buildJoin(ExpressionsModel predicate, List<Source> srcs, CharSequence input2)
	{
		var args = srcs.stream.map(
			e|checkForNestedStatement(e).toString
		).collect(Collectors.toList)
		
		if(input2 != null)
			args.add(input2.toString)
		var join = buildJoin(predicate, args)
		return join
	}

	def CharSequence buildJoin(ExpressionsModel predicate, String[] srcs)
	{
		
		if(srcs.size < 1)
		{ 
			throw new IllegalArgumentException(
				"Invalid number of source elements: There have to be at least two sources"
			)
		}
		if(srcs.size == 1)//Will only be considered if the first call of this method provides a single source
		{
			return srcs.get(0)
		}
		var List<String> list = new ArrayList(Arrays.asList(srcs))
		var src = list.get(0)
		if(list.size == 2)
		{
			return '''JOIN(«list.get(0)»,«list.get(1)»)'''
		}
		list.remove(0)
		if(predicate != null)
		{
			var predicateString = '''{predicate='«buildPredicate(predicate.elements.get(0))»'}'''
			return '''JOIN(«predicateString»,«src»,«buildJoin(null, list)»)'''
		}
		else 
		{ 
			return '''JOIN(«src»,«buildJoin(null, list)»)'''
		} 

	}

	def CharSequence buildJoin(ExpressionsModel predicate, List<Source> scrs)
	{
		return buildJoin(predicate, scrs, null) 	
	}

	def buildJoin(List<Source> scrs)
	{
		
		var args = scrs.stream.map(
			e|checkForNestedStatement(e).toString
		).collect(Collectors.toList)

		return buildJoin(null, args)
	}

	/**
	 * Builds a select operator with a given {@link ExpressionsModel} object as predicate
	 * and a list of {@link Source} elements. If the list contains more then one element,
	 * the {@link CQLGenerator#buildJoin(..)} method will be called to define the input
	 * operator.
	 */
//	def CharSequence buildSelectOP(ExpressionsModel predicate, List<Source> srcs)
//	{
	//	if(srcs.size == 1)
	//		return '''SELECT({predicate='«buildPredicate(predicate.elements.get(0))»'},«srcs.get(0).name»)'''
	//	return '''SELECT({predicate='«buildPredicate(predicate.elements.get(0))»'},«buildJoin(null, srcs)»)'''
//	}

	/**
	 * Builds a select operator with a given {@link ExpressionsModel} object as predicate
	 * and a char sequence to define the input operator. 
	 */
	def CharSequence buildSelectOP(Expression predicate, CharSequence operator)
	{
		predicate = ''
		return '''SELECT({predicate='«buildPredicate(predicate)»'},«operator»)'''
	}
	
	/**
	 * Builds a select operator with a given {@link ExpressionsModel} object as predicate
	 * and a char sequence to define the input operator. 
	 */
	def CharSequence buildSelectOP(List<Expression> predicate, CharSequence operator)
	{
		predicate = ''
		return '''SELECT({predicate='«buildPredicate(predicate)»'},«operator»)'''
	}

//	/**
//	 * Builds a select operator with a given {@link ExpressionsModel} object as predicate
//	 * and a char sequence to define the input operator. 
//	 */
//	def CharSequence buildSelectOP(List<ExpressionsModel> predicate, CharSequence operator)
//	{	
//		var predicates = ''
//		for(var i = 0; i < predicate.size - 1; i++)
//		{
//			predicate = ''
//			predicates += buildPredicate(predicate.get(i).elements.get(0)) + ' && '	
//		}	
//		predicate = ''
//		predicates += buildPredicate(predicate.get(predicate.size - 1).elements.get(0))	
//		return '''SELECT({predicate='«predicates»'},«operator»)'''
//	}

	/**
	 * Builds a project operator with a list of {@link Attribute} and {@link Source}
	 * element. If the list is null, all attributes to the corresponding source will be
	 * added to the attributes parameter.
	 */
	def CharSequence buildProjectOP(List<Attribute> attributes, Source src)
	{
		return buildProjectOP(attributes.stream.map(e|e.name).collect(Collectors.toList), src)
	}
	
	/**
	 * Builds a project operator with a list of {@link Attribute} and char sequence
	 * to define the input operator.
	 */
	def CharSequence buildProjectOP(List<Attribute> attributes, CharSequence operator)
	{
		return '''PROJECT(
				  	{
				  		attributes=[«generateListString(attributes.stream.map(e|e.name).collect(Collectors.toList))»]
				  	},«operator»)'''
	}
	
	def CharSequence buildProjectOP(String[] attributes, CharSequence operator)
	{
		return '''PROJECT(
				  	{
				  		attributes=[«generateListString(attributes)»]
				  	},«operator»)'''
	}
	
	def CharSequence buildProjectOP(String[] attributes, List<Source> sources)
	{
		return '''PROJECT(
				  	{
				  		attributes=[«generateListString(attributes)»]
				  	},«buildJoin(sources)»)'''
	}
	
	def CharSequence buildProjectOP(List<Attribute> attributes, List<Source> srcs)
	{
		return '''PROJECT(
				  	{
				  		attributes=[«generateListString(attributes.stream.map(e|e.name).collect(Collectors.toList))»]
				  	},«buildJoin(srcs)»)'''
	}
	
	def CharSequence buildProjectOP(Source src)
	{
		return buildProjectOP(#[] as String[], src)
	}
	
	def CharSequence buildProjectOP(String[] attributes, Source src)
	{
		if(src == null) { throw new NullPointerException("Given source was null") }
		
		if(attributes.empty)
		{
			return '''PROJECT(
						{
							attributes=[«generateListString(getAttributeNamesFrom(src.name))»]
						},«checkForNestedStatement(src)»)'''
		}
		return '''PROJECT(
				  	{
				  		attributes=[«generateListString(attributes)»]
				  	},«checkForNestedStatement(src)»)'''
	}
	
	//TODO String have to be encupseld between '
	/**Builds a predicate string from a given {@link Expression}. */
	def CharSequence buildPredicate(Expression e)
	{
		if(!e.eContents.empty)
		{
			switch e
			{
				Or:
				{
					buildPredicate(e.left)
					predicate += '||'
					buildPredicate(e.right)
				}
				And:
				{
					buildPredicate(e.left)
					predicate += '&&'
					buildPredicate(e.right)
				}  
				Equality:
				{
					buildPredicate(e.left)
					predicate += e.op
					buildPredicate(e.right)
				}
				Comparision:
				{
					buildPredicate(e.left)
					predicate += e.op
					buildPredicate(e.right)	
				}
				Plus:
				{
					buildPredicate(e.left)
					predicate += '+'
					buildPredicate(e.right)
				}
				Minus:
				{
					buildPredicate(e.left)
					predicate += '-'
					buildPredicate(e.right)					
				}
				MulOrDiv:
				{
					buildPredicate(e.left)
					predicate += e.op
					buildPredicate(e.right)
				}
				NOT:
				{
					predicate += '!'
					buildPredicate(e.expression)
				}
				Bracket:
				{
					predicate += '(' 
					buildPredicate(e.inner)
					predicate += ')' 
				}
				AttributeRef:
				{
					predicate += e.value.name
				}  	 
			}
		} 
		else
		{
			var str = ''
			switch e
			{
				IntConstant: 	str = e.value + ''
				FloatConstant:  str = e.value
				StringConstant: str = "'" + e.value + "'"
				BoolConstant: 	str = e.value
	
			}
			predicate += str
		}
	}
	
	def CharSequence buildPredicate(List<Expression> expressions)
	{
		var s = ''
		for(var i = 0 ; i < expressions.size - 1; i++)
		{
			predicate = ''
			s += buildPredicate(expressions.get(i)) + '&&'
		}
		predicate = ''
		s += buildPredicate(expressions.get(expressions.size - 1))
		return s
	}
	
	def CharSequence checkForNestedStatement(Source src)
	{
		if(src.nested != null)
		{
			return parseSelect(src.nested)
		}
		else
		{
			return buildWindowOP(src)
		}
	}

	def CharSequence buildWindowOP(Source src)
	{
		if(src.time != null)
		{
			var var1 = if(src.time.advance_size != 0) src.time.advance_size else 1
			var var2 = if(src.time.advance_size != 0) src.time.advance_unit else src.time.unit
			return '''TIMEWINDOW
					  (
					  	{
					  		size = [«src.time.size»,'«src.time.unit»'],
					  		advance = [«var1»,'«var2»']
						},
						«src.name»
					 )'''			 
		}
		else if(src.tuple != null)
		{
			var var1 = if(src.tuple.advance_size != 0) src.tuple.advance_size else 1
			if(src.tuple.partition_attribute == null)
			{
				return  '''ELEMENTWINDOW
						   (
						   	{
								size = «src.tuple.size»,
								advance = «var1»
						   	},
						   	«src.name»
						   )'''

			}
			else
			{
				return  '''ELEMENTWINDOW
						   (
						   	{
								size = «src.tuple.size»,
								advance = «var1»,
								partition = '«src.tuple.partition_attribute.name»'
						   	},
						   	«src.name»
						   )'''
			}			
		}
		else 
		{ 
			return src.name
		}
		
	}

	/**
	 * TODO Write documentation! 
	 */
	def String generateKeyValueString(String ... s)
	{
		var str = "["
		if(s.length == 1) 
			return str += "'" + s.get(0) + "']"
		for(var i = 0; i < s.length - 2; i++)
			str += "'" + s.get(i) + "'" + s.get(s.length - 1)			
		return str += "'" + s.get(s.length - 2) + "']"
	}
		
	def String generateKeyValueString(List<String> l1, List<String> l2, String s)
	{
		var str = ''
		for(var i = 0; i < l1.size - 1; i++)// OUT of bounds...
		{
			str += generateKeyValueString(l1.get(i), l2.get(i), s) + ","
		}
		return (str += generateKeyValueString(l1.get(l1.size - 1), l2.get(l1.size - 1), s))
	}
	
	def String generateKeyValueString(String s1, List<String> l2, String s2)
	{
		var str = ''
		for(var i = 0; i < l2.size - 1; i++)
		{
			str += generateKeyValueString(s1, l2.get(i), s2) + ","
		}
		return (str += generateKeyValueString(s1, l2.get(l2.size - 1), s2))
	}
	
	def String generateListString(String s1)
	{
		return "'" + s1 + "'"
	}
		
	def String generateListString(List<String> l1)
	{
		var str = ''
		for(var i = 0; i < l1.size - 1; i++)
		{
			str += generateListString(l1.get(i)) + ","
		}
		return (str += generateListString(l1.get(l1.size - 1)))
	}
		
	def CharSequence buildSchema(List<Attribute> attr, Source src)
	{
		var str = ''
		for(a : innerattributes)
			if(a.sourceName.equals(src.name))
				str += generateKeyValueString(a.attributeName, a.datatype.toString, ',') + ","
		return (str = str.substring(0, str.length - 2))
	}
	
	def CharSequence getID()
	{
		count_ID++
		return count_ID.toString
	}
	
	def void clear()
	{
		outerattributes.clear()
		innerattributes.clear()
		count_ID = 0
		sinks.clear()
		streamto.clear()
	}

	def void setOuterschema(Set<SDFSchema> s)
	{
		outerschema = s
		for(SDFSchema t : s)
		{
			outerattributes.addAll(t.attributes)
		}
	}

	def void setInnerschema(Set<SDFSchema> s)
	{
		innerschema = s
		for(SDFSchema t : s)
		{
			innerattributes.addAll(t.attributes)
		}
	}
	
	val static CharSequence[] keywords = #['input_', 'window_', 'output_', 'select_']
	def static CharSequence getKeyword(int i)
	{
		if(i >= keywords.length || i < 0)
			return 'WRONG_INDEX_NO_KEYWORD'
		return keywords.get(i);
	}
	
	/** Returns all {@link Attribute} elements to the corresponding source. */
	def getAttributeNamesFrom(String src) 
	{
		/*
		 * A source is either from outerattributes» or from innerattributes.
		 * Hence, there should be no duplicated entries while iterating 
		 * through both lists.
		 */
		var List<String> l = new ArrayList 
		for(SDFAttribute a : outerattributes)
			if(a.sourceName.equals(src))
				l.add(a.attributeName)
		for(SDFAttribute a : innerattributes)
			if(a.sourceName.equals(src))
				l.add(a.attributeName)
		return l
	}
	
	
	/* Builds an AggregationOP -> was replaced by buildAggregateOP(..)
	def Object[] buildAggregationOP(List<Aggregation> aggAttr, List<Attribute> orderAttr, CharSequence input) 
	{
		//Produces a string array with strings like 'FUNCTION' = 'COUNT' 
		var functions = generateKeyValueString(
			'FUNCTION',
			aggAttr.stream.map(e|e.name).collect(Collectors.toList),
			'='
		).split(',').stream.map(e|e.replace("[","").replace("]", "")).collect(Collectors.toList)
		//Produces a string array with strings like 'INPUT_ATTRIBUTES' = 'attr1'
		var input_attr = generateKeyValueString(
			'INPUT_ATTRIBUTES',
			aggAttr.stream.map(e|e.attribute.name).collect(Collectors.toList),
			'='
		).split(',').stream.map(e|e.replace("[","").replace("]", "")).collect(Collectors.toList)
		
		//Creates a list with aliases from the given argument and adds missing aliases
		var List<String> aliases = newArrayList()
		for(var i = 0; i < aggAttr.length; i++)
		{
			if(aggAttr.get(i).alias != null)
				aliases.add(aggAttr.get(i).alias.name)
			else
				aliases.add(aggAttr.get(i).name + '_' + aggAttr.get(i).attribute.name)
		}
		//Produces a string array with strings like 'OUTPUT_ATTRIBUTES' = 'COUNT_attr1'
		//or with a corresponding alias for the attribute
		var output_attr = generateKeyValueString(
			'OUTPUT_ATTRIBUTES',
			aliases,
			'='
		).split(',').stream.map(e|e.replace("[","").replace("]", "")).collect(Collectors.toList)
		
		var List<String> lll = newArrayList()
		for(var i = 0; i < functions.size; i++)
		{
			lll.add(functions.get(i) + ',' + input_attr.get(i))
		}
		//Generates the finally argument string that is formed like 
		// ['FUNCTION' = 'COUNT', 'INPUT_ATTRIBUTES' = 'attr1', 'OUTPUT_ATTRIBUTES' = 'COUNT_attr1'] , [...]
		var args = generateKeyValueString(
			lll,
			output_attr,
			','
		).replace("[''", "['")
		.replace("'']", "']")
		.replace("'',''", "','")

		//Generates the group by argument that is formed like ['attr1', attr2', ...]
		var groupby = ''
		if(!orderAttr.empty)
		{
			groupby += ',GROUP_BY=['
			groupby += generateListString(
				orderAttr.stream.map(e|e.name).collect(Collectors.toList)
			) + ']'
		}
		return (#[aliases, '''AGGREGATION({AGGREGATIONS=[«args»]«groupby»}, «input»)'''])
	}
	
	def Object[] buildAggregationOP(List<Aggregation> aggAttr, List<Attribute> orderAttr, Source src) 
	{
		return buildAggregationOP(aggAttr, orderAttr, buildWindowOP(src))
	}

	def Object[] buildAggregationOP(List<Aggregation> aggAttr, List<Attribute> orderAttr, List<Source> src) 
	{
		return buildAggregationOP(aggAttr, orderAttr, buildJoin(null, src))
	}
	 */
}