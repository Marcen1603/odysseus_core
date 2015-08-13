package de.uniol.inf.is.odysseus.iql.qdl.ui.parser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
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
import org.eclipse.xtext.ui.util.ResourceUtil;

import com.google.common.io.Files;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.ui.BasicIQLUiModule;
import de.uniol.inf.is.odysseus.iql.basic.ui.parser.IIQLUiParser;
import de.uniol.inf.is.odysseus.iql.qdl.parser.QDLParser;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.service.QDLServiceBinding;
import de.uniol.inf.is.odysseus.iql.qdl.types.query.IQDLQuery;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class QDLUiParser extends QDLParser implements IIQLUiParser{


	@Inject
	private IJavaProjectProvider javaProjectProvider;
	
	@Inject
	public QDLUiParser(QDLTypeFactory typeFactory, QDLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

	@Override
	public void parse(IQLModel file) {
		ResourceSet resourceSet = EcoreUtil2.getResourceSet(file);
		for (QDLQuery query : EcoreUtil2.getAllContentsOfType(file, QDLQuery.class)) {
			String outputPath = getIQLOutputPath()+QUERIES_DIR+File.separator+query.getSimpleName();
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
		}
	}
	
	protected void copyAndMoveUserEditiedFiles(QDLQuery query, String path) {
		copyAndMoveUserEditiedFiles(query, path, query.getSimpleName());
		for (EObject obj : getUserDefinedTypes(query)) {
			if (obj instanceof IQLClass) {
				copyAndMoveUserEditiedFiles(obj, path, ((IQLClass) obj).getSimpleName());
			} else if (obj instanceof IQLInterface) {
				copyAndMoveUserEditiedFiles(obj, path, ((IQLClass) obj).getSimpleName());
			}
		}
	}
	
	protected void copyAndMoveUserEditiedFiles(EObject obj, String path, String name) {
		IFile file = ResourceUtil.getFile(obj.eResource());
		if (file != null && file.exists()) {
			IProject project = file.getProject();
			if (project != null && project.exists()) {
				URI uri = project.getLocationURI().relativize(file.getParent().getLocationURI());
				File from = new File(project.getLocation().toString()+File.separator+BasicIQLUiModule.EDIT_FOLDER+File.separator+uri.toString()+File.separator+name+".java");
				File to = new File(path+File.separator+uri.toString()+File.separator+name+".java");
				if (from.exists() && to.exists()) {
					try {
						Files.copy(from, to);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
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
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
