package de.uniol.inf.is.odysseus.iql.odl.ui.generator;

import java.io.File;
import java.net.URI;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.ui.util.ResourceUtil;

import de.uniol.inf.is.odysseus.iql.basic.ui.BasicIQLUiModule;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGenerator;
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.ODLCompiler;

public class ODLUiGenerator extends ODLGenerator{


	@Inject
	public ODLUiGenerator(ODLGeneratorContext generatorContext,	ODLCompiler compiler) {
		super(generatorContext, compiler);
	}

	@Override
	public void doGenerate(Resource input, IFileSystemAccess fsa) {
		String outputFolder = "";
		if (fsa instanceof EclipseResourceFileSystemAccess2) {
			outputFolder = getOutputFolder(input);
			createEditFolder(input);
		}
		doGenerate(input, fsa, outputFolder);
	}

	
	private void createEditFolder(Resource input) {
		IFile file = ResourceUtil.getFile(input);
		if (file.exists()) {
			IProject project = file.getProject();
			if (project != null) {
				IFolder folder = project.getFolder(BasicIQLUiModule.EDIT_FOLDER);
				if (!folder.exists()) {
					try {
						folder.create(false, true, null);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
				for (int i = 2; i < input.getURI().segmentCount()-1; i++) {
					String seg = input.getURI().segment(i);
					folder = folder.getFolder(seg);
					if (!folder.exists()) {
						try {
							folder.create(false, true, null);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private String getOutputFolder(Resource res) {
		IFile file = ResourceUtil.getFile(res);
		if (file.exists()) {
			IProject project = file.getProject();
			if (project != null && project.exists()) {
				URI uri = project.getLocationURI().relativize(file.getParent().getLocationURI());
				return uri.toString()+File.separator;
			}
		}
		return "";
	}


}
