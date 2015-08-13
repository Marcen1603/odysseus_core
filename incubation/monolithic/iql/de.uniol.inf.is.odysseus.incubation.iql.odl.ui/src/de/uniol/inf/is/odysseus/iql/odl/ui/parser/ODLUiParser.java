package de.uniol.inf.is.odysseus.iql.odl.ui.parser;

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

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.ui.BasicIQLUiModule;
import de.uniol.inf.is.odysseus.iql.basic.ui.parser.IIQLUiParser;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.parser.ODLParser;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;

public class ODLUiParser extends ODLParser implements IIQLUiParser{

	@Inject
	private IJavaProjectProvider javaProjectProvider;
	
	@Inject
	public ODLUiParser(ODLTypeFactory typeFactory, ODLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

	@Override
	public void parse(IQLModel model, IProject project) {	
		ResourceSet resourceSet = EcoreUtil2.getResourceSet(model);

		for (ODLOperator operator : EcoreUtil2.getAllContentsOfType(model, ODLOperator.class)) {
			String outputPath = getIQLOutputPath()+OPERATORS_DIR+File.separator+operator.getSimpleName();
			
			cleanUpDir(outputPath);
			
			Collection<Resource> resources = createNecessaryIQLFiles(resourceSet, outputPath,operator);
			generateJavaFiles(resources);
			deleteResources(resources);
			
			copyAndMoveUserEditiedFiles(operator, outputPath);
			compileJavaFiles(outputPath, createClassPathEntries(EcoreUtil2.getResourceSet(operator), resources));
			loadOperator(operator, resources);
		}
	}
	
	protected void copyAndMoveUserEditiedFiles(ODLOperator operator, String path) {
		copyAndMoveUserEditiedFiles(operator, path, operator.getSimpleName()+"AO");
		copyAndMoveUserEditiedFiles(operator, path, operator.getSimpleName()+"AORule");
		copyAndMoveUserEditiedFiles(operator, path, operator.getSimpleName()+"PO");
		for (EObject obj : getUserDefinedTypes(operator)) {
			if (obj instanceof IQLClass) {
				copyAndMoveUserEditiedFiles(obj, path, ((IQLClass) obj).getSimpleName());
			} else if (obj instanceof IQLInterface) {
				copyAndMoveUserEditiedFiles(obj, path, ((IQLClass) obj).getSimpleName());
			}
		}
	}
	
	protected void copyAndMoveUserEditiedFiles(EObject obj, String path, String name) {
		IFile file = ResourceUtil.getFile(obj.eResource());
		if (file.exists()) {
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
