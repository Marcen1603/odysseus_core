package de.uniol.inf.is.odysseus.iql.odl.parser;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
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

















import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.GenericOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.basic.parser.AbstractIQLParser;
import de.uniol.inf.is.odysseus.iql.basic.typing.OperatorsObservable;
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory;
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;



public class ODLParser extends AbstractIQLParser<ODLTypeFactory, ODLTypeUtils> {
	
	private static final String LANGUAGE_NAME = "de.uniol.inf.is.odysseus.iql.odl.ODL";
	protected static final String OPERATORS_DIR = "operators";

	@Inject
	public ODLParser(ODLTypeFactory typeFactory, ODLTypeUtils typeUtils) {
		super(typeFactory, typeUtils);
	}

	
	
	@SuppressWarnings("unchecked")
	protected void loadOperator(ODLOperator operator, Collection<Resource> resources) {
		try {
			Collection<URL> urls = new HashSet<>();
			for (Resource res : resources) {
				URI uri = res.getURI();
				uri = uri.trimSegments(1);		
				String outputPath = uri.toFileString();
				urls.add(new File(outputPath).toURI().toURL());
			}			
			URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
			Class<? extends ILogicalOperator> ao = (Class<? extends ILogicalOperator>) Class.forName(operator.getSimpleName()+ODLCompilerHelper.AO_OPERATOR, true, classLoader);
			addLogicalOperator(ao);
			Class<?> rule = Class.forName(operator.getSimpleName()+ODLCompilerHelper.AO_RULE_OPERATOR, true, classLoader);
			TransformationInventory.getInstance().addRule((IRule<?, ?>) rule.newInstance());
			
		} catch (Exception e) {
			throw new QueryParseException("error while loading operator " +operator.getSimpleName(),e);
		} 	
	}
	
	private void addLogicalOperator(Class<? extends ILogicalOperator> curOp) {
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
			String doc = logicalOperatorAnnotation.doc();
            String url = logicalOperatorAnnotation.url();
            GenericOperatorBuilder builder = new GenericOperatorBuilder(curOp, logicalOperatorAnnotation.name(), parameters, logicalOperatorAnnotation.minInputPorts(),logicalOperatorAnnotation.maxInputPorts(), doc, url, logicalOperatorAnnotation.category());
            OperatorBuilderFactory.removeOperatorBuilderByName(builder.getName());
            OperatorBuilderFactory.addOperatorBuilder(builder);
            OperatorsObservable.newOperatorBuilder(builder);
		} catch (Exception e) {
			throw new QueryParseException("error while adding operator " +curOp.getClass().getSimpleName(),e);
		}

	}

	@Override
	protected String getLanguageName() {
		return LANGUAGE_NAME;
	}


	@Override
	public List<IExecutorCommand> parse(String text, IDataDictionary dd,
			ISession session, Context context) throws QueryParseException {
		return new ArrayList<>();		
	}


}
