package de.uniol.inf.is.odysseus.iql.qdl.executor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.executor.AbstractIQLExecutor;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;


public class QDLExecutor extends AbstractIQLExecutor<IQDLTypeDictionary, IQDLTypeUtils> {
	
	public static final String QUERIES_DIR = "queries";
	
	
	@Inject
	public QDLExecutor(IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}
	
	@Inject
	protected OdysseusScriptGenerator generator;
	
	@Inject
	private ParseHelper<IQLModel> parseHelper;
	
	private static final Logger LOG = LoggerFactory.getLogger(QDLExecutor.class);

	
	@Override
	public List<IExecutorCommand> parse(String text, IDataDictionary dd, ISession session, Context context) throws QueryParseException {
		List<IExecutorCommand> result = new ArrayList<>();
		
		IQLModel model = null;
		try {
			model = parseHelper.parse(text);
		} catch (Exception e) {
			throw new QueryParseException("error while parsing operator text: "+e.getMessage(),e);
		}
		
		ResourceSet resourceSet = EcoreUtil2.getResourceSet(model);
		
		List<QDLQuery> queryList = EcoreUtil2.getAllContentsOfType(model, QDLQuery.class);
		if (queryList.size() == 0) {
			throw new QueryParseException("No queries found");
		}			
		for (QDLQuery query : queryList) {
			String outputPath = BasicIQLTypeUtils.getIQLOutputPath()+File.separator+QUERIES_DIR+File.separator+query.getSimpleName();
			cleanUpDir(outputPath);
			
			Collection<IQLModelElement> modelElements = getModelElementsToCompile(EcoreUtil2.getResourceSet(model), outputPath,query);
			generateJavaFiles(modelElements, outputPath);
			
			compileJavaFiles(outputPath, createClassPathEntries(resourceSet));
			
			IQDLQuery qdlQuery = loadQuery(query, resourceSet);
				
			if (query.getMetadataList() == null || query.getMetadataList().getElements().size() == 0) {
				Collection<ILogicalQuery> queries = generator.createLogicalQueries(qdlQuery, dd, session);
				for (ILogicalQuery logQuery : queries) {
					IExecutorCommand exeCommand = new CreateQueryCommand(logQuery,session);
					result.add(exeCommand);
				}
			} else {
				String script = generator.createOdysseusScript(qdlQuery, dd, session);
				
				LOG.info("Generated OdysseusScript for QDL-Query "+query.getSimpleName()+System.lineSeparator()+script);

				QDLServiceBinding.getExecutor().addQuery(script, "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), Context.empty());
			}
			
			LOG.info("Adding textual query using QDL for user "+session.getUser().getName()+" done.");		}
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	protected IQDLQuery loadQuery(QDLQuery query, ResourceSet resourceSet) {
		Collection<URL> urls = createClassloaderURLs(query, resourceSet);			
		URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), QDLExecutor.class.getClassLoader());
		try {
			String queryName = converter.toJavaString(typeUtils.getLongName(query, false));
			Class<? extends IQDLQuery> queryClass = (Class<? extends IQDLQuery>) Class.forName(queryName, true, classLoader);
			return queryClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			LOG.error("error while loading query " +query.getSimpleName(), e);
			throw new QueryParseException("error while loading query " +query.getSimpleName()+": "+System.lineSeparator()+e.getMessage(),e);
		}
	}
	
	protected Collection<URL> createClassloaderURLs(QDLQuery query, ResourceSet resourceSet) {
		Collection<URL> urls = new HashSet<>();
		String outputPath = BasicIQLTypeUtils.getIQLOutputPath()+File.separator+QUERIES_DIR+File.separator+query.getSimpleName();
		File file = new File(outputPath);
		try {
			urls.add(file.toURI().toURL());
		} catch (MalformedURLException e1) {
			LOG.error("error while creating classloader urls" +query.getSimpleName(), e1);
			throw new QueryParseException("error while creating classloader urls " +query.getSimpleName()+": "+System.lineSeparator()+e1.getMessage(),e1);
		}
		return urls;
	}


}
