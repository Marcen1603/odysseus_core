package de.uniol.inf.is.odysseus.parser.novel.cql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.GeneratorDelegate;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.validation.IResourceValidator;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.pql.PQLParser;

public class CQLParser implements IQueryParser 
{
	
	private static Map<String, IParameter<?>> queryParameters = new HashMap<String, IParameter<?>>();

	@Inject
	private ParseHelper<Model> parseHelper;
	
	@Inject 
	private IResourceValidator resourceValidator;
	
	private Injector injector;
	private GeneratorDelegate generator;
	private XtextResourceSet resourceSet;
	private Resource resource;
	
	public CQLParser() 
	{
		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		injector = new CQLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
		generator = injector.getInstance(GeneratorDelegate.class);
		resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		resource = resourceSet.createResource(URI.createURI("dummy:/example.cql"));
	}

	@Override
	public List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException 
	{
		try(InputStream in = new ByteArrayInputStream(query.getBytes())) 
		{
			resource.load(in, resourceSet.getLoadOptions());
		} 
		catch (IOException e) 
		{ 
			throw new QueryParseException("internal error while loading the cql model", e); 
		}
		
		Model model = (Model) resource.getContents().get(0);
		validate();
		return new PQLParser().parse(generatePQLString(query, model), user, dd, context, metaAttribute, executor);
	}

	private String generatePQLString(String str, Model model) throws QueryParseException
	{
		InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
		generator.doGenerate(model.eResource(), fsa);
		String pqlString = null;
		for (Entry<String, CharSequence> file : fsa.getTextFiles().entrySet()) 
		{
			  pqlString = file.getValue().toString();
			  fsa.deleteFile(file.getKey());
		}
		if (pqlString != null)
			return pqlString;
		else
			new QueryParseException("given cql query was empty and could not be transformed to a pql query");
		return null;
	}

	private void validate() throws QueryParseException
	{
//		 Validation
//		IResourceValidator validator = (resourceSet).getResourceServiceProvider().getResourceValidator();
//		List<Issue> issues = resourceValidator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
//		for (Issue issue : issues) 
//		{
//		  System.out.println(issue.getMessage());
//		}
	}
	
	@Override
	public Map<String, List<String>> getTokens(ISession user) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLanguage() { return "CQL2"; }//TODO return "CQL"; } 

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
