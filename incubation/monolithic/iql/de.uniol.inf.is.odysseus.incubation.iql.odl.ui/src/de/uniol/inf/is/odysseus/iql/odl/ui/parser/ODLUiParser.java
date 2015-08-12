package de.uniol.inf.is.odysseus.iql.odl.ui.parser;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.access.jdt.IJavaProjectProvider;

import com.google.common.io.Files;

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
			
			copyAndMoveUserEditiedFiles(project, outputPath);
			compileJavaFiles(outputPath, createClassPathEntries(EcoreUtil2.getResourceSet(operator), resources));
			loadOperator(operator, resources);
		}
	}
	
	protected void copyAndMoveUserEditiedFiles(IProject project, String path) {
		IFolder folder = project.getFolder(BasicIQLUiModule.EDIT_FOLDER);
		if (folder.exists()) {
			copyAndMoveUserEditiedFiles(folder, folder, path);
		}		
	}
	
	protected void copyAndMoveUserEditiedFiles(IFolder rootFolder, IFolder folder, String path) {
		try {
			for (IResource res : folder.members()) {
				if (res instanceof IFolder) {
					copyAndMoveUserEditiedFiles(rootFolder, (IFolder)res, path);
				} else {
					File from = new File(res.getRawLocationURI());					
					java.net.URI uri = rootFolder.getRawLocationURI().relativize(res.getRawLocationURI());					
					File to = new File(path+File.separator+uri.toString());
					if (from.exists() && to.exists()) {
						Files.copy(from, to);
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
