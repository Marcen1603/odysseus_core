package de.uniol.inf.is.odysseus.iql.basic.ui.executor;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.xtext.common.types.access.jdt.IJavaProjectProvider;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.executor.BasicIQLExecutor;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLUiExecutor extends BasicIQLExecutor implements IIQLUiExecutor {

	@Inject
	private IJavaProjectProvider javaProjectProvider;
	
	@Inject
	public BasicIQLUiExecutor(BasicIQLTypeDictionary typeDictionary,BasicIQLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}

	@Override
	public void parse(IQLModel model) {
		
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
