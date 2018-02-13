/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.generator

import com.google.inject.Guice
import com.google.inject.Injector
import de.uniol.inf.is.odysseus.parser.cql2.CQLRuntimeModule
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexSelect
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Create
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Query
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StreamTo
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLBuilderModule
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.CacheModule
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ParserModule
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAggregationParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ICreateParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExistenceParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IPredicateParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IQuantificationParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser
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

/** Generates PQL text from a CQL text. */
class CQLGenerator implements IGenerator2 {

	val private Logger log = LoggerFactory.getLogger(CQLGenerator);

	public var static Injector injector;

	public static var Map<String, String> databaseConnections = newHashMap

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
	var ICreateParser createParser
	
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
		createParser = injector.getInstance(ICreateParser);
		
	}

	def void clear() {
		predicateParser.clear()
		utilityService.clear()
		joinParser.clear()
		renameParser.clear()
		selectParser.clear()
		attributeParser.clear()
		cacheService.getOperatorCache().flush()
		cacheService.getSystemSources().clear()
		cacheService.getSelectCache().flush()
		cacheService.getQueryCache().clear()
		SystemSource.clearQuerySources()
		SystemSource.clearAttributeAliases()
	}

	override afterGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) { clear() }

	override beforeGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) { clear() }

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
			createParser.parseCreate(query.type as Create)
		// check if query is a stream to statement
		} else if (query.type instanceof StreamTo) {
			createParser.parseStreamTo(query.type as StreamTo)
		}
			
		// Generate string pql operator plan	
		return cacheService.getOperatorCache().getPQL()
	}

	def void setSchema(List<SystemSource> schemata) { utilityService.sourcesStructs = schemata }

	def setDatabaseConnections(Map<String, String> connections) {
		databaseConnections = connections;
	}

}
