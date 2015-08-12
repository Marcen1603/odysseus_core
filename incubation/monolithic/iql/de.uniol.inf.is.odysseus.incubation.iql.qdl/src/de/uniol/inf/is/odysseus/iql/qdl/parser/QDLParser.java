package de.uniol.inf.is.odysseus.iql.qdl.parser;



import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.basic.parser.AbstractIQLParser;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;


public class QDLParser extends AbstractIQLParser<QDLTypeFactory, QDLTypeUtils> {
	
	private static final String LANGUAGE_NAME = "de.uniol.inf.is.odysseus.iql.qdl.QDL";
	protected static final String QUERIES_DIR = "queries";
	
	
	@Inject
	public QDLParser(QDLTypeFactory typeFactory, QDLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}
	
	@Inject
	protected OdysseusScriptGenerator generator;
	
	@Override
	public List<IExecutorCommand> parse(String text, IDataDictionary dd, ISession session, Context context) throws QueryParseException {
		return null;
	}

	
	@SuppressWarnings("unchecked")
	protected IQDLQuery loadQuery(QDLQuery query, Collection<Resource> resources) {
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
