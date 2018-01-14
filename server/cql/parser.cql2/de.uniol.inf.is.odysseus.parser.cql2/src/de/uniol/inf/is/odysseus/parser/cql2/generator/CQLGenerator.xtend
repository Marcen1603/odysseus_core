/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.generator

import com.google.inject.Guice
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO
import de.uniol.inf.is.odysseus.parser.cql2.CQLRuntimeModule
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AccessFramework
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Create
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateAccessFramework
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateChannelFormatViaFile
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateChannelFrameworkViaPort
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseSink
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateView
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Query
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SchemaDefinition
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StreamTo
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLBuilderModule
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.CacheModule
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAggregationParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeNameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IExistenceParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IJoinParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IPredicateParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IRenameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ISelectParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ParserModule
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.UtilityModule
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGenerator2
import org.eclipse.xtext.generator.IGeneratorContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeParser
import com.google.inject.Injector
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IQuantificationParser

/** Generates PQL text from a CQL text. */
class CQLGenerator implements IGenerator2 {

	val private Logger log = LoggerFactory.getLogger(CQLGenerator);

	public var static Injector injector;

	var Map<String, String> databaseConnections = newHashMap

	var IPredicateParser predicateParser;	
	var IUtilityService utilityService;
	var ICacheService cacheService;
	var IAttributeNameParser nameParser;
	var IAttributeParser attributeParser;
	var IRenameParser renameParser;
	var IJoinParser joinParser;
	var ISelectParser selectParser;
	var IExistenceParser existenceParser;
	var IAggregationParser aggregationParser;
	var IQuantificationParser quantificationParser;
	var AbstractPQLOperatorBuilder builder;
	
	new () {
		
		// create injector for dependency management
		injector = Guice.createInjector(
			new CQLRuntimeModule(),
			new UtilityModule(), 
			new CacheModule(),
			new PQLBuilderModule(),
			new ParserModule()
		);
		
		// get dependencies
		utilityService = injector.getInstance(IUtilityService);
		cacheService = injector.getInstance(ICacheService);
		predicateParser = injector.getInstance(IPredicateParser);
		nameParser = injector.getInstance(IAttributeNameParser);
		attributeParser = injector.getInstance(IAttributeParser);
		renameParser = injector.getInstance(IRenameParser);
		joinParser = injector.getInstance(IJoinParser);
		selectParser = injector.getInstance(ISelectParser);
		existenceParser = injector.getInstance(IExistenceParser);
		aggregationParser = injector.getInstance(IAggregationParser)
		quantificationParser = injector.getInstance(IQuantificationParser);
		builder = injector.getInstance(AbstractPQLOperatorBuilder);
		
	}

	def void clear() {
		predicateParser.clear()
		utilityService.clear()
		joinParser.clear()
		renameParser.clear()
		selectParser.clear()
		attributeParser.clear()
		
//		existenceParser.clear()//TODO add clear()-method
		cacheService.getOperatorCache().flush()
		cacheService.getSystemSources().clear()
		cacheService.getSelectCache().flush()
		cacheService.getQueryCache().clear()
//		cacheService.getExpressionCache().clear()
		
		SystemSource.clearQuerySources()
		SystemSource.clearAttributeAliases()
		
		//TODO clear caches! really?
		
//		registry_OperatorNames.clear()
//		registry_Operators.clear()
//		registry_NestedSelects.clear()
		
//		registry_RenamedAttributes.clear()
//		queryAggregations.clear()
//		queryAttributes.clear()
//		registry_SubQueries.clear()
//		registry_SimpleSelect.clear()
//TODO add clear 
//		registry_existenceOperators.clear()

//		firstJoinInQuery = true
//		querySources = newArrayList
	}

	override afterGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) { }

	override beforeGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) { }

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		fsa.generateFile("" + 1, resource.allContents.toIterable.filter(typeof(Query)).get(0).parseStatement())
	}

	/**	
	 * Parses a {@link Query} object that either represents a {@link ComplexSelect}, {@link Create} or {@link StreamTo}. 
	 * It returns an operator plan that consists of PQL-operators.
	 */
	def CharSequence parseStatement(Query query) {
		
		log.debug("parsing CQL query: selecting query type")
		
		// check if query is a select statmente
		if (query.type instanceof ComplexSelect) {
			
 			var select = (query.type as ComplexSelect)
 			// check if it is a complex statement
			if (select.operation !== null) {
				
				selectParser.parseComplex(select.left, select.right, select.operation)
			// otherwise, it is a simple select
			} else {
				
				selectParser.parse(select.left)
			}
		// check if query is a create statement
		} else if (query.type instanceof Create) {
			parseCreate(query.type as Create)
		// check if query is a stream to statement
		} else if (query.type instanceof StreamTo) {
			parseStreamTo(query.type as StreamTo)
		}
			
		return cacheService.getOperatorCache().getPQL()
	}

	def parseCreate(Create statement) {
		
		if (statement.create instanceof CreateView)
			parseCreateView(statement.create as CreateView)
		else if (statement.create instanceof CreateAccessFramework)
			parseCreateAccessFramework(statement.create as CreateAccessFramework, statement.type)
		else if (statement.create instanceof CreateChannelFormatViaFile)
			parseCreateStreamFile(statement.create as CreateChannelFormatViaFile)
		else if (statement.create instanceof CreateChannelFrameworkViaPort)
			parseCreateStreamChannel(statement.create as CreateChannelFrameworkViaPort)
		else if (statement.create instanceof CreateDatabaseStream)
			parseCreateDatabaseStream(statement.create as CreateDatabaseStream)
		else if (statement.create instanceof CreateDatabaseSink)
			parseCreateDatabaseSink(statement.create as CreateDatabaseSink)
	}

	def private CharSequence parseCreateView(CreateView view) {
		var select = view.select.select as SimpleSelect
		selectParser.parse(select)
		var String viewName = view.getName()
		cacheService.getOperatorCache().addSink(viewName)
		return viewName
	}

	private val SINK_INPUT_KEYWORD = '--INPUT--'
	val String VIEW = "VIEW_KEY_";

	def private CharSequence parseCreateAccessFramework(CreateAccessFramework create, String type) {
		var String operator
		switch (type.toUpperCase) {
			case 'STREAM': operator = 'ACCESS'
			case 'SINK': operator = 'SENDER'
		}
		operator = buildCreate1(operator, create.pars, create.attributes, create.attributes.name).toString
		if (type.toUpperCase.equals('SINK'))
			if (!operator.contains(SINK_INPUT_KEYWORD))
//				return cacheService.getOperatorCache().registerOperator(operator, VIEW + create.attributes.name)
				return cacheService.getOperatorCache().add(VIEW + create.attributes.name, operator)
			else
				cacheService.getOperatorCache().getSinks().put(VIEW + create.attributes.name, operator)
		else
//			cacheService.getOperatorCache().registerOperator(operator, VIEW + create.attributes.name)
			cacheService.getOperatorCache().add(VIEW + create.attributes.name, operator)
		return ''
	}

	def private parseCreateDatabaseSink(CreateDatabaseSink sink) {
		var Map<String, String> args = newHashMap
		args.put('connection', sink.database)
		args.put('table', sink.table)
		var type = ''
		if (databaseConnections.keySet.contains(sink.database))
			type = databaseConnections.get(sink.database)
		else
			throw new IllegalArgumentException("Database connection " + sink.database + " could not be found");
		args.put('type', type)
		args.put('input', SINK_INPUT_KEYWORD)
		if (sink.option !== null)
			if (sink.option.toUpperCase().equals("DROP"))
				args.put('drop', 'true')
			else
				args.put('truncate', 'true')
	// TODO not working 
//		var operator = builder.build(typeof(DatabasesinkAO), args)
//		registry_Sinks.put(sink.name, operator)
	}

	def private CharSequence extractSchema(SchemaDefinition schema) {
		var attributenames = newArrayList
		var datatypes = newArrayList
		for (var i = 0; i < schema.arguments.size - 1; i = i + 2) {
			attributenames.add(schema.arguments.get(i))
			datatypes.add(schema.arguments.get(i + 1))
		}
		return utilityService.generateKeyValueString(attributenames, datatypes, ',')
	}

	def private int getTimeInMilliseconds(String time, int value) {
		switch (time.toUpperCase) {
			case 'MILLISECONDS',
			case 'MILLISECOND': return value
			case 'SECONDS',
			case 'SECOND': return value * 1000
			case 'MINUTES',
			case 'MINUTE': return value * (60 * 1000)
			case 'HOURS',
			case 'HOUR': return value * (60 * 60 * 1000)
			case 'DAYS',
			case 'DAY': return value * (24 * 60 * 60 * 1000)
			case 'WEEKS',
			case 'WEEK': return value * (7 * 24 * 60 * 60 * 1000)
			default: return 0
		}
	}

	def parseCreateDatabaseStream(CreateDatabaseStream stream) {
		var Map<String, String> args = newHashMap
		args.put('connection', stream.database)
		args.put('table', stream.table)
		args.put('attributes', extractSchema(stream.attributes).toString)
		var operator = ''
		var waitMillis = getTimeInMilliseconds(stream.unit.getName, stream.size).toString
		if (!waitMillis.equals('0.0'))
			args.put('waiteach', waitMillis)
		// TODO not working
//		operator = builder.build(typeof(DatabasesourceAO), args)
		return cacheService.getOperatorCache().add(VIEW + stream.attributes.name, operator)
	}

	def private CharSequence parseCreateStreamFile(CreateChannelFormatViaFile file) {
		var Map<String, String> args = newHashMap
		args.put('source', file.attributes.name)
		args.put('wrapper', 'GenericPull')
		args.put('protocol', file.type)
		args.put('transport', 'File')
		args.put('datahandler', 'Tuple')
		args.put('schema', extractSchema(file.attributes).toString)
		args.put('options', '''['filename','«file.filename»'],['delimiter',';'],['textDelimiter',"'"]''')
		var operator = builder.build(typeof(AccessAO), args)
		return cacheService.getOperatorCache().add(VIEW + file.attributes.name, operator)
	}

	def private CharSequence parseCreateStreamChannel(CreateChannelFrameworkViaPort channel) {
		var Map<String, String> args = newHashMap
		args.put('source', channel.attributes.name)
		args.put('wrapper', 'GenericPush')
		args.put('protocol', 'SizeByteBuffer')
		args.put('transport', 'NonBlockingTcp')
		args.put('datahandler', 'Tuple')
		args.put('schema', extractSchema(channel.attributes).toString)
		args.put('options', '''['port','«channel.port»'],['host', '«channel.host»']''')
		var operator = builder.build(typeof(AccessAO), args)
		return cacheService.getOperatorCache().add(VIEW + channel.attributes.name, operator)
	}

	def private parseStreamTo(StreamTo query) {
		var lastOperator = ''
		var sink = ''
		var Map<String, String> sinks = cacheService.getOperatorCache().getSinks();
		if (sinks.keySet.contains(VIEW + query.name))
			sink = sinks.get(VIEW + query.name)
		else if (sinks.keySet.contains(query.name))
			sink = sinks.get(query.name)

		if (query.statement !== null) {
			selectParser.parse(query.statement.select as SimpleSelect)
			lastOperator = cacheService.getOperatorCache().last()
		} else
			lastOperator = query.inputname

		if (sink != '') {
			sink = sink.replace("--INPUT--", lastOperator)
			if (cacheService.getOperatorCache().isBACKUPState()) {
				cacheService.getOperatorCache().changeToBACKUP();
			}
			sinks.remove(query.name)
			cacheService.getOperatorCache().add(sink, query.name)
		} else {
			cacheService.getOperatorCache().getStreamTo().put(query.name, query.name)
			cacheService.getOperatorCache().changeToBACKUP();
		}
	}

	def private CharSequence buildCreate1(String type, AccessFramework pars, SchemaDefinition schema, String name) {
		var Class<?> t = null
		var input = "--INPUT--"
		if(type.equals("ACCESS")) t = typeof(AccessAO) else t = typeof(SenderAO)
		
		var Map<String, String> streamTo = cacheService.getOperatorCache().getStreamTo();
		
		if(streamTo.keySet.contains(name)) input = streamTo.get(name)
		var Map<String, String> argss = newHashMap
		if (t.equals(typeof(AccessAO))) {
			argss.put('source', name)
		} else {
			argss.put('sink', name)
		}
		argss.put('wrapper', pars.wrapper)
		argss.put('protocol', pars.protocol)
		argss.put('transport', pars.transport)
		argss.put('datahandler', pars.datahandler)
		argss.put('schema', if(argss.containsKey('source')) extractSchema(schema).toString else null)
		argss.put('options', utilityService.generateKeyValueString(pars.keys, pars.values, ','))
		argss.put('input', if(argss.containsKey('sink')) input else null)
		return builder.build(t, argss)
	}

//	def boolean checkIfSelectAll(List<Attribute> attributes) {
//		if (attributes.empty)
//			return true
//		else
//			for (Attribute attribute : attributes)
//				if (!attribute.name.contains('.*'))
//					return false
//		return true
//	}

	def private boolean isSame(String attribute1, String attribute2) {
		var name1 = attribute1
		var name2 = attribute2
		var source1 = ''
		var source2 = ''
		if (name1.contains('.')) {
			var split = name1.split('\\.')
			name1 = split.get(1)
			source1 = split.get(0)
		}

		if (name2.contains('.')) {
			var split = name2.split('\\.')
			name2 = split.get(1)
			source2 = split.get(0)
		}

		if (utilityService.getAttributeAliasesAsList().contains(name1))
			name1 = utilityService.getAttributeFromAlias(name1).attributename
		if (utilityService.getAttributeAliasesAsList().contains(name2))
			name2 = utilityService.getAttributeFromAlias(name2).attributename
		if (utilityService.getSourceAliasesAsList().contains(source1))
			source1 = utilityService.getSourceNameFromAlias(source1)
		if (utilityService.getSourceAliasesAsList().contains(source2))
			source2 = utilityService.getSourceNameFromAlias(source2)

		if (name1.equals(name2))
			if (source1 == '' || source2 == '')
				return true
			else if (source1.equals(source2))
				return true
		return false
	}

//TODO Put this a helper class
	def boolean containsAttribute(List<Attribute> list, Attribute attribute) {
		for (Attribute element : list) {
			if (isSame(attribute.name, element.name)) {
				return true
			}
		}
		return false
	}

	def boolean isSelectAll(SimpleSelect select) {
		for (SelectArgument a : select.arguments)
			if (a.attribute !== null)
				return false
		return true
	}

//
	def void setSchema(List<SystemSource> schemata) { utilityService.sourcesStructs = schemata }

	def setDatabaseConnections(Map<String, String> connections) {
		databaseConnections = connections;
	}

}
