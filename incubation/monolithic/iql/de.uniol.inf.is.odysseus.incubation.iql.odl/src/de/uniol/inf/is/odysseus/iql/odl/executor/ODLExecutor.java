package de.uniol.inf.is.odysseus.iql.odl.executor;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;








































import javax.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;































import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.junit4.util.ParseHelper;





import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.GenericOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMetadata;
import de.uniol.inf.is.odysseus.iql.basic.executor.AbstractIQLExecutor;
import de.uniol.inf.is.odysseus.iql.basic.typing.OperatorsObservable;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLModel;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.factory.IODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;



public class ODLExecutor extends AbstractIQLExecutor<IODLTypeFactory, IODLTypeUtils> {
	
	protected static final String OPERATORS_DIR = "operators";

	
	@Inject
	private ParseHelper<ODLModel> parseHelper;
	
	
	@Inject
	public ODLExecutor(IODLTypeFactory typeFactory, IODLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

	
	
	@SuppressWarnings("unchecked")
	protected void loadOperator(ODLOperator operator, Collection<Resource> resources) {
		Collection<URL> urls = new HashSet<>();
		for (Resource res : resources) {
			URI uri = res.getURI();
			uri = uri.trimSegments(1);		
			String outputPath = uri.toFileString();
			try {
				urls.add(new File(outputPath).toURI().toURL());
			} catch (MalformedURLException e) {
				throw new QueryParseException("error while loading operator "+operator.getSimpleName()+": "+e.getMessage(),e);
			}
		}			
		URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), ODLExecutor.class.getClassLoader());
		try {
			Class<? extends ILogicalOperator> ao = (Class<? extends ILogicalOperator>) Class.forName(operator.getSimpleName()+IODLCompilerHelper.AO_OPERATOR, true, classLoader);
			addLogicalOperator(ao);
			Class<?> rule = Class.forName(operator.getSimpleName()+IODLCompilerHelper.AO_RULE_OPERATOR, true, classLoader);
			TransformationInventory.getInstance().addRule((IRule<?, ?>) rule.newInstance());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new QueryParseException("error while loading operator "+operator.getSimpleName()+": "+e.getMessage(),e);
		}
	}
	
	private static void addLogicalOperator(Class<? extends ILogicalOperator> curOp) {
		Map<Parameter, Method> parameters = new HashMap<Parameter, Method>();
		LogicalOperator logicalOperatorAnnotation = curOp.getAnnotation(LogicalOperator.class);

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(curOp, Object.class);
			for (PropertyDescriptor curProperty : beanInfo.getPropertyDescriptors()) {
				Method writeMethod = curProperty.getWriteMethod();
				if (writeMethod != null && writeMethod.isAnnotationPresent(Parameter.class)) {
					Parameter parameterAnnotation = writeMethod.getAnnotation(Parameter.class);
					parameters.put(parameterAnnotation, writeMethod);
				}
			}
		} catch (IntrospectionException e) {
			throw new QueryParseException("error while adding operator "+logicalOperatorAnnotation.name()+": "+e.getMessage(),e);
		}
		String doc = logicalOperatorAnnotation.doc();
        String url = logicalOperatorAnnotation.url();
        GenericOperatorBuilder builder = new GenericOperatorBuilder(curOp, logicalOperatorAnnotation.name(), parameters, logicalOperatorAnnotation.minInputPorts(),logicalOperatorAnnotation.maxInputPorts(), doc, url, logicalOperatorAnnotation.category());
        if (OperatorBuilderFactory.containsOperatorBuilderType(builder.getName()) && !isODLOperator(OperatorBuilderFactory.getOperatorBuilderType(builder.getName()))) {
        	throw new RuntimeException("Overriding system operators is not possible");
        }
        OperatorBuilderFactory.removeOperatorBuilderByName(builder.getName());
        OperatorBuilderFactory.addOperatorBuilder(builder);
        OperatorsObservable.newOperatorBuilder(builder);
	}

	private static boolean isODLOperator(IOperatorBuilder operatorBuilderType) {
		return operatorBuilderType.getOperatorClass().getPackage()==null;
	}


	@Override
	public List<IExecutorCommand> parse(String text, IDataDictionary dd, ISession session, Context context) throws QueryParseException {		
		ODLModel model = null;
		try {
			model = parseHelper.parse(text);
		} catch (Exception e) {
			throw new QueryParseException("error while parsing operator text: "+e.getMessage(),e);
		}
		
		for (ODLOperator operator : EcoreUtil2.getAllContentsOfType(model, ODLOperator.class)) {
			String outputPath = getIQLOutputPath()+OPERATORS_DIR+File.separator+operator.getSimpleName();
			
			cleanUpDir(outputPath);
			
			Collection<Resource> resources = createNecessaryIQLFiles(EcoreUtil2.getResourceSet(operator), outputPath,operator);
			generateJavaFiles(resources);
			deleteResources(resources);
			
			compileJavaFiles(outputPath, createClassPathEntries(EcoreUtil2.getResourceSet(operator), resources));
			loadOperator(operator, resources);
			
			if (!isPersistent(operator)){
				cleanUpDir(outputPath);
			}
		}
		
		return new ArrayList<>();		
	}
	
	protected boolean isPersistent(ODLOperator operator) {
		if (operator.getMetadataList() != null) {
			for (IQLMetadata m : operator.getMetadataList().getElements()) {
				if (m.getName().equalsIgnoreCase(IODLTypeFactory.OPERATOR_PERSISTENT)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public static void loadPersistentOperators() {
		String outputPath = getIQLOutputPath()+OPERATORS_DIR;
		File folder = new File(outputPath);
		if (folder.exists()) {
		    for (File file : folder.listFiles()) {
		        if (file.isDirectory()) {
		        	loadPersistentOperator(file);
		        } 
		    }
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void loadPersistentOperator(File folder) {
		String operatorName = folder.getName();
		Collection<URL> urls = getURLs(folder, operatorName);
		try {
			urls.add(folder.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new QueryParseException("error while loading operator "+operatorName+": "+e.getMessage(),e);
		}
		URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), ODLExecutor.class.getClassLoader());
		try {
			Class<? extends ILogicalOperator> ao = (Class<? extends ILogicalOperator>) Class.forName(operatorName+IODLCompilerHelper.AO_OPERATOR, true, classLoader);
			addLogicalOperator(ao);
			Class<?> rule = Class.forName(operatorName+IODLCompilerHelper.AO_RULE_OPERATOR, true, classLoader);
			TransformationInventory.getInstance().addRule((IRule<?, ?>) rule.newInstance());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new QueryParseException("error while loading operator "+operatorName+": "+e.getMessage(),e);
		}
	}



	private static Collection<URL> getURLs(File folder, String operatorName) {
		Collection<URL> urls = new HashSet<>();
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				try {
					urls.add(file.toURI().toURL());
				} catch (MalformedURLException e) {
					throw new QueryParseException("error while loading operator "+operatorName+": "+e.getMessage(),e);
				}
				urls.addAll(getURLs(file, operatorName));
			}
		}
		return urls;
	}
	
	


}
