package de.uniol.inf.is.odysseus.parser.cql2.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MissingParameterException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
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
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mep.FunctionStore;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.parser.cql2.CQLStandaloneSetupGenerated;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Command;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateContextStore;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDataBaseConnectionGeneric;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDataBaseConnectionJDBC;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.DropContextStore;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.DropDatabaseConnection;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.DropStream;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Model;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Query;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.RightsManagement;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.RoleManagement;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.UserManagement;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;

public class CQLParser implements IQueryParser {

	private static final InfoService infoService = InfoServiceFactory.getInfoService(CQLParser.class);
	private static final Logger logger = LoggerFactory.getLogger(CQLParser.class);

	private static Map<String, String> databaseConnections = new HashMap<>();
	private static Set<SDFSchema> currentSchema;
	private static CQLGenerator generator;
	private static List<String> failedQueries = new ArrayList<>();

	private List<IExecutorCommand> executorCommands;
	private XtextResourceSet resourceSet;
	private Injector injector;
	private Resource resource;
	private String[] tokens;
	private Model model;
	private String path;
	private boolean init = false;

	public CQLParser(String path) {
//		setup(path);
		this.path = path;
	}

	public CQLParser() {
//		setup("../");
		this.path = "../";
	}

	private void setup(String path) {
		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri(path);
		injector = new CQLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		generator = injector.getInstance(CQLGenerator.class);
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		resource = resourceSet.createResource(
				URI.createURI("platform:/resource/de.uniol.inf.is.odysseus.parser.cql2.server/example.cql", true));// "dummy:/example.cql"));
		executorCommands = new ArrayList<>();
		// Get all parser tokens
		List<String> list = new ArrayList<>();
		URL url = null;
		if (Platform.getBundle("de.uniol.inf.is.odysseus.parser.cql2") != null) {
			try {
				url = Platform.getBundle("de.uniol.inf.is.odysseus.parser.cql2").getEntry(
						"/src-gen/de/uniol/inf/is/odysseus/parser/cql2/parser/antlr/internal/InternalCQLParser.tokens");
			} catch (Exception e) {

			}
			if (url == null) {
				try {
					url = Platform.getBundle("de.uniol.inf.is.odysseus.parser.cql2").getEntry(
							"/bin/de/uniol/inf/is/odysseus/parser/cql2/parser/antlr/internal/InternalCQLParser.tokens");
				} catch (Exception e) {

				}
			}
		}
		if (url != null) {
			// String pathToTokens =
			// "/../bin/de/uniol/inf/is/odysseus/parser/cql2/parser/antlr/internal/InternalCQLParser.tokens";
			try (InputStream in = url.openStream()) {// getClass().getClassLoader().getResourceAsStream(pathToTokens))
														// {
				if (in != null) {
					String line;
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					while ((line = br.readLine()) != null) {
						if (line.contains("'")) {
							line = line.substring(1, line.lastIndexOf("'"));
							list.add(line);
						}
					}
				} else {
					throw new IOException("InputStream was null: no tokens could be loaded");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				tokens = list.toArray(new String[list.size()]);
			}
		} else {
			tokens = new String[1];
			infoService.warning("Could not read tokens!");
			logger.warn("Could not read tokens");
		}
	}

	public synchronized List<EObject> injectModel(InputStream in) throws IOException {
		resource.load(in, resourceSet.getLoadOptions());
		model = (Model) resource.getContents().get(0);
		return model.getComponents();
	}

	public synchronized Map<String, CharSequence> generatePQL(EObject component, InMemoryFileSystemAccess fsa,
			IGeneratorContext context) {
		generator.doGenerate(component.eResource(), fsa, context);
		return fsa.getTextFiles();
	}

	@Override
	public synchronized List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException {
		
		if (!init) {
			setup(this.path);
		}
		
		List<EObject> components;
		// Create model from query string
		try (InputStream in = new ByteArrayInputStream(query.getBytes())) {
			components = injectModel(in);
		} catch (Exception e) {
			generator.clear();
			resource.unload();
			throw new QueryParseException("internal error while loading the cql model" + e.getMessage(), e);
		}
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		try {
			// Validate model
			List<Issue> issues = injector.getInstance(IResourceValidator.class).validate(resource,
					CheckMode.NORMAL_ONLY, CancelIndicator.NullImpl);
			if (issues.isEmpty()) {
				Set<SDFSchema> schema = executor.getDataDictionary(user).getStreamsAndViews(user).stream()
						.map(e -> e.getValue().getOutputSchema()).collect(Collectors.toSet());
				generator.setSchema(convertSchemaToSystemSource(schema));
				generator.setMetaAttributes(convertSchemaToSystemSource(metaAttribute.getSchema()));
				executorCommands.clear();
				// Translate query to executor commands
				for (EObject component : components) {
					if (component instanceof Query) {
						// Generate a pql operator plan from the given model and
						// create a new {@ IExecutorCommand}
						for (Entry<String, CharSequence> e : generatePQL(component, fsa, null).entrySet()) {
							if (e.getValue() != null && !e.getValue().equals("")) {
								String pqlString = "#PARSER PQL\n";
								pqlString += "#ADDQUERY\n";
								pqlString += e.getValue().toString();
								infoService.info("Generated following PQL String:\n " + pqlString);
								logger.info("Generated following PQL String:\n " + pqlString);
								executorCommands.add(new AddQueryCommand(pqlString, "OdysseusScript", user, null,
										context, new ArrayList<>(), false));
							}
						}
					} else if (component instanceof Command) {
						// There are no pql operators for {@link CommandImpl},
						// instead get the corresponding
						// executor command like {@link CreateStreamCommand} for
						// the given command.
						IExecutorCommand command = parseCommand((Command) component, user, dd, metaAttribute,
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
			
			/* TODO WORKAROUND that relates to an issue where the PQLOperatorBuilder 
			 * cannot resolve all parameters for an Aggregate operator. At the 
			 * moment it is unknown why or when this bug occurs (only in rare cases).
			 * However, a MissingParameterException will be raised if this is happening.
			 * 
			 * To overcome the issue, the exception will be caught and the
			 * query will be build a second time. If this attempt fails, the
			 * query will be marked as failed.
			 * 
			 * author ~ Jens Plümer on 16.05.18
			 */
			if (e instanceof MissingParameterException) {
				if (!failedQueries.contains(query)) {
					logger.warn("Query could not be parsed because of MissingParameterException; trying a second time");
					failedQueries.add(query);
					generator.clear();
					resource.unload();
					List<IExecutorCommand> cmds = parse(query, user,dd,context, metaAttribute,executor);
					failedQueries.remove(query);
					return cmds;
				} 
			}
			
			
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
	private IExecutorCommand parseCommand(Command command, ISession user, IDataDictionary dictionary,
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
				// CQLDictionaryProvider.getDictionary(user).remove(cast.getStream());
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
				List<String> objects = cast.getOperations2() == null ? new ArrayList<String>() : cast.getOperations2();
				if (isGrant)
					executorCommand = new GrantPermissionCommand(cast.getUser(), cast.getOperations(), objects, user);
				else
					executorCommand = new RevokePermissionCommand(cast.getUser(), cast.getOperations(), objects, user);
			}
		} else if (commandType instanceof RoleManagement) {
			RoleManagement cast = ((RoleManagement) commandType);
			if (cast.getName().toUpperCase().equals("GRANT"))
				executorCommand = new GrantRoleCommand(cast.getUser(), cast.getOperations(), user);
			else
				executorCommand = new RevokeRoleCommand(cast.getUser(), cast.getOperations(), user);
		} else if (commandType instanceof CreateDataBaseConnectionGeneric) {
			noExecutorCommand = true;
			databaseConnections = (Map<String, String>) ExtensionFactory.getInstance().getExtension("DatabaseParser")
					.parse((CreateDataBaseConnectionGeneric) commandType);
			generator.setDatabaseConnections(databaseConnections);
		} else if (commandType instanceof CreateDataBaseConnectionJDBC) {
			noExecutorCommand = true;
			databaseConnections = (Map<String, String>) ExtensionFactory.getInstance().getExtension("DatabaseParser")
					.parse((CreateDataBaseConnectionJDBC) commandType);
			generator.setDatabaseConnections(databaseConnections);
		} else if (commandType instanceof DropDatabaseConnection) {
			noExecutorCommand = true;
			databaseConnections = (Map<String, String>) ExtensionFactory.getInstance().getExtension("DatabaseParser")
					.parse((DropDatabaseConnection) commandType);
			generator.setDatabaseConnections(databaseConnections);
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

	public Pattern getAggregateFunctionPattern() {
		return AggregateFunctionBuilderRegistry.getAggregatePattern();
	}

	public FunctionStore getFunctionStore() {
		return FunctionStore.getInstance();
	}

	public MEP getMEP() {
		return MEP.getInstance();
	}

	public Collection<SystemSource> convertSchemaToSystemSource(Collection<SDFSchema> schema) {
		return schema.stream().map(e -> convertSchemaToSystemSource(e)).flatMap(Collection::stream).collect(Collectors.toList());
	}
	
	public Collection<SystemSource> convertSchemaToSystemSource(SDFSchema schema) {
		
		Collection<SystemSource> col = new ArrayList<>();
		for (String sourcename : schema.getBaseSourceNames()) {
			SystemSource sourceStruct = new SystemSource();
			sourceStruct.setName(sourcename);
			for (SDFAttribute struct : schema.getAttributes()) {
				if (sourcename.equals(struct.getSourceName())) {
					sourceStruct.add(new SystemAttribute(
							sourceStruct, 
							struct.getAttributeName(),
							struct.getDatatype().toString()
							)
					);
				}
			}
			col.add(sourceStruct);
		}
	
		return col;
	}
	
	private Collection<SystemSource> convertSchemaToSystemSource(List<SDFMetaSchema> schema) {
		return convertSchemaToSystemSource(schema.stream().map(e -> (SDFSchema) e).collect(Collectors.toList()));
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
