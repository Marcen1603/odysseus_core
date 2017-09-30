package de.uniol.inf.is.odysseus.iql.basic.ui.generator;

import java.net.URI;

import javax.inject.Inject;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.ui.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.generator.BasicIQLGenerator;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.BasicIQLCompiler;
import de.uniol.inf.is.odysseus.iql.basic.generator.context.BasicIQLGeneratorContext;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.ui.BasicIQLUiModule;
import de.uniol.inf.is.odysseus.iql.basic.ui.typing.BasicIQLUiTypeUtils;

public class BasicIQLUiGenerator extends BasicIQLGenerator{

	private static final Logger LOG = LoggerFactory.getLogger(BasicIQLUiGenerator.class);
	
	@Inject
	public BasicIQLUiGenerator(BasicIQLGeneratorContext generatorContext,BasicIQLCompiler compiler) {
		super(generatorContext, compiler);
	}

	public void doGenerate(Resource input, IFileSystemAccess fsa) {
		URI outputFolder = BasicIQLUiTypeUtils.getOutputPath(input);
		if (fsa instanceof EclipseResourceFileSystemAccess2) {
			createEditFolder(input);
		}
		doGenerate(input, fsa, outputFolder);
	}
	

	public void doGenerate(IQLModelElement element, IFileSystemAccess fsa) {
		URI outputFolder = BasicIQLUiTypeUtils.getOutputPath(element.eResource());
		if (fsa instanceof EclipseResourceFileSystemAccess2) {
			createEditFolder( element.eResource());
		}
		doGenerate(element, fsa, outputFolder);
	}
	
	protected String getPackage(IQLModelElement element) {
		String packageName = BasicIQLUiTypeUtils.getPackage(element.eResource());
		if (packageName != null && packageName.length() > 0) {
			return packageName.replace(IQLQualifiedNameConverter.DELIMITER, ".");
		}
		return packageName;
	}

	
	public static void createEditFolder(Resource input) {		
		IFile file = ResourceUtil.getFile(input);
		if (file.exists()) {
			IProject project = file.getProject();
			if (project != null) {
				IFolder folder = project.getFolder(BasicIQLUiModule.EDIT_FOLDER);
				if (!folder.exists()) {
					try {
						folder.create(false, true, null);
					} catch (CoreException e) {
						LOG.error("Could not create edit folder", e);
					}
				}
				String outputFolder = BasicIQLUiTypeUtils.getOutputFolder(input);
				folder = folder.getFolder(outputFolder);
				if (!folder.exists()) {
					try {
						mkdirs(folder);
						folder.create(false, true, null);
					} catch (CoreException e) {
						LOG.error("Could not create output folder", e);
					}
				}				
			}
		}
	}
	
	public static void mkdirs(IFolder folder) throws CoreException {
		IContainer container = folder.getParent();
		if (!container.exists()) {
			mkdirs((IFolder) container);
		}
		folder.create(false, true, null);
	}

}
