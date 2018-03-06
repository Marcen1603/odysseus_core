package de.uniol.inf.is.odysseus.parser.cql2.generator.parser

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AccessFramework
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Create
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateAccessFramework
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateChannelFormatViaFile
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateChannelFrameworkViaPort
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseSink
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDatabaseStream
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateView
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SchemaDefinition
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StreamTo
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ICreateParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService
import java.util.Map

class CreateParser implements ICreateParser {

	@Inject
	var ISelectParser selectParser
	@Inject
	var ICacheService cacheService
	@Inject
	var IUtilityService utilityService
	@Inject
	var AbstractPQLOperatorBuilder builder

	val String regex = ",$";

	override parseCreate(Create statement) {
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
	
	def private parseCreateDatabaseSink(CreateDatabaseSink sink) {
		var Map<String, String> args = newHashMap
		args.put('connection', sink.database)
		args.put('table', sink.table)
		var type = ''
		if (CQLGenerator.databaseConnections.keySet.contains(sink.database))
			type = CQLGenerator.databaseConnections.get(sink.database)
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

	def private CharSequence extractSchema(SchemaDefinition schema) {
		var attributenames = newArrayList
		var datatypes = newArrayList
		for (var i = 0; i < schema.arguments.size - 1; i = i + 2) {
			attributenames.add(schema.arguments.get(i))
			datatypes.add(schema.arguments.get(i + 1))
		}
		return utilityService.generateKeyValueString(attributenames, datatypes, ',')
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

	override parseStreamTo(StreamTo query) {
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
		
		//TODO refactor me
		val StringBuilder b = new StringBuilder();
		cacheService.getSystemSources().stream().filter(e | e.isMeta).forEach(e | b.append(e.getName) + ",")
		argss.put('metaattribute', b.toString.replaceAll(regex, ""))
		
		return builder.build(t, argss)
	}

}
