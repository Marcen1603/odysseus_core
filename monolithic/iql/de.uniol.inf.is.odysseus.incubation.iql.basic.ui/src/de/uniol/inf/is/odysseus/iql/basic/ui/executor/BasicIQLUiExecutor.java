package de.uniol.inf.is.odysseus.iql.basic.ui.executor;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.jar.Manifest;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.xtext.common.types.access.jdt.IJavaProjectProvider;
import org.eclipse.xtext.ui.util.ResourceUtil;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.executor.BasicIQLExecutor;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.BasicIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.basic.ui.BasicIQLUiModule;
import de.uniol.inf.is.odysseus.iql.basic.ui.typing.BasicIQLUiTypeUtils;

public class BasicIQLUiExecutor extends BasicIQLExecutor implements IIQLUiExecutor {

	@Inject
	private IJavaProjectProvider javaProjectProvider;
	
	private static final Logger LOG = LoggerFactory.getLogger(BasicIQLUiExecutor.class);

	@Inject
	public BasicIQLUiExecutor(BasicIQLTypeDictionary typeDictionary,BasicIQLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}

	@Override
	public void parse(IQLModel model) {
		
	}
	
	@Override
	protected String getFolder(IQLModel file) {
		return BasicIQLUiTypeUtils.getOutputFolder(file.eResource());
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
			} catch (JavaModelException e) {
				LOG.error("error while creating classpath entries", e);
			}
		}
		
		return result;
	}
	
	public static Collection<String> readRequiredBundles(IJavaProject project) {	
		Collection<String> result = new HashSet<>();
		IResource res = project.getProject().findMember("META-INF"+File.separator+"MANIFEST.MF");
		if (res.exists()) {
			try {
				Manifest mf = new Manifest(res.getLocation().toFile().toURI().toURL().openStream());
				String bundles = mf.getMainAttributes().getValue(Constants.REQUIRE_BUNDLE);
				if (bundles != null) {
					for (String entry : bundles.split(",")) {					
						result.add(entry.split(";")[0]);
					}
				} 				
			} catch (IOException e) {
			}
		}
		return result;
	}
	
	public static Collection<String> readBundleClasspath(IJavaProject project) {	
		Collection<String> result = new HashSet<>();
		IResource res = project.getProject().findMember("META-INF"+File.separator+"MANIFEST.MF");
		if (res.exists()) {
			try {
				Manifest mf = new Manifest(res.getLocation().toFile().toURI().toURL().openStream());
				String bundleClasspath = mf.getMainAttributes().getValue(Constants.BUNDLE_CLASSPATH);
				if (bundleClasspath != null) {
					for (String entry : bundleClasspath.split(",")) {
						result.add(entry);
					}
				}				
			} catch (IOException e) {
			}
		}
		return result;
	}

	
	public static void copyAndMoveUserEditiedFiles(EObject obj, String path, String name) {
		IFile file = ResourceUtil.getFile(obj.eResource());
		if (file != null && file.exists()) {
			IProject project = file.getProject();
			if (project != null && project.exists()) {
				String outputFolder = BasicIQLUiTypeUtils.getOutputFolder(obj.eResource());
				
				StringBuilder fromBuilder = new StringBuilder();
				fromBuilder.append(project.getLocation().toString()+File.separator+BasicIQLUiModule.EDIT_FOLDER);
				if (outputFolder != null && outputFolder.length() > 0) {
					fromBuilder.append(File.separator+outputFolder);
				}
				fromBuilder.append(File.separator+name+".java");

				StringBuilder toBuilder = new StringBuilder();
				toBuilder.append(path);
				if (outputFolder != null && outputFolder.length() > 0) {
					toBuilder.append(File.separator+outputFolder);
				}
				toBuilder.append(File.separator+name+".java");
				
				File from = new File(fromBuilder.toString());
				File to = new File(toBuilder.toString());
				if (from.exists() && to.exists()) {
					try {
						Files.copy(from, to);
					} catch (IOException e) {
						throw new QueryParseException("error while moving user edited files : " +e.getMessage(),e);
					}
				}
			}
		}
	}

}
