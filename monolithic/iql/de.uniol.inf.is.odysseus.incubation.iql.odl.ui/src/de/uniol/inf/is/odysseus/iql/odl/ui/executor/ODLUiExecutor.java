package de.uniol.inf.is.odysseus.iql.odl.ui.executor;

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

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
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
	
	private static final Logger LOG = LoggerFactory.getLogger(ODLExecutor.class);

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

		List<ODLOperator> operators = EcoreUtil2.getAllContentsOfType(model, ODLOperator.class);
		if (operators.size() == 0) {
			throw new QueryParseException("No operators found");
		}	
		for (ODLOperator operator : operators) {
			String outputPath = getOperatorPath(operator);
						
			cleanUpDir(outputPath);	
			
			Collection<IQLModelElement> resources = getModelElementsToCompile(resourceSet, outputPath,operator);
			
			generateJavaFiles(resources, outputPath);
			
			copyAndMoveUserEditiedFiles(operator, outputPath);
			compileJavaFiles(outputPath, createClassPathEntries(EcoreUtil2.getResourceSet(operator)));
			
			loadOperator(operator, resourceSet);	
			
			LOG.info("Adding operator "+operator.getSimpleName()+" using ODL done.");								
		}
	}
	
	protected void copyAndMoveUserEditiedFiles(ODLOperator operator, String path) {
		BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(operator, path, operator.getSimpleName()+"AO");
		BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(operator, path, operator.getSimpleName()+"AORule");
		BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(operator, path, operator.getSimpleName()+"PO");
		Collection<EObject> userDefinedTypes = new HashSet<>();
		collectUserDefinedTypes(operator, userDefinedTypes);
		for (EObject obj : userDefinedTypes) {
			if (obj instanceof IQLClass) {
				BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(obj, path, ((IQLClass) obj).getSimpleName());
			} else if (obj instanceof IQLInterface) {
				BasicIQLUiExecutor.copyAndMoveUserEditiedFiles(obj, path, ((IQLClass) obj).getSimpleName());
			}
		}
	}
	

	@Override
	protected Collection<URL> createClassloaderURLs(ODLOperator operator, ResourceSet resourceSet) {
		Collection<URL> urls = super.createClassloaderURLs(operator, resourceSet);
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
				throw new QueryParseException("error while creating classpath entries : " +e.getMessage(),e);
			}
		}
		
		return result;
	}

}
