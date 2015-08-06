package de.uniol.inf.is.odysseus.iql.qdl.parser;



import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.parser.AbstractIQLParser;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;


public class QDLParser extends AbstractIQLParser<QDLTypeFactory, QDLTypeUtils> {
	
	private static final String LANGUAGE_NAME = "de.uniol.inf.is.odysseus.iql.qdl.QDL";
	private static final String QUERIES_DIR = "queries";
	
	
	@Inject
	public QDLParser(QDLTypeFactory typeFactory, QDLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}
	
	@Inject
	private OdysseusScriptGenerator generator;
	
	@Override
	public List<IExecutorCommand> parse(String text, IDataDictionary dd, ISession session, Context context) throws QueryParseException {
		return null;
	}



	@Override
	public void parse(IQLFile file, IProject project) {
		for (QDLQuery query : EcoreUtil2.getAllContentsOfType(file, QDLQuery.class)) {
			String outputPath = getIQLOutputPath()+QUERIES_DIR+File.separator+query.getSimpleName();
			cleanUpDir(outputPath);

			Collection<Resource> resources = createNecessaryIQLFiles(outputPath,query);
			generateJavaFiles(resources);

			copyAndMoveUserEditiedFiles(project, outputPath);
			compileJavaFiles(outputPath, createClassPathEntries(resources));
			
			IQDLQuery qdlQuery = loadQuery(query,resources);
						
			ISession session = OdysseusRCPPlugIn.getActiveSession();
			ITenant tenant = UserManagementProvider.getDefaultTenant();
			IDataDictionary dd = DataDictionaryProvider.getDataDictionary(tenant);
			
			String script = generator.createOdysseusScript(qdlQuery, dd, session);
			QDLServiceBinding.getExecutor().addQuery(script, "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), Context.empty());
		}
	}
	
	@SuppressWarnings("unchecked")
	private IQDLQuery loadQuery(QDLQuery query, Collection<Resource> resources) {
		try {
			Collection<URL> urls = new HashSet<>();
			for (Resource res : resources) {
				URI uri = res.getURI();
				uri = uri.trimSegments(1);		
				String outputPath = uri.toFileString();
				urls.add(new File(outputPath).toURI().toURL());
			}			
			URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
			Class<? extends IQDLQuery> queryClass = (Class<? extends IQDLQuery>) Class.forName(query.getSimpleName(), true, classLoader);
			return queryClass.newInstance();		
		} catch (Exception e) {
			throw new QueryParseException("error while loading query " +query.getSimpleName(),e);
		} 	
	}


	
	@Override
	protected String getLanguageName() {
		return LANGUAGE_NAME;
	}

}
