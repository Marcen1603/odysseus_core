package de.uniol.inf.is.odysseus.iql.odl.ui.executor;

import java.io.File;
import java.util.Collection;

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



import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.basic.ui.executor.BasicIQLUiExecutor;
import de.uniol.inf.is.odysseus.iql.basic.ui.executor.IIQLUiExecutor;
import de.uniol.inf.is.odysseus.iql.basic.ui.typing.BasicIQLUiTypeUtils;
import de.uniol.inf.is.odysseus.iql.odl.executor.ODLExecutor;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;

public class ODLUiExecutor extends ODLExecutor implements IIQLUiExecutor{

	@Inject
	private IJavaProjectProvider javaProjectProvider;
	
	@Inject
	public ODLUiExecutor(IODLTypeDictionary typeDictionary, IODLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}
	

	@Override
	protected String getFolder(IQLModel file) {
		return BasicIQLUiTypeUtils.getOutputFolder(file.eResource());
	}

	@Override
	public void parse(IQLModel model) {	
		ResourceSet resourceSet = EcoreUtil2.getResourceSet(model);

		for (ODLOperator operator : EcoreUtil2.getAllContentsOfType(model, ODLOperator.class)) {
			String outputPath = BasicIQLTypeUtils.getIQLOutputPath()+File.separator+OPERATORS_DIR+File.separator+operator.getSimpleName();
			
			cleanUpDir(outputPath);
			
			Collection<Resource> resources = createNecessaryIQLFiles(resourceSet, outputPath,operator);
			generateJavaFiles(resources);
			deleteResources(resources);
			
			copyAndMoveUserEditiedFiles(operator, outputPath);
			compileJavaFiles(outputPath, createClassPathEntries(EcoreUtil2.getResourceSet(operator), resources));
			loadOperator(operator, resources);			
			
			if (!isPersistent(operator)){
				cleanUpDir(outputPath);
			}
		}
	}
	
	protected void copyAndMoveUserEditiedFiles(ODLOperator operator, String path) {
		BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(operator, path, operator.getSimpleName()+"AO");
		BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(operator, path, operator.getSimpleName()+"AORule");
		BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(operator, path, operator.getSimpleName()+"PO");
		for (EObject obj : getUserDefinedTypes(operator)) {
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
