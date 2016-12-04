/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.generator

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.And
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeRef
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.BoolConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Bracket
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Comparision
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Create_Statement
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Equality
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.FloatConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.IntConstant
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Minus
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.MulOrDiv
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.NOT
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Or
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Plus
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select_Statement
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Statement
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StringConstant
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.Set
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class CQLGenerator extends AbstractGenerator 
{

//	@Inject extension IQualifiedNameProvider

	var Set<SDFAttribute> attributes = new HashSet
	var Map<String, SDFAttribute> map

	def void setSchema(Set<SDFSchema> s)
	{
		for(SDFSchema t : s)
		{
//			print(t)//TODO Remove after debugging
			attributes.addAll(t.attributes)
		}
	}

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) 
	{
		var i = 0 ;
		for(e : resource.allContents.toIterable.filter(typeof(Statement)))
		{
			fsa.generateFile(
//				e.fullyQualifiedName.toString() + '.pql',
//				 i + " " + resource.URI.lastSegment.replace('.cql', '.pql'),
				""+ i++,	
				e.parseStatement()
			)
		}
	}
	
	def CharSequence parseStatement(Statement stmt)
	'''
	«switch stmt.type 
	{
	Select_Statement : parseSelect(stmt.type as Select_Statement)
	Create_Statement : parseCreate(stmt.type as Create_Statement)
	}
	»
	'''
	
	def parseCreate(Create_Statement stmt)
	{
		if(stmt.channel != null)
		{
			var args = ''
			var size = stmt.channel.attributes.size
			for(var i = 0; i < size -1; i++)//TODO Make a function of the schema creation!
			{
				args += "['" + stmt.channel.attributes.get(i).name + "','" + stmt.channel.datatypes.get(i) + "'],\n"
			}
			args += "['" + stmt.channel.attributes.get(size - 1).name + "','" + stmt.channel.datatypes.get(size - 1) + "']"
			'''
			«getKeyword(0) + stmt.channel.name» := ACCESS({source = '«stmt.channel.name»', 
			wrapper = 'GenericPush',
			schema = [«args»],
			transport = 'NonBlockingTcp',
			protocol = 'SizeByteBuffer',
			dataHandler ='Tuple',
			options =[['port', '«stmt.channel.port»'],['host', '«stmt.channel.host»']]})
			'''
		}
		else
		{//TODO Something wrong here ...
		 	var type = ''
			switch stmt.accessframework.type
			{
				case "VIEW",
				case "STREAM":
					type = "ACCESS"
				case "SINK":
					type = "SENDER"	
			}
			var args = ''
			var bool = type.equals('ACCESS')
			if(bool)
			{
				var size = stmt.accessframework.attributes.size
				for(var i = 0; i < size -1; i++)
				{
					args += "['" + stmt.accessframework.attributes.get(i).name + "','" + stmt.accessframework.datatypes.get(i) + "'],\n"
				}
				args += "['" + stmt.accessframework.attributes.get(size - 1).name + "','" + stmt.accessframework.datatypes.get(size - 1) + "']"
			}
			var options = ''
			var size = stmt.accessframework.keys.size
			for(var i = 0; i < size - 1; i++)
			{
				options += "['" + stmt.accessframework.keys.get(i) + "','" + stmt.accessframework.values.get(i) + "'],"	
			}
			options += "['" + stmt.accessframework.keys.get(size - 1) + "','" + stmt.accessframework.values.get(size - 1) + "']"
			'''
			«getKeyword(2) + stmt.accessframework.name» := «type» (
			{«IF bool»source ='«stmt.accessframework.name»',«ENDIF»
			«IF !bool»sink='«stmt.accessframework.name»'«ENDIF»
			wrapper='«stmt.accessframework.wrapper»',
			protocol='«stmt.accessframework.protocol»',
			transport='«stmt.accessframework.transport»',
			dataHandler='«stmt.accessframework.datahandler»',
			«IF bool»schema=[«args»],«ENDIF»
			options=[«options»]})
			'''	
		}
	}

	val static CharSequence[] keywords = #['input_', 'window_', 'output_', 'select_']

	def static CharSequence getKeyword(int i)
	{
		if(i >= keywords.length || i < 0)
			return 'WRONG_INDEX_NO_KEYWORD'
		return keywords.get(i);
	}

	def CharSequence parseSelect(Select_Statement stmt)
	{
		predicate = ''
		if(stmt.predicates == null)//Without a WHERE clause
		{
			'''
			«FOR src : stmt.sources»
				«getKeyword(0) + src.name» := «buildAccessOP(stmt.attributes, src)»
			«ENDFOR»
			'''
		}
		else
		{ 
			var srcs =''
			for(var i = 0; i < stmt.sources.size - 1; i++)
			{
				srcs += stmt.sources.get(i).name + ','
			}
			srcs += stmt.sources.get(stmt.sources.size - 1).name
			var srcs_names = srcs.replace(',', '')
			'''
			«FOR src : stmt.sources»
				«getKeyword(0) + src.name» := «buildAccessOP(stmt.attributes, src)»
			«ENDFOR»
			«getKeyword(3) + srcs_names» := SELECT({predicate='
			«buildPredicate(stmt.predicates.elements.get(0))»'},
			«srcs»)
			'''					
		}
	}

	var predicate = ''
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
	
	def CharSequence buildAccessOP(List<Attribute> attr, Source src)
	{
		'''
		ACCESS
		(
			{	
				source      = '«src.name»',
				wrapper     = 'GenericPush',
				transport   = 'TCPClient',
				dataHandler = 'Tuple',
				schema = [«buildSchema(attr, src)»]
			}
		)
		
		«buildWindowOP(src, src.name)»
		'''
	}

	def CharSequence buildWindowOP(Source w, CharSequence name)
	{
	
		if(w.time != null)
		{
			'''
			«getKeyword(1)»«name» := TIMEWINDOW
			({size = [«w.time.size», '«w.time.unit»']},
			«getKeyword(0)»«name»
			)
			'''
		}
		else if(w.tuple != null)
		{
			var var1 = if(w.tuple.advance_size != 0) w.tuple.advance_size else 1
			if(w.tuple.partition_attribute == null)
			{
				'''
				«getKeyword(1)»«name» := ELEMENTWINDOW
				(
					{size = «w.tuple.size»,
					advance = «var1»
					},
				«getKeyword(0)»«name»
				)
				'''
			}
			else
			{
				'''
				«getKeyword(1)»«name» := ELEMENTWINDOW
				(
					{size = «w.tuple.size»,
					advance = «var1»,
					partition = '«w.tuple.partition_attribute.name»'
					},
				«getKeyword(0)»«name»
				)
				'''
			}			
		}
		else if(w.unbounded != null)
		{
			''''''
		}
			
	}
		
	//TODO Refactore	
	def CharSequence buildSchema(List<Attribute> attr, Source src)
	{
		var str = '\n'
		var SDFDatatype type
		var String alias
		if(attr.size == 0)
		{
			for(a : attributes)
			{
				if(a.sourceName.equals(src.name))
				{
					alias = a.attributeName
					type = a.datatype
					str += "['" + alias + "', '" + type.toString + "'],\n"
				}
			}
			str = str.substring(0, str.length - 2)
		}
		else
		{
			for(a : attributes)
			{
				if(a.sourceName.equals(src.name))
				{
					type = a.datatype
					str += "['" + a.attributeName + "', '" + type.toString + "'],\n"
				}
			}
			str = str.substring(0, str.length - 2)
		}
		str
	}	
}