package de.uniol.inf.is.odysseus.parser.novel.cql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.context.store.types.MultiElementStore;
import de.uniol.inf.is.odysseus.context.store.types.PartitionedMultiElementStore;
import de.uniol.inf.is.odysseus.context.store.types.SingleElementStore;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
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
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Command;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ContextStoreType;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateContextStore;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateDataBaseConnectionGeneric;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateDataBaseConnectionJDBC;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DropContextStore;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DropDatabaseConnection;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DropStream;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.RightsManagement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.RoleManagement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SchemaDefinition;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Statement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.UserManagement;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.NameProvider;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;

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
		// Register schemata from cql create stream/sink queries//TODO remove this
//		CQLDictionary dictionary = createDictionary(EcoreUtil2.eAllOfType(model, SchemaDefinition.class), user);
//		Set<SDFSchema> cqlschemata = dictionary.getSchema();
		// Get schemata from other parsers and query languages
		Set<SDFSchema> otherschemata = executor.getDataDictionary(user).getStreamsAndViews(user).stream()
				.map(e -> e.getValue().getOutputSchema()).collect(Collectors.toSet());
		// Set schemata to the generator
		generator.setOtherSchemata(otherschemata);
//		generator.setCQLSchemata(cqlschemata);
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
						IExecutorCommand command = getExecutorCommand((Command) component, user, dd, metaAttribute, executorCommands);
						if(command != null)
							executorCommands.add(command);
					}
					else
					{
						throw new QueryParseException("Given querie contains unknown component: " + component.toString());
					}
				}
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
//			CQLDictionaryProvider.removeDictionary(user);//TODO remove this
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

	private IExecutorCommand getExecutorCommand(Command command, ISession user,IDataDictionary dictionary, IMetaAttribute metaattribute, List<IExecutorCommand> commands)
	{
		boolean noExecutorCommand = false;
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
		else if(commandType instanceof CreateDataBaseConnectionGeneric)
		{
			noExecutorCommand = true;
			CreateDataBaseConnectionGeneric cast = ((CreateDataBaseConnectionGeneric) commandType);
			createDatabaseConnection(cast.getName(), cast.getDriver(), cast.getSource(), cast.getHost(), cast.getPort(), cast.getUser(), cast.getPassword(), cast.getLazy());
		}
		else if(commandType instanceof CreateDataBaseConnectionJDBC)
		{
			noExecutorCommand = true;
			CreateDataBaseConnectionJDBC cast = ((CreateDataBaseConnectionJDBC) commandType);
			createDatabaseConnection(cast.getName(), null, null, cast.getServer(), -1, cast.getUser(), cast.getPassword(), cast.getLazy());
		}
		else if(commandType instanceof DropDatabaseConnection)
		{
			noExecutorCommand = true;
			DatabaseConnectionDictionary.removeConnection(((DropDatabaseConnection) commandType).getName());
		}
		else if(commandType instanceof CreateContextStore)
		{
			noExecutorCommand = true;
			CreateContextStore cast = ((CreateContextStore) commandType);
			createContextStore(cast.getAttributes().getName(), cast.getContextType(), cast.getAttributes(), user, dictionary, metaattribute, commands);
		}
		else if(commandType instanceof DropContextStore)
		{
			noExecutorCommand = true;
			DropContextStore cast = ((DropContextStore) commandType);
			String name = cast.getName();
			if(ContextStoreManager.storeExists(name))
				ContextStoreManager.removeStore(name);
			else if(cast.getExists() != null)
					throw new QueryParseException("There is no store named \""+name+"\"");
		}
		
		if(!noExecutorCommand && executorCommand == null)
			throw new QueryParseException("Command could no be translated: " + commandType.toString());
		return executorCommand;
	}
	 	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createContextStore(String name, ContextStoreType contextType, SchemaDefinition definitions, ISession session, 
			IDataDictionary datadictionary, IMetaAttribute metaAttribute , List<IExecutorCommand> commands)
	{
		
		String schemaName = "ContextStore:" + name;
		List<SDFAttribute> attributes = new ArrayList<>();
		for(int i = 0; i < definitions.getArguments().size() - 1; i = i + 2)
		{
			SDFAttribute newAttribute = new SDFAttribute(schemaName, definitions.getArguments().get(i), new SDFDatatype(definitions.getArguments().get(i + 1)));
			attributes.add(newAttribute);
		}
		SDFSchema schema = SDFSchemaFactory.createNewTupleSchema(schemaName, attributes);
		
		
		ITimeIntervalSweepArea sa;
		try 
		{
			sa = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} 
		catch (InstantiationException | IllegalAccessException e1) 
		{
			throw new QueryParseException(e1);
		}
		
		int size = -1;
		int partitionedBy = -1;
		switch(contextType.getType().toUpperCase())
		{
		case("SINGLE"):
			size = 1;
		break;
		case("MULTI"):
			size = contextType.getSize();
			partitionedBy = contextType.getPartition();
		break;
		default:
			throw new QueryParseException("Type for context store does not exist!");
		}
		
        IContextStore<Tuple<? extends ITimeInterval>> store;
        if (size == 1) 
        {
            store = new SingleElementStore<Tuple<? extends ITimeInterval>>(name, schema);
        }
        else 
        {
        	if(partitionedBy < 0)
        		store = new MultiElementStore<Tuple<? extends ITimeInterval>>(name, schema, size, sa);
        	else
        	{
        		if(partitionedBy >= schema.size())
        			throw new QueryParseException("Partition key index is not compatiple with the schema!");
        		store = new PartitionedMultiElementStore<Tuple<? extends ITimeInterval>>(name, schema, size, partitionedBy);
        	}
        }

		try 
		{
			ContextStoreManager.addStore(name, store);
		} 
		catch (ContextManagementException e) 
		{
			throw new QueryParseException(e);
		}
	}
	
	private void createDatabaseConnection(String connectionName, String dbms, String dbname, String host, int port, String user, String pass, String lazy)
	{
		if (DatabaseConnectionDictionary.isConnectionExisting(connectionName))
			throw new QueryParseException("Connection with name \"" + connectionName + "\" already exists!");
		if(user == null)
		{
			user = "root";
			pass = "";
		}
		try 
		{
			if(dbms != null && dbname != null)
			{
	
				IDatabaseConnectionFactory factory = DatabaseConnectionDictionary.getFactory(dbms);
				if (factory == null) 
				{
					String currentInstalled = "";
					String sep = "";
					for (String n : DatabaseConnectionDictionary.getConnectionFactoryNames()) 
					{
						currentInstalled = currentInstalled + sep + n;
						sep = ", ";
					}
					throw new QueryParseException("DBMS \"" + dbms + "\" not supported! Currently available: " + currentInstalled);
				}
				IDatabaseConnection con = factory.createConnection(host, port, dbname, user, pass);
				DatabaseConnectionDictionary.addConnection(connectionName, con);
			}
			else//otherwise, we have a JDBC based connection
			{
				Properties props = new Properties();
				props.setProperty("user", user);
				props.setProperty("password", pass);
				IDatabaseConnection connection = new DatabaseConnection(host, props);
				DatabaseConnectionDictionary.addConnection(connectionName, connection);
			}
		}
		catch (Exception e) 
		{
			throw new QueryParseException("Error creating connection", e);
		}		
		
		// is check option used?
		if (lazy != null)
		{
			IDatabaseConnection con = DatabaseConnectionDictionary.getDatabaseConnection(connectionName);
			try 
			{
				con.checkProperties();
			} 
			catch (SQLException e) 
			{
				throw new QueryParseException("Check for database connection failed: " + e.getMessage(), e);
			}
		}
	}
	
	@Deprecated
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
