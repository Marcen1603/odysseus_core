package de.uniol.inf.is.odysseus.iql.basic.ui.typing;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.ui.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;

public class BasicIQLUiTypeUtils extends BasicIQLTypeUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(BasicIQLUiTypeUtils.class);

	@Override
	protected String getLongNameOfUserDefinedType(JvmGenericType type) {
		String packageName = BasicIQLUiTypeUtils.getPackage(type);
		if (packageName != null && packageName.length() > 0) {
			return packageName+IQLQualifiedNameConverter.DELIMITER+type.getSimpleName();
		} else {
			return type.getIdentifier();
		}
	}

	public static String getPackage(JvmGenericType type) {
		String packageName = type.getPackageName();
		if (packageName == null) {
			packageName = getPackage(type.eResource());
		}
		return packageName;		
	}
	

	public static String getPackage(Resource res) {
		URI outputFolder = getOutputPath(res);
		if (outputFolder != null) {
			String[] segments = outputFolder.getPath().split("/");
			StringBuilder packageBuilder = new StringBuilder();
			for (int i = 0; i<segments.length; i++) {
				if (i > 0) {
					packageBuilder.append(IQLQualifiedNameConverter.DELIMITER);
				}
				packageBuilder.append(segments[i]);
			}
			return packageBuilder.toString();
		} else {
			return null;
		}
	}


	public static URI getOutputPath(Resource eResource) {
		if (eResource.getURI().isPlatform()) {
			IFile file = null;
			try {
				file = ResourceUtil.getFile(eResource);
			}catch (Exception e) {
				LOG.error("", e);
			}
			if (file != null && file.exists()) {
				IProject project = file.getProject();
				if (project != null) {
					IFolder srcFolder = getSourceFolder(file);
					if (srcFolder != null && file.getParent() != srcFolder) {
						URI uri = srcFolder.getLocationURI().relativize(file.getParent().getLocationURI());
						return uri;
					} else if (file.getParent() != srcFolder) {
						URI uri = project.getLocationURI().relativize(file.getParent().getLocationURI());
						return uri;
					}				
				}
			} 
		} else {
			File iqlFile = new File(OdysseusConfiguration.instance.getHomeDir()+IQL_DIR);			
			URI iqlUri = iqlFile.toURI();
			URI resUri = URI.create(eResource.getURI().toString());
			URI uri = iqlUri.relativize(resUri);

			String[] segments = uri.getPath().split("/");
			StringBuilder builder = new StringBuilder();
			for (int i = 2; i<segments.length-1; i++) {
				if (i > 2) {
					builder.append("/");
				}
				builder.append(segments[i]);
			}
			return URI.create(builder.toString());
		}		
		return null;
	} 
	
	private static IFolder getSourceFolder(IFile file) {
		IProject project = file.getProject();
		IJavaProject javaProject = null;
		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				javaProject = JavaCore.create(file.getProject());
			}
		} catch (CoreException e) {
		}
		if (javaProject != null) {
			Set<IPath> srcFolders = getJavaProjectSourceDirectories(javaProject);
			IContainer parent = file.getParent();
			while (parent instanceof IFolder) {
				IFolder folder = (IFolder) parent;
				IPath path = folder.getProjectRelativePath();
				if (srcFolders.contains(path)) {
					return folder;
				}
				parent = folder.getParent();
			}
		}
		return null;
	}
	
	private static Set<IPath> getJavaProjectSourceDirectories (IJavaProject javaProject){
		Set<IPath> result = new HashSet<>();
		try {
			IClasspathEntry[] classpathEntries = javaProject.getResolvedClasspath(true);
	        for(int i = 0; i<classpathEntries.length;i++){
	            IClasspathEntry entry = classpathEntries[i];
	            if(entry.getContentKind() == IPackageFragmentRoot.K_SOURCE){
	                result.add(entry.getPath().makeRelativeTo(javaProject.getPath()));
	            }
	        }
		} catch (JavaModelException e) {
			LOG.error("error while getting source directories of java project", e);
		}
        return result;
	}

	public static String getOutputFolder(Resource input) {
		URI uri = getOutputPath(input);
		if (uri != null) {
			return uri.toString();
		}		
		return "";
	}
	
	

}
