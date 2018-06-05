package de.uniol.inf.is.odysseus.iql.qdl.ui.executor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.access.jdt.IJavaProjectProvider;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.basic.ui.executor.BasicIQLUiExecutor;
import de.uniol.inf.is.odysseus.iql.basic.ui.executor.IIQLUiExecutor;
import de.uniol.inf.is.odysseus.iql.basic.ui.typing.BasicIQLUiTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.executor.QDLExecutor;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class QDLUiExecutor extends QDLExecutor implements IIQLUiExecutor{


	@Inject
	private IJavaProjectProvider javaProjectProvider;
	
	private static final Logger LOG = LoggerFactory.getLogger(QDLUiExecutor.class);

	
	@Inject
	public QDLUiExecutor(IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}
	
	@Override
	protected String getFolder(IQLModel file) {
		return BasicIQLUiTypeUtils.getOutputFolder(file.eResource());
	}

	@Override
	public void parse(IQLModel model) {
		ResourceSet resourceSet = EcoreUtil2.getResourceSet(model);
		List<QDLQuery> queries = EcoreUtil2.getAllContentsOfType(model, QDLQuery.class);
		if (queries.size() == 0) {
			throw new QueryParseException("No queries found");
		}
		for (QDLQuery query : queries) {
			String outputPath = BasicIQLTypeUtils.getIQLOutputPath()+File.separator+QUERIES_DIR+File.separator+query.getSimpleName();
			cleanUpDir(outputPath);	
			Collection<IQLModelElement> resources = getModelElementsToCompile(resourceSet, outputPath,query);

			generateJavaFiles(resources, outputPath);

			copyAndMoveUserEditiedFiles(query, outputPath);
			compileJavaFiles(outputPath, createClassPathEntries(resourceSet));

			IQDLQuery qdlQuery = loadQuery(query, resourceSet);
						
			ISession session = OdysseusRCPPlugIn.getActiveSession();
			ITenant tenant = UserManagementProvider.instance.getDefaultTenant();
			IDataDictionary dd = DataDictionaryProvider.instance.getDataDictionary(tenant);
			
			String script = generator.createOdysseusScript(qdlQuery, dd, session);

			LOG.info("Generated OdysseusScript for QDL-Query "+query.getSimpleName()+System.lineSeparator()+script);

			QDLServiceBinding.getExecutor().addQuery(script, "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), Context.empty());

			LOG.info("Adding textual query using QDL for user "+session.getUser().getName()+" done.");
		}
	}
	
	protected void copyAndMoveUserEditiedFiles(QDLQuery query, String path) {
		BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(query, path, query.getSimpleName());
		Collection<EObject> userDefinedTypes = new HashSet<>();
		collectUserDefinedTypes(query, userDefinedTypes);
		for (EObject obj : userDefinedTypes) {
			if (obj instanceof IQLClass) {
				BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(obj, path, ((IQLClass) obj).getSimpleName());
			} else if (obj instanceof IQLInterface) {
				BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(obj, path, ((IQLClass) obj).getSimpleName());
			}
		}
	}
	
	@Override
	protected Collection<URL> createClassloaderURLs(QDLQuery query, ResourceSet resourceSet) {
		Collection<URL> urls = super.createClassloaderURLs(query, resourceSet);
		IJavaProject project = javaProjectProvider.getJavaProject(resourceSet);
		if (project != null) {
			try {
				IPath path = project.getOutputLocation();
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IFolder folder = root.getFolder(path);
				urls.add(folder.getLocation().toFile().toURI().toURL());
				for (String cp : BasicIQLUiExecutor.readBundleClasspath(project)) {
					urls.add(new File(folder.getParent().getLocation().toFile(), cp).toURI().toURL());
				}
			} catch (JavaModelException | MalformedURLException e) {
				LOG.error("error while creating classloader urls", e);
				throw new QueryParseException("error while creating classloader urls : "+System.lineSeparator() +e.getMessage(),e);
			}
		}
		return urls;
	}

	@Override
	protected Collection<String> createClassPathEntries(ResourceSet set) {
		Collection<String> result = super.createClassPathEntries(set);
		
		IJavaProject project = javaProjectProvider.getJavaProject(set);
		if (project != null) {
			try {
				IPath path = project.getOutputLocation();
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IFolder folder = root.getFolder(path);
				result.add(folder.getLocation().toFile().getAbsolutePath());
				for (String cp : BasicIQLUiExecutor.readBundleClasspath(project)) {
					result.add(folder.getParent().getLocation().toFile().getAbsolutePath()+File.separator+cp);
				}
				for (String bundleName : BasicIQLUiExecutor.readRequiredBundles(project)) {
					Bundle bundle = Platform.getBundle(bundleName);
					if (bundle != null) {
						result.addAll(getBundleClassPathEntries(bundle));
					}
				}
			} catch (JavaModelException e) {
				LOG.error("error while creating classpath entries", e);
				throw new QueryParseException("error while creating classpath entries : "+System.lineSeparator() +e.getMessage(),e);
			}
		}
		
		return result;
	}
}
