package de.uniol.inf.is.odysseus.parser.cql2.generator.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;

public abstract class AbstractPQLOperatorBuilder {
	
	abstract public String build(Class<?> operator, Map<String, String> args);
	
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

		@Override
		public String toString() {
			return "\n[name=" + this.name + ", optional=" + optional + ", type=" + type.getSimpleName() + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Parameter))
				return false;
			return ((Parameter) obj).name.equals(this.name);
		}

	}
	
	protected List<Parameter> getParameters(final Class<?> type) {
		final List<Parameter> parameters = new ArrayList<>();
		final Class<? extends Annotation> annotation = de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter.class;
		final Class<? extends Annotation> setterAnnot = GetParameter.class;

		Class<?> klass = type;
		while (klass != Object.class) {
			final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
			Parameter parameter = null;
			for (final Method method : allMethods) {
				if (method.isAnnotationPresent(annotation)) {
					de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter annotInstance = (de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter) method
							.getAnnotation(annotation);

					parameter = foo(method, annotInstance.name(), annotInstance.type(), annotInstance.optional(),
							annotInstance.isList());

					// String name = annotInstance.name();
					// Class<? extends IParameter<?>> type2 = null;
					//
					//
					//
					// if (name.equals("")) {
					// name = method.getName().replaceAll("get", "").replaceAll("set", "");
					// if (method.getReturnType().getSimpleName().equals("List")) {
					// type2 = (Class<? extends IParameter<?>>) new ListParameter(null).getClass();
					// }
					// parameter = new Parameter(name, annotInstance.optional(), type2);
					// } else {
					// parameter = new Parameter(annotInstance.name(), annotInstance.optional(),
					// annotInstance.type());
					// }
				} else if (method.isAnnotationPresent(setterAnnot)) {
					GetParameter annotInstance = (GetParameter) method.getAnnotation(setterAnnot);
					parameter = foo(method, annotInstance.name(), null, false, false);
					// Class<? extends IParameter<?>> type2 = null;
					// if (method.getReturnType().getSimpleName().equals("List")) {
					// type2 = (Class<? extends IParameter<?>>) new ListParameter(null).getClass();
					// }
					// parameter = new Parameter(annotInstance.name().replace("set", ""), false,
					// type2);
				}

				if (parameter != null && !parameter.name.trim().equals("") && parameter.type != null) {
					if (!parameters.contains(parameter)) {
						parameters.add(parameter);
					}
				}
				parameter = null;
			}
			klass = klass.getSuperclass();
		}
		Collections.sort(parameters, (a, b) -> a.name.compareToIgnoreCase(b.name));
		return parameters;
	}

	@SuppressWarnings({ "rawtypes" })
	private Parameter foo(Method method, String name, Class<? extends IParameter> type, boolean optional,
			boolean isList) {
		Class<? extends IParameter> parType = type;
		String parName = name;

		if (parName != null) {
			if (parName.equals("") || (parName.contains("get") || parName.contains("set"))) {
				parName = method.getName().replaceAll("get", "").replaceAll("set", "");
			}
		}

		if (method.getReturnType().getSimpleName().toLowerCase().contains("list")) {
			parType = new ListParameter<>(null).getClass();
		}

		if (isList) {
			parType = new ListParameter<>(null).getClass();
		}

		return new Parameter(parName.toLowerCase(), optional, parType);
	}

}
