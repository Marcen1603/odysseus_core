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
import org.eclipse.emf.ecore.EObject;
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
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AddQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropStreamCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeDefinition;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CommandImpl;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.StatementImpl;
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
	private PQLParser pqlParser;
	 
//	private String types;
//	private String regex;
	
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
	
//	public synchronized void createDictionary(List<Create> list, ISession user)
//	{
//		CQLDictionary dic = CQLDictionaryProvider.getDictionary(user);
//		for(Create stmt : list)
//		{
//			if(stmt.getAccessframework() != null && stmt.getAccessframework().getType().equals("SINK"))//TODO Refactore
//			{
//				// do nothing
//			}
//			else
//			{
//				if(stmt.getAccessframework() != null)
//				{
//					AccessFramework create = stmt.getAccessframework();
//					int s = create.getAttributes().size();
//					SDFAttribute[] attributes = new SDFAttribute[s];
//					for(int i = 0; i < s; i++)
//					{
//						String name = create.getAttributes().get(i).getName();
//						SDFDatatype type = new SDFDatatype(create.getDatatypes().get(i).getValue());
//						attributes[i] = new SDFAttribute(create.getName(), name, type);
//					}
//					dic.add(attributes);
//				}
//				else//CHANNEL FORMAT
//				{
//					ChannelFormat create = stmt.getChannelformat();
//					if(create.getStream() != null)//CREATE STREAN
//					{
//						int s = create.getStream().getAttributes().size();
//						SDFAttribute[] attributes = new SDFAttribute[s];
//						for(int i = 0; i < s; i++)
//						{
//							String name = create.getStream().getAttributes().get(i).getName();
//							SDFDatatype type = new SDFDatatype(create.getStream().getDatatypes().get(i).getValue());
//							attributes[i] = new SDFAttribute(create.getStream().getName(), name, type);
//						}
//						dic.add(attributes);
//					}
//					else// CREATE VIEW
//					{
//					}
//				}
//			}
//		}
//	}
//	
	
	public synchronized void createDictionary(AttributeDefinition d, ISession user)
	{
		CQLDictionary dic = CQLDictionaryProvider.getDictionary(user);
		int s = d.getAttributes().size();
		SDFAttribute[] attributes = new SDFAttribute[s];
		for(int i = 0; i < s; i++)
		{
			String name = d.getAttributes().get(i).getName();
			SDFDatatype type = new SDFDatatype(d.getDatatypes().get(i).getValue());
			attributes[i] = new SDFAttribute(d.getName(), name, type);
		}
		dic.add(attributes);
	}
	
	public synchronized List<IExecutorCommand> translate(String query, ISession user, IDataDictionary dd, Context context,
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
		
		for(AttributeDefinition d : EcoreUtil2.eAllOfType(model, AttributeDefinition.class))
		{
				createDictionary(d, user);
		}
		
//		createDictionary(EcoreUtil2.eAllOfType(model, Create.class), user);
		
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
		return generate(query.toString(), model, outerschema, innerschema, user, dd, context, metaAttribute, executor);
	}
	
	private List<IExecutorCommand> commands = new ArrayList<>();
	
	@Override
	public synchronized List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException 
	{
		commands = translate(query, user, dd, context, metaAttribute, executor);
		resource.unload();
		
		return commands;
	}
	
	public synchronized List<IExecutorCommand> generate(String str, Model model, Set<SDFSchema> outerschema, Set<SDFSchema> innerschema, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException
	{
		List<EObject> lll = EcoreUtil2.eAllContentsAsList(model.eResource());
 		List<EObject> stuff = lll.stream().filter(e -> {
 			if(e instanceof StatementImpl)
 				return true;
 			if(e instanceof CommandImpl)
 				return true;
 			return false;
 		}).collect(Collectors.toList());
 		
// 		System.out.println("lll= " + lll.toString());
//		System.out.println("stuff= " +  stuff.toString());
		
//		Systm.out.println("Generated PQL query text: ");//TODO Remove after debugging
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		generator.setOuterschema(outerschema);
		generator.setInnerschema(innerschema);
//		generator.doGenerate(model.eResource(), fsa, null);
		
		List<IExecutorCommand> list = new ArrayList<>();
		for(EObject obj : stuff)
		{
			List<IExecutorCommand> l = new ArrayList<>();
			if(obj instanceof CommandImpl)
			{
				CommandImpl cmd = (CommandImpl) obj;
				IExecutorCommand exCmd = null; 
				switch(cmd.getKeyword1().toUpperCase())
				{
				case("DROP"):
					boolean ifExists = cmd.getKeyword3() != null ? true : false;
					switch(cmd.getKeyword2().toUpperCase())
					{
					case("STREAM"):
						exCmd = new DropStreamCommand(cmd.getValue1(), ifExists, user);
					break;
					case("SINK"):
						exCmd = new DropSinkCommand(cmd.getValue1(), ifExists, user);
					break;
					}
				break;
				}
				
			list.add(exCmd);
			}
			else
			{
				generator.doGenerate(obj.eResource(), fsa, null);
				for(Entry<String, CharSequence> e : fsa.getTextFiles().entrySet())
				{
					l.add(new AddQueryCommand(e.getValue().toString(), "PQL", user, null, context, new ArrayList<>(), true));
				}
				list.addAll(l);
			}
			l.clear();
		}
		
		generator.clear();
		return list;
	}
	
	@Override
	public Map<String, List<String>> getTokens(ISession user) 
	{
		return new HashMap<>();
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
