package de.uniol.inf.is.odysseus.iql.basic.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.eclipse.osgi.framework.internal.core.AbstractBundle;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import com.google.inject.Provider;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLNamespace;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

@SuppressWarnings("restriction")
public abstract class AbstractIQLExecutor<F extends IIQLTypeDictionary, U extends IIQLTypeUtils> implements IIQLExecutor{
	
	protected static final String JAVA_VERSION = "1.7";
	
	
	@Inject 
	protected Provider<ResourceSet> resourceSetProvider;
	
	@Inject
	protected IGenerator generator;
	
	@Inject
	protected JavaIoFileSystemAccess fsa;	
	
	@Inject
	protected IOutputConfigurationProvider outputConfigurationProvider;
	
	
	@Inject
	protected IQLQualifiedNameConverter converter;
	
	protected U typeUtils;
	protected F typeDictionary;

	public AbstractIQLExecutor(F typeDictionary, U typeUtils) {
		this.typeDictionary = typeDictionary;
		this.typeUtils = typeUtils;
	}
	
	
	protected void cleanUpDir(String dir) {
		try {
			FileUtils.deleteDirectory(new File(dir));
		} catch (Exception e) {
		}
	}
			
	protected void deleteResources(Collection<Resource> resources) {
		for (Resource res : resources) {
			try {
				res.delete(new HashMap<>());
			} catch (IOException e) {
			}
		}
	}
	
	protected Collection<Resource> createNecessaryIQLFiles(ResourceSet resourceSet, String outputPath, EObject element) {
		Collection<EObject> userDefinedTypes = getUserDefinedTypes(element);
		userDefinedTypes.add(element);
		Map<IQLModel, StringBuilder> fileBuilders = new HashMap<>();
		for (EObject type : userDefinedTypes) {
			IQLModel containerFile = EcoreUtil2.getContainerOfType(type, IQLModel.class);
			StringBuilder builder = fileBuilders.get(containerFile);
			if (builder == null) {
				builder = new StringBuilder();
				for (IQLNamespace namepace : containerFile.getNamespaces()) {
					builder.append(getNodeText(namepace)+System.lineSeparator());
				}
				fileBuilders.put(containerFile, builder);
			} 
			builder.append(getNodeText(type)+System.lineSeparator());
		}
		Collection<Resource> resources = new HashSet<>();
		for (Entry<IQLModel, StringBuilder> entry : fileBuilders.entrySet()) {
			String text = entry.getValue().toString();
			String fileName = getFileName(entry.getKey());
			String folder = getFolder(entry.getKey());
			StringBuilder pathBuilder = new StringBuilder();
			pathBuilder.append(outputPath);
			if (folder != null && folder.length() > 0) {
				pathBuilder.append(File.separator+folder);
			}
			pathBuilder.append(File.separator+fileName);
			resources.add(createIQLFile(pathBuilder.toString(), text, resourceSet));
		}
		return resources;
	}
	
	protected String getFolder(IQLModel file) {
		return "";
	}
	
	protected String getFileName(IQLModel file) {
		URI uri = file.eResource().getURI();		
		return uri.lastSegment();
	}
	
	private String getNodeText(EObject object) {
		INode node = NodeModelUtils.getNode(object);
		return NodeModelUtils.getTokenText(node);
	}
	
	protected Set<EObject> getUserDefinedTypes(EObject element) {
		Set<EObject> userDefinedTypes = new HashSet<>();
		for (JvmTypeReference typeRef : EcoreUtil2.getAllContentsOfType(element, JvmTypeReference.class)) {
			JvmType type = typeUtils.getInnerType(typeRef, false);			
			if (typeUtils.isUserDefinedType(type, false)) {
				userDefinedTypes.add(type);
				userDefinedTypes.addAll(getUserDefinedTypes(type));
			} 
		}
		return userDefinedTypes;
	}
	
	
	private Resource createIQLFile(String path, String text, ResourceSet resourceSet) {
		File file = new File(path);
		file.getParentFile().mkdirs();
		try(PrintWriter writer = new PrintWriter(file)) {
			writer.print(text);
		} catch (FileNotFoundException e) {
			throw new QueryParseException("error while creating iql file: "+e.getMessage(),e);
		}
		return resourceSet.getResource(URI.createURI(file.toURI().toString()), true);
	}
	
	protected void generateJavaFiles(Collection<Resource> resources) {
		for (Resource res : resources) {
			generateJavaFiles(res);
		}
	}

	private void generateJavaFiles(Resource res) {
		URI uri = res.getURI();
		uri = uri.trimSegments(1);		
		String outputPath = uri.toFileString();
		fsa.setOutputPath(outputPath);
		generator.doGenerate(res, fsa);
	}
	
	protected void compileJavaFiles(String path, Collection<String> classPathEntries) {
		String classPath = "";
		for (String cpe : classPathEntries) {
			classPath = classPath+cpe+File.pathSeparatorChar;
		}
		BatchCompiler.compile("-"+JAVA_VERSION+" -classpath "+classPath+" "+path,new PrintWriter(System.out),new PrintWriter(System.err),null);
	}
	
	protected Collection<String> createClassPathEntries(ResourceSet set, Collection<Resource> resources) {
		Collection<Bundle> bundles = typeDictionary.getRequiredBundles();	
		
		Collection<String> entries = new ArrayList<>();		
		for (Bundle bundle : bundles) {
			File file = getPluginDir(bundle);
			if (file != null) {
				File binFolder = new File(file.getAbsolutePath()+File.separator+"bin");
				if (binFolder.exists()) {
					entries.add(file.getAbsolutePath()+File.separator+"bin");
				}
				entries.add(file.getParentFile().getAbsolutePath());
				if (bundle instanceof AbstractBundle) {
					try {
						String[] classPathEntries = ((AbstractBundle) bundle).getBundleData().getClassPath();
						for (String e : classPathEntries) {
							entries.add(file.getAbsolutePath()+File.separator+e);
						}
					} catch (BundleException e) {
						throw new QueryParseException("error while adding classpath entries of bundle " +bundle.getSymbolicName()+": "+e.getMessage(),e);
					}								
				}
			}
		}	
		
		
		return entries;	
	}
	
	
	private File getPluginDir(Bundle bundle) {
		try {
			URL url = FileLocator.toFileURL(FileLocator.find(bundle, new Path(""), null));
			return new File(url.toURI());
		} catch (Exception e) {
			return null;
		}
	}


}
