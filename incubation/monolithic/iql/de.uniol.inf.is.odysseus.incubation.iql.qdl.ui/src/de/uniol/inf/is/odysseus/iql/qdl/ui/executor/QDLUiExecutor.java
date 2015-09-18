package de.uniol.inf.is.odysseus.iql.qdl.ui.executor;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.access.jdt.IJavaProjectProvider;




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
	
	@Inject
	public QDLUiExecutor(IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}
	
	@Override
	protected String getFolder(IQLModel file) {
		return BasicIQLUiTypeUtils.getOutputFolder(file.eResource());
	}

	@Override
	public void parse(IQLModel file) {
		ResourceSet resourceSet = EcoreUtil2.getResourceSet(file);
		for (QDLQuery query : EcoreUtil2.getAllContentsOfType(file, QDLQuery.class)) {
			String outputPath = BasicIQLTypeUtils.getIQLOutputPath()+File.separator+QUERIES_DIR+File.separator+query.getSimpleName();
			cleanUpDir(outputPath);
			
			Collection<Resource> resources = createNecessaryIQLFiles(resourceSet, outputPath,query);
			generateJavaFiles(resources);
			deleteResources(resources);
			
			copyAndMoveUserEditiedFiles(query, outputPath);
			compileJavaFiles(outputPath, createClassPathEntries(resourceSet, resources));
			
			IQDLQuery qdlQuery = loadQuery(query,resources);
						
			ISession session = OdysseusRCPPlugIn.getActiveSession();
			ITenant tenant = UserManagementProvider.getDefaultTenant();
			IDataDictionary dd = DataDictionaryProvider.getDataDictionary(tenant);
			
			String script = generator.createOdysseusScript(qdlQuery, dd, session);
			QDLServiceBinding.getExecutor().addQuery(script, "OdysseusScript", OdysseusRCPPlugIn.getActiveSession(), Context.empty());
		
			cleanUpDir(outputPath);
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
	protected Collection<String> createClassPathEntries(ResourceSet set, Collection<Resource> resources) {
		Collection<String> result = super.createClassPathEntries(set, resources);
		
		IJavaProject project = javaProjectProvider.getJavaProject(set);
		if (project != null) {
			try {
				IPath path = project.getOutputLocation();
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IFolder folder = root.getFolder(path);
				result.add(folder.getLocation().toFile().getAbsolutePath());
			} catch (JavaModelException e) {
				throw new QueryParseException("error while creating classpath entries : " +e.getMessage(),e);
			}
		}
		
		return result;
	}
}
