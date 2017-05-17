package de.uniol.inf.is.odysseus.parser.novel.cql;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
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
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateContextStore;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateDataBaseConnectionGeneric;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CreateDataBaseConnectionJDBC;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DropContextStore;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DropDatabaseConnection;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DropStream;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.RightsManagement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.RoleManagement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Statement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.UserManagement;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.NameProvider;

public class CQLParser implements IQueryParser {

	private static Set<SDFSchema> currentSchema;
	private static CQLGenerator generator;
	private static Map<String, String> databaseConnections = new HashMap<>();

	private List<IExecutorCommand> executorCommands;
	private Injector injector;
	private XtextResourceSet resourceSet;
	private Resource resource;
	private Model model;
	private String[] tokens;

	public CQLParser() {
		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		injector = new CQLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		generator = injector.getInstance(CQLGenerator.class);
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		resource = resourceSet.createResource(URI.createURI("dummy:/example.cql"));
		executorCommands = new ArrayList<>();
		// Get all parser tokens
		List<String> list = new ArrayList<>();
		String pathToTokens = "/../bin/de/uniol/inf/is/odysseus/parser/novel/cql/parser/antlr/internal/InternalCQLParser.tokens";
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(pathToTokens)) {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((line = br.readLine()) != null) {
				if (line.contains("'")) {
					line = line.substring(1, line.lastIndexOf("'"));
					list.add(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			tokens = list.toArray(new String[list.size()]);
		}

	}

	@Override
	public synchronized List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException {
		// Create model from query string
		try (InputStream in = new ByteArrayInputStream(query.getBytes())) {
			resource.load(in, resourceSet.getLoadOptions());
			model = (Model) resource.getContents().get(0);
		} catch (Exception e) {
			generator.clear();
			resource.unload();
			throw new QueryParseException("internal error while loading the cql model" + e.getMessage(), e);
		}
		Set<SDFSchema> otherschemata = executor.getDataDictionary(user).getStreamsAndViews(user).stream()
				.map(e -> e.getValue().getOutputSchema()).collect(Collectors.toSet());
		generator.setOtherSchemata((currentSchema = otherschemata));
		generator.setNameProvider(new NameProvider());
		executorCommands.clear();
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		try {
			// Validate model
			List<Issue> issues = injector.getInstance(IResourceValidator.class).validate(resource,
					CheckMode.NORMAL_ONLY, CancelIndicator.NullImpl);
			if (issues.isEmpty()) {
				// Translate query to executor commands
				for (EObject component : model.getComponents()) {
					if (component instanceof Statement) {
						// Generate a pql operator plan from the given model and
						// create a new {@ IExecutorCommand}
						generator.doGenerate(component.eResource(), fsa, null);
						for (Entry<String, CharSequence> e : fsa.getTextFiles().entrySet())
							if (e.getValue() != null && !e.getValue().equals(""))
								executorCommands.add(new AddQueryCommand(e.getValue().toString(), "PQL", user, null,
										context, new ArrayList<>(), false));
					} else if (component instanceof Command) {
						// There are no pql operators for {@link CommandImpl},
						// instead get the
						// corresponding executor command like {@link
						// CreateStreamCommand} for
						// the given command.
						IExecutorCommand command = getExecutorCommand((Command) component, user, dd, metaAttribute,
								executorCommands);
						if (command != null)
							executorCommands.add(command);
					} else {
						throw new QueryParseException(
								"Given querie contains unknown component: " + component.toString());
					}
				}
			} else {
				StringBuilder builder = new StringBuilder();
				builder.append("Model is not valid: The following issues were reported"
						+ System.getProperty("line.separator"));
				int counter = 0;
				for (Issue issue : issues) {
					builder.append(counter++ + " ");
					builder.append(issue.getSeverity());
					builder.append(" - ");
					builder.append(issue.getMessage());
					builder.append(System.getProperty("line.separator"));
				}
				throw new QueryParseException(builder.toString());
			}
		} catch (Exception e) {
			executorCommands.clear();
			throw e;
		} finally {
			if (fsa != null) {
				for (String filename : fsa.getTextFiles().keySet())
					fsa.deleteFile(filename);
			}
			generator.clear();
			resource.unload();
		}

		return executorCommands;
	}

	@SuppressWarnings("unchecked")
	private IExecutorCommand getExecutorCommand(Command command, ISession user, IDataDictionary dictionary,
			IMetaAttribute metaattribute, List<IExecutorCommand> commands) {
		boolean noExecutorCommand = false;
		IExecutorCommand executorCommand = null;
		EObject commandType = command.getType();

		if (commandType instanceof DropStream) {
			DropStream cast = ((DropStream) commandType);
			boolean exists = cast.getExists() != null ? true : false;
			switch (cast.getName().toUpperCase()) {
			case ("STREAM"):
				executorCommand = new DropStreamCommand(cast.getStream(), exists, user);
				CQLDictionaryProvider.getDictionary(user).remove(cast.getStream());
				break;
			case ("SINK"):
				executorCommand = new DropSinkCommand(cast.getStream(), exists, user);
				break;
			case ("VIEW"):
				executorCommand = new DropViewCommand(cast.getStream(), exists, user);
				break;
			}
		} else if (commandType instanceof UserManagement) {
			UserManagement cast = ((UserManagement) commandType);
			switch (cast.getName().toUpperCase()) {
			case ("CREATE"):
				if (cast.getSubject().toUpperCase().equals("ROLE"))
					executorCommand = new CreateRoleCommand(cast.getSubjectName(), user);
				else if (cast.getSubject().toUpperCase().equals("USER"))
					executorCommand = new CreateUserCommand(cast.getSubjectName(), cast.getPassword(), user);
				else if (cast.getSubject().toUpperCase().equals("TENANT"))
					executorCommand = new CreateTenantCommand(cast.getSubjectName(), user);
				break;
			case ("DROP"):
				if (cast.getSubject().toUpperCase().equals("ROLE"))
					executorCommand = new DropRoleCommand(cast.getSubjectName(), user);
				else if (cast.getSubject().toUpperCase().equals("USER"))
					executorCommand = new DropUserCommand(cast.getSubjectName(), user);
				break;
			case ("ALTER"):
				executorCommand = new ChangeUserPasswordCommand(cast.getSubjectName(), cast.getPassword(), user);
				break;
			}
		} else if (commandType instanceof RightsManagement) {
			boolean isGrant;
			RightsManagement cast = ((RightsManagement) commandType);
			if ((isGrant = cast.getName().toUpperCase().equals("GRANT"))
					|| cast.getName().toUpperCase().equals("REVOKE")) {
				List<IPermission> operations = new ArrayList<IPermission>();
				for (String r : cast.getOperations()) {
					IPermission action = PermissionFactory.valueOf(r.toUpperCase());
					if (action != null)
						operations.add(action);
					else
						throw new QueryParseException("Right " + r + " not defined.");
				}
				List<String> objects = cast.getOperations2() == null ? new ArrayList<String>() : cast.getOperations2();
				if (isGrant)
					executorCommand = new GrantPermissionCommand(cast.getUser(), operations, objects, user);
				else
					executorCommand = new RevokePermissionCommand(cast.getUser(), operations, objects, user);
			}
		} else if (commandType instanceof RoleManagement) {
			RoleManagement cast = ((RoleManagement) commandType);
			if (cast.getName().toUpperCase().equals("GRANT"))
				executorCommand = new GrantRoleCommand(cast.getUser(), cast.getOperations(), user);
			else
				executorCommand = new RevokeRoleCommand(cast.getUser(), cast.getOperations(), user);
		} else if (commandType instanceof CreateDataBaseConnectionGeneric) {
			noExecutorCommand = true;
			databaseConnections = (Map<String, String>) ExtensionFactory.getInstance().getExtension("DatabaseParser").parse((CreateDataBaseConnectionGeneric) commandType);
		} else if (commandType instanceof CreateDataBaseConnectionJDBC) {
			noExecutorCommand = true;
			databaseConnections = (Map<String, String>) ExtensionFactory.getInstance().getExtension("DatabaseParser").parse((CreateDataBaseConnectionJDBC) commandType);
		} else if (commandType instanceof DropDatabaseConnection) {
			noExecutorCommand = true;
			databaseConnections = (Map<String, String>) ExtensionFactory.getInstance().getExtension("DatabaseParser").parse((DropDatabaseConnection) commandType);
		} else if (commandType instanceof CreateContextStore) {
			noExecutorCommand = true;
			IExtension contextParser = ExtensionFactory.getInstance().getExtension("ContextParser");
			contextParser.setCommands(commands);
			contextParser.setDataDictionary(dictionary);
			contextParser.setUser(user);
			contextParser.setMetaAttribute(metaattribute);
			contextParser.parse((CreateContextStore) commandType);
		} else if (commandType instanceof DropContextStore) {
			noExecutorCommand = true;
			ExtensionFactory.getInstance().getExtension("ContextParser").parse((DropContextStore) commandType);
		}

		if (!noExecutorCommand && executorCommand == null)
			throw new QueryParseException("Command could no be translated: " + commandType.toString());
		return executorCommand;
	}

	public static Map<String, String> getDatabaseConnections() {
		return databaseConnections;
	}

	public static Set<SDFSchema> getCurrentSchema() {
		return currentSchema;
	}

	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		Map<String, List<String>> tokens = new HashMap<>();
		tokens.put("TOKEN", Arrays.asList(this.tokens));
		return tokens;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		return new ArrayList<>();
	}

	@Override
	public String getLanguage() {
		return "CQL";
	}

}
