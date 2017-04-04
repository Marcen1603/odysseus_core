package de.uniol.inf.is.odysseus.parser.novel.cql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

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
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Command;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DropStream;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.RightsManagement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.RoleManagement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SchemaDefinition;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Statement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.UserManagement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.impl.CommandImpl;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.NameProvider;

public class CQLParser implements IQueryParser 
{
	
	private List<IExecutorCommand> executorCommands;
	private Injector injector;
	private CQLGenerator generator;
	private XtextResourceSet resourceSet;
	private Resource resource;
	private Model model;

	public CQLParser() 
	{
		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		injector = new CQLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		generator = injector.getInstance(CQLGenerator.class);
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		resource = resourceSet.createResource(URI.createURI("dummy:/example.cql"));
		executorCommands = new ArrayList<>();
	}
	
	@Override
	public synchronized List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException 
	{
		// Create model from query string
		try(InputStream in = new ByteArrayInputStream(query.getBytes())) 
		{
			resource.load(in, resourceSet.getLoadOptions());
			model = (Model) resource.getContents().get(0);
		} 
		catch (Exception e) 
		{ 
			generator.clear();
			resource.unload();
			throw new QueryParseException("internal error while loading the cql model" + e.getMessage(), e); 
		}
		// Register schemata from cql create stream/sink queries
		CQLDictionary dictionary = createDictionary(EcoreUtil2.eAllOfType(model, SchemaDefinition.class), user);
		Set<SDFSchema> cqlschemata = dictionary.getSchema();
		// Get schemata from other parsers and query languages
		Set<SDFSchema> otherschemata = executor.getDataDictionary(user).getStreamsAndViews(user).stream()
				.map(e -> e.getValue().getOutputSchema()).collect(Collectors.toSet());
		// Set schemata to the generator
		generator.setOtherSchemata(otherschemata);
		generator.setCQLSchemata(cqlschemata);
		generator.setNameProvider(new NameProvider());
		executorCommands.clear();
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		try
		{
			// Validate model
			List<Issue> issues = injector.getInstance(IResourceValidator.class).validate(resource, CheckMode.NORMAL_ONLY, CancelIndicator.NullImpl);
			if(issues.isEmpty())
			{
				// Translate query to executor commands
				for(EObject component : model.getComponents())
				{
					if(component instanceof Statement)
					{
						// Generate a pql operator plan from the given model and create a new {@ IExecutorCommand}
						generator.doGenerate(component.eResource(), fsa, null);
						for(Entry<String, CharSequence> e : fsa.getTextFiles().entrySet())
							executorCommands.add(new AddQueryCommand(e.getValue().toString(), "PQL", user, null, context, new ArrayList<>(), false));
					}
					else if(component instanceof Command)
					{
						// There are no pql operators for {@link CommandImpl}, instead get the
						// corresponding executor command like {@link CreateStreamCommand} for
						// the given command.
						executorCommands.add(getExecutorCommand((Command) component, user));
					}
					else
					{
						throw new QueryParseException("Given querie contains unknown component: " + component.toString());
					}
				}
			
//				for(EObject statement : EcoreUtil2.eAllOfType(model, StatementImpl.class))
//				{
//					EObject type = ((StatementImpl) statement);
//					if(type instanceof CommandImpl)
//					{
//						// There are no pql operators for {@link CommandImpl}, instead get the
//						// corresponding executor command like {@link CreateStreamCommand} for
//						// the given command.
//						executorCommands.add(getExecutorCommand((CommandImpl) type, user));
//					}
//					else
//					{
//						// Generate a pql operator plan from the given model and create a new {@ IExecutorCommand}
//						generator.doGenerate(statement.eResource(), fsa, null);
//						for(Entry<String, CharSequence> e : fsa.getTextFiles().entrySet())
//							executorCommands.add(new AddQueryCommand(e.getValue().toString(), "PQL", user, null, context, new ArrayList<>(), false));
//					}
//				}
			}
			else
			{
				StringBuilder builder = new StringBuilder();
				builder.append("Model is not valid: The following issues were reported" + System.getProperty("line.separator"));
				int counter = 0;
				for(Issue issue : issues)
				{
					builder.append(counter++ + " ");
					builder.append(issue.getSeverity());
					builder.append(" - ");
					builder.append(issue.getMessage());
					builder.append(System.getProperty("line.separator"));
				}
				throw new QueryParseException(builder.toString());
			}
		}
		catch(Exception e)
		{
			CQLDictionaryProvider.removeDictionary(user);
			executorCommands.clear();
			throw e;
		}
		finally
		{
			if(fsa != null)
			{
				for(String filename : fsa.getTextFiles().keySet())
					fsa.deleteFile(filename);
			}
			generator.clear();
			resource.unload();
		}
		
		return executorCommands;
	}

	private IExecutorCommand getExecutorCommand(Command command, ISession user)
	{
		IExecutorCommand executorCommand = null;
		EObject commandType = command.getType();
		if(commandType instanceof DropStream)
		{
			DropStream cast = ((DropStream) commandType);
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
		else if(commandType instanceof UserManagement)
		{
			UserManagement cast = ((UserManagement) commandType);
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
		else if(commandType instanceof RightsManagement)
		{
			boolean isGrant;
			RightsManagement cast = ((RightsManagement) commandType);
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
					executorCommand = new RevokePermissionCommand(cast.getUser(), operations, objects, user);
			}
		}
		else if(commandType instanceof RoleManagement)
		{
			RoleManagement cast = ((RoleManagement) commandType);
			if(cast.getName().toUpperCase().equals("GRANT"))
				executorCommand = new GrantRoleCommand(cast.getUser(), cast.getOperations(), user);
			else
				executorCommand = new RevokeRoleCommand(cast.getUser(), cast.getOperations(), user);
		}
		
		if(executorCommand == null)
			throw new QueryParseException("Command could no be translated: " + command.toString());
		return executorCommand;
	}
	
	private synchronized CQLDictionary createDictionary(List<SchemaDefinition> schemata, ISession user)
	{
		CQLDictionary dictionary = CQLDictionaryProvider.getDictionary(user);
		for(SchemaDefinition schema : schemata)
		{
			int size = schema.getArguments().size();
			List<SDFAttribute> attributes = new ArrayList<>(size);
			for(int i = 0; i < size - 1 ; i = i + 2)
			{
				String attributename 	  = schema.getArguments().get(i);
				SDFDatatype attributetype = new SDFDatatype(schema.getArguments().get(i + 1));
				attributes.add(new SDFAttribute(schema.getName(), attributename, attributetype));
			}
			dictionary.add(schema.getName(), attributes);
		}
		return dictionary;
	}

	@Override
	public Map<String, List<String>> getTokens(ISession user) { return new HashMap<>(); }

	@Override
	public List<String> getSuggestions(String hint, ISession user) { return new ArrayList<>(); }

	@Override
	public String getLanguage() { return "CQL"; } 

}
