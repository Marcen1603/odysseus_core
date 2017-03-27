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
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AddQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropStreamCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.ChangeUserPasswordCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.CreateRoleCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.CreateTenantCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.CreateUserCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.DropRoleCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.DropUserCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.GrantPermissionCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.GrantRoleCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.RevokePermissionCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user.RevokeRoleCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.PermissionFactory;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeDefinition;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DropCommand;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.RightsCommand;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.RightsRoleCommand;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.UserCommand;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CommandImpl;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.StatementImpl;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.NameProvider;
import de.uniol.inf.is.odysseus.parser.novel.cql.validation.CQLExpressionsValidator;

public class CQLParser implements IQueryParser 
{
	

	@Inject
	private ParseHelper<Model> parseHelper;
	
	@Inject 
	private IResourceValidator resourceValidator;
	
	private CQLExpressionsValidator validator;
	
	private Injector injector;
	private CQLGenerator generator;
	private XtextResourceSet resourceSet;
	private Resource resource;
	private Model model;

	public CQLParser() 
	{
		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		injector = new CQLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		validator = injector.getInstance(CQLExpressionsValidator.class);
		generator = injector.getInstance(CQLGenerator.class);
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		resource = resourceSet.createResource(URI.createURI("dummy:/example.cql"));
	}
	
	public synchronized void createDictionary(AttributeDefinition d, ISession user)
	{
		CQLDictionary dic = CQLDictionaryProvider.getDictionary(user);
		int size = d.getArguments().size();
		List<SDFAttribute> attributes = new ArrayList<>(size);
		for(int i = 0; i < size - 1 ; i = i + 2)
		{
			String attributename 	  = d.getArguments().get(i);
			SDFDatatype attributetype = new SDFDatatype(d.getArguments().get(i + 1));
			attributes.add(new SDFAttribute(d.getName(), attributename, attributetype));
		}
		dic.add(d.getName(), attributes);
	}
	
	public synchronized List<IExecutorCommand> translate(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException
	{
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
		
		//Get schemata from PQl queries
//		Set<SDFSchema> outerschema = executor.getExecutionPlan(user)
//										  .getQueries(user)
//										  .stream()
//										  .map(e -> e.getLogicalQuery().getLogicalPlan())
//										  .map(e -> e.getOutputSchema())
//										  .collect(Collectors.toSet());
		
		Set<SDFSchema> outerschema = executor.getDataDictionary(user)
				.getStreamsAndViews(user)
				.stream()
				.map(e -> e.getValue().getOutputSchema())
				.collect(Collectors.toSet());
		
		Set<SDFSchema> innerschema = (Set<SDFSchema>) CQLDictionaryProvider.getDictionary(user).getSchema();
//		System.out.println("outerschema:: "+outerschema.toString());
//		System.out.println("innerschema:: "+innerschema);
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
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		generator.setNameProvider(new NameProvider());
		generator.setOuterschema(outerschema);
		generator.setInnerschema(innerschema);
		List<IExecutorCommand> executorCommands = new ArrayList<>();
		for(EObject statement : EcoreUtil2.eAllOfType(model, StatementImpl.class))
		{
			EObject type = ((StatementImpl) statement).getType();
			if(type instanceof CommandImpl)
			{
				executorCommands.add(translateCommand((CommandImpl) type, user));
			}
			else
			{
				generator.doGenerate(statement.eResource(), fsa, null);
				for(Entry<String, CharSequence> e : fsa.getTextFiles().entrySet())
					executorCommands.add(new AddQueryCommand(e.getValue().toString(), "PQL", user, null, context, new ArrayList<>(), false));
			}
		}
		generator.clear();
		
		return executorCommands;
	}
	
	private IExecutorCommand translateCommand(CommandImpl command, ISession user)
	{
		IExecutorCommand executorCommand = null;
		if(command instanceof DropCommand)
		{
			System.out.println("is drop command");
			DropCommand cast = ((DropCommand) command);
			boolean exists = cast.getExists() != null ? true : false;
			switch(cast.getName().toUpperCase())
			{
			case("STREAM"):
				executorCommand = new DropStreamCommand(cast.getStream(), exists, user);
				CQLDictionaryProvider.getDictionary(user).remove(cast.getStream());//FIXME Not sure, if this methods works properly
				break;
			case("SINK"):
				executorCommand = new DropSinkCommand(cast.getStream(), exists, user);
				break;
			case("VIEW"):
				executorCommand = new DropViewCommand(cast.getStream(), exists, user);
				break;
			}
		}
		else if(command instanceof UserCommand)
		{
			System.out.println("is user command");
			UserCommand cast = ((UserCommand) command);
			switch(cast.getName().toUpperCase())
			{
			case("CREATE"):
				if(cast.getSubject().toUpperCase().equals("ROLE"))
					executorCommand = new CreateRoleCommand(cast.getSubjectName(), user);
				else if(cast.getSubject().toUpperCase().equals("USER"))
					executorCommand = new CreateUserCommand(cast.getSubjectName(), cast.getPassword(), user);
				else if(cast.getSubject().toUpperCase().equals("TENANT"))
					executorCommand = new CreateTenantCommand(cast.getSubjectName(), user);
				break;
			case("DROP"):
				if(cast.getSubject().toUpperCase().equals("ROLE"))
					executorCommand = new DropRoleCommand(cast.getSubjectName(), user);
				else if(cast.getSubject().toUpperCase().equals("USER"))
					executorCommand = new DropUserCommand(cast.getSubjectName(), user);
				break;
			case("ALTER"):
				executorCommand = new ChangeUserPasswordCommand(cast.getSubjectName(), cast.getPassword(), user);
				break;
			}
		}
		else if(command instanceof RightsCommand)
		{
			System.out.println("is rights command");
			boolean isGrant;
			RightsCommand cast = ((RightsCommand) command);
			if((isGrant = cast.getName().toUpperCase().equals("GRANT")) 
				|| cast.getName().toUpperCase().equals("REVOKE"))
			{
				List<IPermission> operations = new ArrayList<IPermission>();
				for (String r : cast.getOperations()) 
				{
					IPermission action = PermissionFactory.valueOf(r.toUpperCase());
					if (action != null) 
						operations.add(action);
					else 
						throw new QueryParseException("Right " + r + " not defined.");
				}
				List<String> objects = cast.getOperations2() == null ? new ArrayList<String>() : cast.getOperations2();
				if(isGrant)
					executorCommand = new GrantPermissionCommand(cast.getUser(), operations, objects, user);
				else
				{
					executorCommand = new RevokePermissionCommand(cast.getUser(), operations, objects, user);
					System.out.println("executorCommand:: " + executorCommand);
				}
			}
		}
		else if(command instanceof RightsRoleCommand)
		{
			System.out.println("is rights role command");
			RightsRoleCommand cast = ((RightsRoleCommand) command);
			if(cast.getName().toUpperCase().equals("GRANT"))
				executorCommand = new GrantRoleCommand(cast.getUser(), cast.getOperations(), user);
			else
			{
				executorCommand = new RevokeRoleCommand(cast.getUser(), cast.getOperations(), user);
				System.out.println("executorCommand:: " + executorCommand);
			}
		}
		else
		{
			System.out.println("no corresponding class found:: "  + command.toString());
		}
		
		if(executorCommand == null)
			throw new QueryParseException("Command could no be translated: " + command.toString());
		return executorCommand;
	}
	
	@Override
	public Map<String, List<String>> getTokens(ISession user) { return new HashMap<>(); }

	@Override
	public List<String> getSuggestions(String hint, ISession user) { return new ArrayList<>(); }

	@Override
	public String getLanguage() { return "CQL"; } 

}
