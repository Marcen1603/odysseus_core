package de.uniol.inf.is.odysseus.iql.basic.executor;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.generator.IIQLGenerator;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public abstract class AbstractIQLExecutor<F extends IIQLTypeDictionary, U extends IIQLTypeUtils>
		implements IIQLExecutor {

	protected static final String JAVA_VERSION = "1.7";

	@Inject
	protected Provider<ResourceSet> resourceSetProvider;

	@Inject
	protected IIQLGenerator generator;

	@Inject
	protected JavaIoFileSystemAccess fsa;

	@Inject
	protected IOutputConfigurationProvider outputConfigurationProvider;

	@Inject
	protected IQLQualifiedNameConverter converter;

	protected U typeUtils;

	protected F typeDictionary;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractIQLExecutor.class);

	public AbstractIQLExecutor(F typeDictionary, U typeUtils) {
		this.typeDictionary = typeDictionary;
		this.typeUtils = typeUtils;
	}

	protected void cleanUpDir(String dir) {
		try {
			FileUtils.deleteDirectory(new File(dir));
		} catch (Exception e) {
			LOG.warn("Could not delete directory " + dir);
		}
	}

	protected Collection<IQLModelElement> getModelElementsToCompile(ResourceSet resourceSet, String outputPath,
			EObject element) {
		Collection<EObject> userDefinedTypes = new HashSet<>();
		collectUserDefinedTypes(element, userDefinedTypes);
		userDefinedTypes.add(element);
		Collection<IQLModelElement> result = new HashSet<>();
		for (EObject obj : userDefinedTypes) {
			result.add((IQLModelElement) obj.eContainer());
		}
		return result;
	}

	//
	// protected Collection<IQLModelElement>
	// getModelElementsToCompile(ResourceSet resourceSet, String outputPath,
	// EObject element) {
	// Collection<IQLModelElement> result = new HashSet<>();
	// IQLModel model = EcoreUtil2.getContainerOfType(element, IQLModel.class);
	// for (IQLModelElement member : model.getElements()) {
	// result.add(member);
	// }
	// return result;
	// }

	protected String getFolder(IQLModel file) {
		return "";
	}

	protected String getFileName(IQLModel file) {
		URI uri = file.eResource().getURI();
		return uri.lastSegment();
	}

	protected void collectUserDefinedTypes(EObject element, Collection<EObject> userDefinedTypes) {
		for (JvmTypeReference typeRef : EcoreUtil2.getAllContentsOfType(element, JvmTypeReference.class)) {
			JvmType type = typeUtils.getInnerType(typeRef, false);
			if (typeUtils.isUserDefinedType(type, false) && !userDefinedTypes.contains(type)) {
				userDefinedTypes.add(type);
				collectUserDefinedTypes(type, userDefinedTypes);
			}
		}
	}

	protected void generateJavaFiles(Collection<IQLModelElement> modelElements, String outputPath) {
		for (IQLModelElement obj : modelElements) {
			generateJavaFiles(obj, outputPath);
		}
	}

	private void generateJavaFiles(IQLModelElement obj, String outputPath) {
		fsa.setOutputPath(outputPath);
		try {
			generator.doGenerate(obj, fsa);
		} catch (Exception e) {
			LOG.error("error while generating java files for " + obj.getInner().getSimpleName(), e);
			throw new QueryParseException("error while generating java files for " + obj.getInner().getSimpleName()
					+ ": " + System.lineSeparator() + e.getMessage(), e);
		}
	}

	protected void compileJavaFiles(String path, Collection<String> classPathEntries) {
		String classPath = "";
		for (String cpe : classPathEntries) {
			classPath = classPath + cpe + File.pathSeparatorChar;
		}
		StringWriter errWriter = new StringWriter();
		BatchCompiler.compile("-" + JAVA_VERSION + " -classpath " + classPath + " " + path, new PrintWriter(System.out),
				new PrintWriter(errWriter), null);
		String output = errWriter.toString();
		if (output != null && output.length() > 0) {
			System.err.print(output);
			throw new QueryParseException("error while compiling java files: " + System.lineSeparator() + output);
		}
	}

	protected Collection<String> createClassPathEntries(ResourceSet set) {
		Collection<Bundle> bundles = typeDictionary.getRequiredBundles();	
		
		Collection<String> entries = new ArrayList<>();		
		for (Bundle bundle : bundles) {
			entries.addAll(getBundleClassPathEntries(bundle));
		}			
		return entries;	
	}
	
	
	protected Collection<String> getBundleClassPathEntries(Bundle bundle) {
		Collection<String> entries = new ArrayList<>();		
		File file = getPluginDir(bundle);
		if (file != null) {
			File binFolder = new File(file.getAbsolutePath()+File.separator+"bin");
			if (binFolder.exists()) {
				entries.add(file.getAbsolutePath()+File.separator+"bin");
			}
			entries.add(file.getParentFile().getAbsolutePath());
			String[] classPathEntries = typeDictionary.getBundleClasspath(bundle);
			if (classPathEntries != null){
				for (String e : classPathEntries) {
					entries.add(file.getAbsolutePath()+File.separator+e);
				}
			}
		}
		return entries;
	}
	
	protected File getPluginDir(Bundle bundle) {
		try {
			URL url = FileLocator.toFileURL(FileLocator.find(bundle, new Path(""), null));
			return new File(url.toURI());
		} catch (Exception e) {
			return null;
		}
	}

}
