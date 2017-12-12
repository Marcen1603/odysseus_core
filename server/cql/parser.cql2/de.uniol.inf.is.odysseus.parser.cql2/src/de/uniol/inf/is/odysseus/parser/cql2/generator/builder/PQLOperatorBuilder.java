package de.uniol.inf.is.odysseus.parser.cql2.generator.builder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MapParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;

public class PQLOperatorBuilder extends AbstractPQLOperatorBuilder {

//	private final Logger LOGGER = LoggerFactory.getLogger(PQLOperatorBuilder.class);

	private StringBuilder builder;

	@Inject
	public PQLOperatorBuilder() {

	}

	/** Builds the string representation of a specified PQL-operator. */
	@Override
	public String build(final Class<?> operator, final Map<String, String> args) {
		builder = new StringBuilder();
		String name = null;
		for (Annotation annotation : new ArrayList<>(Arrays.asList(operator.getAnnotations()))) {
			if (annotation instanceof LogicalOperator) {
				LogicalOperator logicalOperator = (LogicalOperator) annotation;
				name = logicalOperator.name();
			}
		}

		if (name != null) {
			buildPrefix(name);
			buildArguments(operator, args);
			buildSuffix(args.get("input"));
		} else {
			throw new IllegalArgumentException("logical operator could not be found: " + operator.toString());
		}
		String operatorString = builder.toString().replaceAll("\\s+", "");
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
			if (!parameter.optional && !mandatoryParameters.contains(parameter)) {
				mandatoryParameters.add(parameter);
			}

			for (Map.Entry<String, String> entry : map.entrySet()) {
				final String name = parameter.name.toLowerCase().trim();
				final String givenName = entry.getKey().toLowerCase().trim();
				if (name.equals(givenName)) {
					if (!name.equals("input")) {
						String value = getValue(type, entry.getValue());
						if (value != null) {
							builder.append(name + "=" + value + ",");
							mandatoryParameters.remove(parameter);
						}
					}
				}
			}

		}

		if (!mandatoryParameters.isEmpty()) {
			builder = new StringBuilder();
			for (Parameter parameter : mandatoryParameters) {
				if (parameter.name != null && parameter.name != "") {
					builder.append(parameter.toString()).append("\n");
				}
			}
			throw new IllegalArgumentException(operator.getSimpleName() + " misses parameters: " + builder.toString());
		}

		builder.deleteCharAt(builder.toString().length() - 1);
		return builder;
	}

	@SuppressWarnings("rawtypes")
	private String getValue(Class<? extends IParameter> type, String value) {
		if (value == null)
			return value;

		final String typename = type.getSimpleName().toLowerCase();

		// get type names from odysseus IParameter-classes
//		final String string = new StringParameter().getClass().getSimpleName().toLowerCase();
//		final String bool = new BooleanParameter().getClass().getSimpleName().toLowerCase();
		final String list = new ListParameter<>(null).getClass().getSimpleName().toLowerCase();
		final String map = new MapParameter<>(null, null).getClass().getSimpleName().toLowerCase();
		final String integer = new IntegerParameter().getClass().getSimpleName().toLowerCase();
		final String longg = new LongParameter().getClass().getSimpleName().toLowerCase();
		final String option = new OptionParameter().getClass().getSimpleName().toLowerCase();
		final String schema = new CreateSDFAttributeParameter().getClass().getSimpleName().toLowerCase();
//		final String accessao = new AccessAOSourceParameter().getClass().getSimpleName().toLowerCase();
//		final String ressource = new ResourceParameter().getClass().getSimpleName().toLowerCase();
		final String expressions = new NamedExpressionParameter().getClass().getSimpleName().toLowerCase();
		final String aggregations = new AggregateItemParameter().getClass().getSimpleName().toLowerCase();
//		final String resolved = new ResolvedSDFAttributeParameter().getClass().getSimpleName().toLowerCase();
//		final String predicate = new PredicateParameter().getClass().getSimpleName().toLowerCase();
//		final String enumm = new EnumParameter().getClass().getSimpleName().toLowerCase();
		final String window = new TimeParameter().getClass().getSimpleName().toLowerCase();
		// evaluate type parameter type and return correct format
		if (typename.equals(list) || typename.equals(map) || typename.equals(option) || typename.equals(schema)
				|| typename.equals(expressions) || typename.equals(aggregations) || typename.equals(window)) {
			return "[" + value + "]";
		} else if (typename.equals(integer) || typename.equals(longg)) {
			return value;
		} else {
			return "'" + value + "'";
		}
	}

}
