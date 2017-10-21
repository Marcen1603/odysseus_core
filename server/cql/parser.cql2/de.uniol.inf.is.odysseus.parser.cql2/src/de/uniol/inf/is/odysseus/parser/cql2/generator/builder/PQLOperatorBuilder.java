package de.uniol.inf.is.odysseus.parser.cql2.generator.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AccessAOSourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MapParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;

public class PQLOperatorBuilder {

	private final Logger LOGGER = LoggerFactory.getLogger(PQLOperatorBuilder.class);
	
	private StringBuilder builder;

	public PQLOperatorBuilder() {

	}

	@SuppressWarnings({ "unchecked", "rawtypes"})
	private List<Parameter> getParameters(final Class<?> type) {
		final List<Parameter> parameters = new ArrayList<>();
		final Class<? extends Annotation> annotation = de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter.class;
		final Class<? extends Annotation> setterAnnot= GetParameter.class;
		
		Class<?> klass = type;
		while (klass != Object.class) {
			final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
			for (final Method method : allMethods) {
				if (method.isAnnotationPresent(annotation)) {
					de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter annotInstance = (de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter) method.getAnnotation(annotation);
					parameters.add(new Parameter(annotInstance.name(), annotInstance.optional(), annotInstance.type()));
//					parameters.add(new Pair(annotInstance.name(), annotInstance.type()));
				} else if (method.isAnnotationPresent(setterAnnot)) {
					GetParameter annotInstance = (GetParameter) method.getAnnotation(setterAnnot);
					parameters.add(new Parameter(annotInstance.name(), false, StringParameter.class));
				}
			}
			klass = klass.getSuperclass();
		}
//		StringBuilder builder = new StringBuilder();
//		for(Pair<String, Class<? extends IParameter>> par : parameters) {
//			builder.append(par.getE1()).append("\n");
//		}
//		LOGGER.info("parameters=" + builder.toString());
		return parameters;
	}

	/** Builds the string representation of a specified PQL-operator. */
	public String build(final Class<?> operator, final Map<String, String> args) {
		builder = new StringBuilder();
		String name = null;
		for(Annotation annotation : new ArrayList<>(Arrays.asList(operator.getAnnotations()))) {
			if(annotation instanceof LogicalOperator) {
				LogicalOperator logicalOperator = (LogicalOperator) annotation;
				name = logicalOperator.name();
			}
		}
		
		if(name != null) {
			buildPrefix(name);
			buildArguments(operator, args);
			buildSuffix(args.get("input"));
		} else {
			throw new IllegalArgumentException("logical operator could not be found: " + operator.toString());
		}
		String operatorString = builder.toString();
		LOGGER.info(operatorString);
		return operatorString;
	}

	private StringBuilder buildPrefix(String name) {
		builder.setLength(0);
		builder.append(name);
		builder.append("(");
		builder.append("{");
		return builder;
	}

	private StringBuilder buildSuffix(String string) {
		if (string != null) {
			builder.append("}");
			builder.append("," + string);
		} else {
			builder.append("}");
		}
		builder.append(")");
		return builder;
	}

	@SuppressWarnings("rawtypes")
	private StringBuilder buildArguments(final Class<?> operator, Map<String, String> map) {
		List<Parameter> parameters = new ArrayList<>(getParameters(operator));
		List<Parameter> mandatoryParameters = new ArrayList<>();
		for (Parameter parameter : parameters) {
			final Class<? extends IParameter> type = parameter.type;
			if(!parameter.optional && !mandatoryParameters.contains(parameter)) {
				mandatoryParameters.add(parameter);
			}
			for (Map.Entry<String, String> entry : map.entrySet()) {
				final String name = parameter.name.toLowerCase();
				final String givenName = entry.getKey().toLowerCase();
				if(name.equals(givenName))
				if (!name.equals("input")) {
					String value = getValue(type, map.get(name));
					if (value != null) {
						builder.append(name + "=" + value + ",");
						mandatoryParameters.remove(parameter);
					}
				}
			}
		}
		
		if (!mandatoryParameters.isEmpty()) {
			builder = new StringBuilder();
			for (Parameter parameter : mandatoryParameters) {
				if (parameter.name != null && parameter.name != "") {
					builder.append(parameter.name).append("\n");
				}
			}
			throw new IllegalArgumentException(operator.getSimpleName() + " misses parameters: " + builder.toString());
		}
		
		builder.deleteCharAt(builder.toString().length() - 1);
		return builder;
	}

	@SuppressWarnings("rawtypes")
	private String getValue(Class<? extends IParameter> type, String value) {
		
		LOGGER.info("getValue");
		
		if (value == null)
			return value;
		
		final String typename = type.getSimpleName();
		
		// get type names from odysseus IParameter-classes
		final String string = new StringParameter().getClass().getSimpleName();
		final String bool = new BooleanParameter().getClass().getSimpleName();
		final String list = new ListParameter<>(null).getClass().getSimpleName();
		final String map = new MapParameter<>(null, null).getClass().getSimpleName();
		final String integer = new IntegerParameter().getClass().getSimpleName();
		final String option = new OptionParameter().getClass().getSimpleName();
		final String schema = new CreateSDFAttributeParameter().getClass().getSimpleName();
		final String accessao = new AccessAOSourceParameter().getClass().getSimpleName();
		final String ressource = new ResourceParameter().getClass().getSimpleName();
		final String expressions = new NamedExpressionParameter().getClass().getSimpleName();
		final String aggregations = new AggregateItemParameter().getClass().getSimpleName();
		final String resolved = new ResolvedSDFAttributeParameter().getClass().getSimpleName();
		final String predicate = new PredicateParameter().getClass().getSimpleName();
		final String enumm = new EnumParameter().getClass().getSimpleName();
		final String window = new TimeParameter().getClass().getSimpleName();
		// evaluate type parameter type and return correct format
		if (typename.equals(string) 
			|| typename.equals(accessao) 
		    || typename.equals(resolved) 
		    || typename.equals(predicate) 
		    || typename.equals(enumm) 
		    || typename.equals(bool)
		    || typename.equals(ressource)) {
				return "'" + value + "'";
		} else if (typename.equals(list) 
				|| typename.equals(map)
				|| typename.equals(option)
				|| typename.equals(schema)
				|| typename.equals(expressions)
				|| typename.equals(aggregations)
				|| typename.equals(window)) {
			return "[" + value + "]";
		} else if (typename.equals(integer)) {
			return value;
		} else {
			return "'" + value + "'"; 
		}
	}
	
	class Parameter {
		
		final String name;
		final boolean optional;
		@SuppressWarnings("rawtypes")
		final Class<? extends IParameter> type;

		@SuppressWarnings("rawtypes")
		public Parameter(String name, boolean optional, Class<? extends IParameter> type) {
			this.name = name;
			this.optional = optional;
			this.type = type;
		}

	}
	
}
