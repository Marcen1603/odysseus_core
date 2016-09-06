package de.uniol.inf.is.odysseus.iql.basic.linking;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmExecutable;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.IIQLExpressionEvaluator;
import de.uniol.inf.is.odysseus.iql.basic.exprevaluator.TypeResult;
import de.uniol.inf.is.odysseus.iql.basic.lookup.IIQLLookUp;
import de.uniol.inf.is.odysseus.iql.basic.scoping.IQLQualifiedNameConverter;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;

public class SimpleIQLMethodFinder implements IIQLMethodFinder {

	@Inject
	private IIQLTypeUtils typeUtils;

	@Inject
	private IIQLLookUp lookUp;
	
	@Inject
	private IIQLExpressionEvaluator evaluator;
	
	@Inject
	private IQLQualifiedNameConverter converter;
	
	private static final Logger LOG = LoggerFactory.getLogger(SimpleIQLMethodFinder.class);

	@Override
	public String createExecutableID(JvmExecutable exe){
		StringBuilder builder = new StringBuilder();
		if (exe.getSimpleName() != null) {
			builder.append(exe.getSimpleName());
		} 
		for (JvmFormalParameter parameter : exe.getParameters()) {
			builder.append(typeUtils.getLongName(parameter.getParameterType(), true));
		}
		return builder.toString();
	}	

	@Override
	public JvmExecutable findConstructor(Collection<JvmExecutable> constructors, List<IQLExpression> arguments) {
		Collection<JvmExecutable> matches = new HashSet<>();
		for (JvmExecutable exe : constructors) {
			if (compareParametersByTypeName(exe.getParameters(), arguments)) {
				matches.add(exe);
			}
		}
		if (matches.size() == 1) {
			return matches.iterator().next();
		} else if (matches.size() > 1) {
			return null;
		}
		
		for (JvmExecutable exe : constructors) {
			if (compareParameters(exe.getParameters(), arguments)) {
				matches.add(exe);
			}
		}
		if (matches.size() == 1) {
			return matches.iterator().next();
		} else if (matches.size() > 1) {
			return null;
		}
		for (JvmExecutable exe : constructors) {
			if (compareParametersWithCasts(exe.getParameters(), arguments)) {
				matches.add(exe);
			}
		}
		if (matches.size() == 1) {
			return matches.iterator().next();
		}
		return null;
	}
	
	private boolean compareParametersByTypeName(EList<JvmFormalParameter> parameters, List<IQLExpression> arguments) {
		if (parameters.size() == arguments.size()){
			int i = 0;
			for (JvmFormalParameter parameter : parameters) {
				TypeResult typeResult = evaluator.eval(arguments.get(i++), parameter.getParameterType());
				if (typeResult.hasError()) {
					LOG.error("Could not evaluate expression. "+typeResult.getDiagnostic());
					return false;
				} else if (!typeResult.isNull()) {
					String targetName = typeUtils.getLongName(parameter.getParameterType(), true);
					String typeName = typeUtils.getLongName(typeResult.getRef(), true);
					if (!targetName.equalsIgnoreCase(typeName)) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	private boolean compareParameters(EList<JvmFormalParameter> parameters, List<IQLExpression> arguments) {
		if (parameters.size() == arguments.size()){
			int i = 0;
			for (JvmFormalParameter parameter : parameters) {
				TypeResult typeResult = evaluator.eval(arguments.get(i++), parameter.getParameterType());
				if (typeResult.hasError()) {
					LOG.error("Could not evaluate expression. "+typeResult.getDiagnostic());
					return false;
				} else if (!typeResult.isNull() && !lookUp.isAssignable(parameter.getParameterType(), typeResult.getRef())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private boolean compareParametersWithCasts(EList<JvmFormalParameter> parameters, List<IQLExpression> arguments) {
		if (parameters.size() == arguments.size()){
			int i = 0;
			for (JvmFormalParameter parameter : parameters) {
				TypeResult typeResult = evaluator.eval(arguments.get(i++), parameter.getParameterType());
				if (typeResult.hasError()) {
					LOG.error("Could not evaluate expression. "+typeResult.getDiagnostic());
					return false;
				} else if (!typeResult.isNull() && !lookUp.isAssignable(parameter.getParameterType(), typeResult.getRef()) && !lookUp.isCastable(parameter.getParameterType(), typeResult.getRef())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public JvmOperation findMethod(Collection<JvmOperation> methods,String name, List<IQLExpression> arguments) {
		Collection<JvmOperation> matches = new HashSet<>();		
		for (JvmOperation method : methods) {
			if (method.getSimpleName().equalsIgnoreCase(name) && compareParametersByTypeName(method.getParameters(), arguments)) {
				matches.add(method);				
			}			
		}
		if (matches.size() == 1) {
			return matches.iterator().next();
		} else if (matches.size() > 1) {
			return null;
		}
		
		for (JvmOperation method : methods) {
			if (method.getSimpleName().equalsIgnoreCase(name) && compareParameters(method.getParameters(), arguments)) {
				matches.add(method);				
			}			
		}
		if (matches.size() == 1) {
			return matches.iterator().next();
		} else if (matches.size() > 1) {
			return null;
		}
		for (JvmOperation method : methods) {
			if (method.getSimpleName().equalsIgnoreCase(name) && compareParametersWithCasts(method.getParameters(), arguments)) {
				matches.add(method);				
			}			
		}
		if (matches.size() == 1) {
			return matches.iterator().next();
		}
		return null;
	}
	
	@Override
	public JvmOperation findMethod(Collection<JvmOperation> methods, String name) {
		Collection<JvmOperation> matches = new HashSet<>();	
		
		for (JvmOperation method : methods) {
			if (method.getSimpleName().equalsIgnoreCase(name) && method.getParameters().size() == 0) {
				matches.add(method);				
			}
		}
		
		if (matches.size() == 1) {
			return matches.iterator().next();
		}
		return null;
	}
	

	@Override
	public JvmOperation findMethod(Collection<JvmOperation> methods, String name, int args, boolean hasReturnType) {
		Collection<JvmOperation> matches = new HashSet<>();	
		
		for (JvmOperation method : methods) {			
			if (method.getSimpleName().equalsIgnoreCase(name) && method.getParameters().size() == args) {
				if (hasReturnType && method.getReturnType() != null && !(converter.toJavaString(typeUtils.getLongName(method.getReturnType(), false)).equalsIgnoreCase(Void.class.getCanonicalName()))) {
					matches.add(method);				
				} else if (!hasReturnType && (method.getReturnType() == null || converter.toJavaString(typeUtils.getLongName(method.getReturnType(), false)).equalsIgnoreCase(Void.class.getCanonicalName()))) {
					matches.add(method);				
				}
			}
		}
		
		if (matches.size() == 1) {
			return matches.iterator().next();
		}
		return null;
	}

}
