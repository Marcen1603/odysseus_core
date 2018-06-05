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
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement;
import de.uniol.inf.is.odysseus.iql.basic.executor.AbstractIQLExecutor;
import de.uniol.inf.is.odysseus.iql.basic.typing.OperatorsObservable;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.IODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLAO;
import de.uniol.inf.is.odysseus.iql.odl.typing.dictionary.IODLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.odl.typing.utils.IODLTypeUtils;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;

public class ODLExecutor extends AbstractIQLExecutor<IODLTypeDictionary, IODLTypeUtils> {
	
	public static final String OPERATORS_DIR = "operators";

	private static final Logger LOG = LoggerFactory.getLogger(ODLExecutor.class);

	@Inject
	private ParseHelper<IQLModel> parseHelper;
	
	
	@Inject
	public ODLExecutor(IODLTypeDictionary typeDictionary, IODLTypeUtils typeUtils) {
		super(typeDictionary, typeUtils);
	}
	
	protected String getOperatorPath(ODLOperator operator) {
		String operatorName = converter.toJavaString(typeUtils.getLongName(operator, false));
		if (isPersistent(operator)) {
			operatorName ="persistent."+operatorName;
		}
		String outputPath = BasicIQLTypeUtils.getIQLOutputPath()+File.separator+OPERATORS_DIR+File.separator+operatorName;
		return outputPath;
	}
	
	@Override
	public List<IExecutorCommand> parse(String text, IDataDictionary dd, ISession session, Context context) throws QueryParseException {		
		IQLModel model = null;
		try {
			model = parseHelper.parse(text);
		} catch (Exception e) {
			LOG.error("error while parsing operator text", e);
			throw new QueryParseException("error while parsing operator text: "+System.lineSeparator()+e.getMessage(),e);
		}
		
		List<ODLOperator> operators = EcoreUtil2.getAllContentsOfType(model, ODLOperator.class);
		if (operators.size() == 0) {
			throw new QueryParseException("No operators found");
		}			
		for (ODLOperator operator : operators) {
			String outputPath = getOperatorPath(operator);
			
			cleanUpDir(outputPath);
			
			ResourceSet resourceSet = EcoreUtil2.getResourceSet(operator);

			Collection<IQLModelElement> resources = getModelElementsToCompile(resourceSet, outputPath,operator);
			generateJavaFiles(resources, outputPath);
			
			compileJavaFiles(outputPath, createClassPathEntries(EcoreUtil2.getResourceSet(operator)));
			loadOperator(operator, resourceSet);
			
			LOG.info("Adding operator "+operator.getSimpleName()+" using ODL done.");
		}
		
		return new ArrayList<>();		
	}

	
	
	@SuppressWarnings("unchecked")
	protected void loadOperator(ODLOperator operator, ResourceSet resourceSet) {
		Collection<URL> urls = createClassloaderURLs(operator, resourceSet);		
		URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), ODLExecutor.class.getClassLoader());
		try {
			String operatorName = converter.toJavaString(typeUtils.getLongName(operator, false));		
			Class<? extends ILogicalOperator> ao = (Class<? extends ILogicalOperator>) Class.forName(operatorName+IODLCompilerHelper.AO_OPERATOR, true, classLoader);
			addLogicalOperator(ao);
			Class<?> rule = Class.forName(operatorName+IODLCompilerHelper.AO_RULE_OPERATOR, true, classLoader);
			TransformationInventory.getInstance().addRule((IRule<?, ?>) rule.newInstance());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			LOG.error("error while loading operator "+operator.getSimpleName(), e);
			throw new QueryParseException("error while loading operator "+operator.getSimpleName()+": "+System.lineSeparator()+e.getMessage(),e);
		}
	}
	
	protected Collection<URL> createClassloaderURLs(ODLOperator operator, ResourceSet resourceSet) {
		Collection<URL> urls = new HashSet<>();
		String outputPath = getOperatorPath(operator);
		File file = new File(outputPath);
		try {
			urls.add(file.toURI().toURL());
		} catch (MalformedURLException e1) {
			LOG.error("error while creating classloader urls "+operator.getSimpleName(), e1);
			throw new QueryParseException("error while creating classloader urls "+operator.getSimpleName()+": "+System.lineSeparator()+e1.getMessage(),e1);
		}	
		return urls;
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
			LOG.error("error while adding operator "+logicalOperatorAnnotation.name(), e);
			throw new QueryParseException("error while adding operator "+logicalOperatorAnnotation.name()+": "+System.lineSeparator()+e.getMessage(),e);
		}
		String doc = logicalOperatorAnnotation.doc();
        String url = logicalOperatorAnnotation.url();
        GenericOperatorBuilder builder = new GenericOperatorBuilder(curOp, logicalOperatorAnnotation.name(), parameters, logicalOperatorAnnotation.minInputPorts(),logicalOperatorAnnotation.maxInputPorts(), doc, url, logicalOperatorAnnotation.category());
        if (!isOverridingAllowed(builder.getName())) {
        	throw new QueryParseException("Overriding system operators is not possible");
        }
        OperatorBuilderFactory.removeOperatorBuilderByName(builder.getName());
        OperatorBuilderFactory.addOperatorBuilder(builder);
        OperatorsObservable.newOperatorBuilder(builder);
	}
	
	private static boolean isOverridingAllowed(String opName) {
		IOperatorBuilder builder = OperatorBuilderFactory.getOperatorBuilderType(opName);
		if (builder != null) {
			return IODLAO.class.isAssignableFrom(builder.getOperatorClass());
		}
		return true;
	}

	protected boolean isPersistent(ODLOperator operator) {
		if (operator.getMetadataList() != null) {
			for (IQLMetadata m : operator.getMetadataList().getElements()) {
				if (m.getName().equalsIgnoreCase(IODLTypeDictionary.OPERATOR_PERSISTENT)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public static void loadPersistentOperators() {
		String outputPath = BasicIQLTypeUtils.getIQLOutputPath()+File.separator+OPERATORS_DIR;
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
		if (operatorName.startsWith("persistent")) {
			operatorName = operatorName.substring(11, operatorName.length());
		} else {
			return;
		}
		Collection<URL> urls = new HashSet<>();
		try {
			urls.add(folder.toURI().toURL());
		} catch (MalformedURLException e1) {
			LOG.error("error while loading operator "+operatorName, e1);
			throw new QueryParseException("error while loading operator "+operatorName+": "+e1.getMessage(),e1);
		}			
		URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), ODLExecutor.class.getClassLoader());
		try {
			Class<? extends ILogicalOperator> ao = (Class<? extends ILogicalOperator>) Class.forName(operatorName+IODLCompilerHelper.AO_OPERATOR, true, classLoader);
			addLogicalOperator(ao);
			Class<?> rule = Class.forName(operatorName+IODLCompilerHelper.AO_RULE_OPERATOR, true, classLoader);
			TransformationInventory.getInstance().addRule((IRule<?, ?>) rule.newInstance());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			LOG.error("error while loading operator "+operatorName, e);
			throw new QueryParseException("error while loading operator "+operatorName+": "+e.getMessage(),e);
		}
	}

}
