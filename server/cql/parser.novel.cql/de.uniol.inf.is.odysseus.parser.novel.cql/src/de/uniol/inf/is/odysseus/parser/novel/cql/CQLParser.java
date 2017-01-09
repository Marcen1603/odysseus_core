package de.uniol.inf.is.odysseus.parser.novel.cql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.validation.IResourceValidator;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AccessFramework;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ChannelFormat;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Create;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsTypeProvider;
import de.uniol.inf.is.odysseus.parser.novel.cql.validation.CQLExpressionsValidator;
import de.uniol.inf.is.odysseus.parser.pql.PQLParser;

public class CQLParser implements IQueryParser 
{
	
	private static Map<String, IParameter<?>> queryParameters = new HashMap<String, IParameter<?>>();

	@Inject
	private ParseHelper<Model> parseHelper;
	
	@Inject 
	private IResourceValidator resourceValidator;
	
	private CQLExpressionsValidator validator;
	private ExpressionsTypeProvider typeProvider;
	
	private Injector injector;
	private CQLGenerator generator;
	private XtextResourceSet resourceSet;
	private Resource resource;
	private Model model;
	 
	private String types;
	private String regex;
	
	/////
	/*
	 * Only one CREATE expression will be recognized!
	 * Therefore there must be a mechanism to detect
	 * all CREATE expressions in the query text...
	 */
//	Pattern pattern = Pattern.compile(regex);
//	Matcher matcher = pattern.matcher(query.toLowerCase());
//	if(matcher.find())
//	{
//		String attributes = matcher.group(2);
//		String[] l = attributes.split(",[ ]*");
////		Map<String, String> map = new HashMap<>();
//		SDFAttribute[] tmp = new SDFAttribute[l.length];
//		/*
//		 * If a query contains attributes with the same
//		 * name, this wont be recognized. That leads
//		 * probaly to an error. 
//		 */
//		for(int i = 0; i < l.length; i++)
//		{
//			String[] split = l[i].split(" ");
//			split[1] = split[1].toUpperCase();
//			tmp[i]=new SDFAttribute(matcher.group(1), split[0], new SDFDatatype(split[1]));
////			map.put(split[0], split[1]);
//		}
//		dictionary.add(user, tmp);
//	}

	public CQLParser() 
	{
		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		injector = new CQLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		typeProvider = injector.getInstance(ExpressionsTypeProvider.class);
		validator = injector.getInstance(CQLExpressionsValidator.class);
		generator = injector.getInstance(CQLGenerator.class);
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		resource = resourceSet.createResource(URI.createURI("dummy:/example.cql"));
//		DataDictionaryProvider.getAllDatatypeNames().forEach(s -> types += s +"|");
//		types = types.substring(0, types.length()-1);
//		String queryType = "stream|sink|view";
//		regex = ".*create[ ]*["+queryType+"][ ]*[a-z0-9]*[ ]+([a-z0-9]+){1}[ ]*[(]([a-z0-9]+[ ]+["+types+"]+[ ]*(, [a-z0-9]+[ ]+["+types+"]+)*)[)].*";
	}
	
	public synchronized void createDictionary(List<Create> list, ISession user)
	{
		CQLDictionary dic = CQLDictionaryProvider.getDictionary(user);
		for(Create stmt : list)
		{
			if(stmt.getAccessframework() != null && stmt.getAccessframework().getType().equals("SINK"))//TODO Refactore
			{
				// do nothing
			}
			else
			{
				if(stmt.getAccessframework() != null)
				{
					AccessFramework create = stmt.getAccessframework();
					int s = create.getAttributes().size();
					SDFAttribute[] attributes = new SDFAttribute[s];
					for(int i = 0; i < s; i++)
					{
						String name = create.getAttributes().get(i).getName();
						SDFDatatype type = new SDFDatatype(create.getDatatypes().get(i).getValue());
						attributes[i] = new SDFAttribute(create.getName(), name, type);
					}
					dic.add(attributes);
				}
				else//CHANNEL FORMAT
				{
					ChannelFormat create = stmt.getChannelformat();
					if(create.getStream() != null)//CREATE STREAN
					{
						int s = create.getStream().getAttributes().size();
						SDFAttribute[] attributes = new SDFAttribute[s];
						for(int i = 0; i < s; i++)
						{
							String name = create.getStream().getAttributes().get(i).getName();
							SDFDatatype type = new SDFDatatype(create.getStream().getDatatypes().get(i).getValue());
							attributes[i] = new SDFAttribute(create.getStream().getName(), name, type);
						}
						dic.add(attributes);
					}
					else// CREATE VIEW
					{
					}
				}
			}
		}
	}
	
	public synchronized String translate(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException
	{
//		System.out.println("ERROS:: " + resource.getErrors().toString());
		try(InputStream in = new ByteArrayInputStream(query.getBytes())) 
		{
			resource.load(in, resourceSet.getLoadOptions());
		} 
		catch (IOException e) 
		{ 
			throw new QueryParseException("internal error while loading the cql model" + e.getMessage(), e); 
		}
		
		model = (Model) resource.getContents().get(0);
		createDictionary(EcoreUtil2.eAllOfType(model, Create.class), user);
		
//		System.out.println(dic.toString());
		//Get schemata from PQl queries
		Set<SDFSchema> outerschema = executor.getExecutionPlan(user)
										  .getQueries(user)
										  .stream()
										  .map(e -> e.getLogicalQuery().getLogicalPlan())
										  .map(e -> e.getOutputSchema())
										  .collect(Collectors.toSet());
		//Get schemata from CQLDictionary
		Set<SDFSchema> innerschema = (Set<SDFSchema>) CQLDictionaryProvider.getDictionary(user).getSchema();
		return generate(query.toString(), model, outerschema, innerschema);
	}
	
	@Override
	public synchronized List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException 
	{
		String pqlQuery = translate(query, user, dd, context, metaAttribute, executor);
		resource.unload();
		return new PQLParser().parse(pqlQuery, user, dd, context, metaAttribute, executor);
	}
	
	public synchronized String generate(String str, Model model, Set<SDFSchema> outerschema, Set<SDFSchema> innerschema) throws QueryParseException
	{
		System.out.println("Generate PQL query text: ");//TODO Remover afer debugging
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		generator.setOuterschema(outerschema);
		generator.setInnerschema(innerschema);
		generator.doGenerate(model.eResource(), fsa, null);
		String pqlString = "";
		for(Entry<String, CharSequence> e : fsa.getTextFiles().entrySet())
		{
			pqlString += e.getValue().toString();
		}
		generator.clear();
		return pqlString;
	}

	@Override
	public Map<String, List<String>> getTokens(ISession user) 
	{
		return new HashMap();
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) 
	{
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public String getLanguage() { return "CQL"; } 

	public static void addQueryParameter(IParameter<?> parameter) 
	{
		String parameterName = parameter.getName();
		if (queryParameters.containsKey(parameterName)) {
			throw new IllegalArgumentException(
					"multiple definitions of query parameter: " + parameterName);
		}

		queryParameters.put(parameterName, parameter);
	}

	public static void removeQueryParameter(String identifier) 
	{
		queryParameters.remove(identifier);
	}

}
