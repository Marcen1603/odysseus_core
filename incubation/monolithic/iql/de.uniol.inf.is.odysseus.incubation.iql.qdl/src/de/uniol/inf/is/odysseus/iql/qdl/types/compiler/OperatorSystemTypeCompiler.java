package de.uniol.inf.is.odysseus.iql.qdl.types.compiler;

import java.util.List;

import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;

import de.uniol.inf.is.odysseus.iql.basic.typing.builder.IIQLSystemTypeCompiler;
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;

public class OperatorSystemTypeCompiler implements IIQLSystemTypeCompiler{
	
	private IQDLTypeDictionary typeDictionary;
	
	private IIQLTypeUtils typeUtils;
	
	public OperatorSystemTypeCompiler(IQDLTypeDictionary typeDictionary, IIQLTypeUtils typeUtils) {
		this.typeUtils = typeUtils;
		this.typeDictionary = typeDictionary;
	}

	
	@Override
	public String compileFieldSelection(JvmField field) {
		return null;
	}

	@Override
	public boolean compileFieldSelectionManually() {
		return false;
	}

	@Override
	public String compileMethodSelection(JvmOperation method, List<String> compiledArguments) {
		JvmDeclaredType type = (JvmDeclaredType) method.eContainer();
		if (isParameterGetter(method, type)) {
			String parameter = getParameterOfGetter(method);
			StringBuilder builder = new StringBuilder();
			builder.append("getParameter");
			builder.append("(");
			builder.append("\""+parameter+"\"");
			builder.append(")");			
			return builder.toString();
		} else if (isParameterSetter(method, type)) {
			String parameter = getParameterOfSetter(method);
			StringBuilder builder = new StringBuilder();
			builder.append("setParameter");
			builder.append("(");
			builder.append("\""+parameter+"\"");
			for (int i = 0; i<compiledArguments.size(); i++) {
				builder.append(", "+compiledArguments.get(i));
			}
			builder.append(")");			
			return builder.toString();
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append(method.getSimpleName());
			builder.append("(");
			for (int i = 0; i<compiledArguments.size(); i++) {
				if (i > 0) {
					builder.append(", ");
				}
				builder.append(compiledArguments.get(i));
			}
			builder.append(")");			
			return builder.toString();
		}
	}

	@Override
	public boolean compileMethodSelectionManually() {
		return true;
	}
	
	
	private boolean isParameter(String parameter, JvmDeclaredType opType) {
		return typeDictionary.isParameter(parameter, typeUtils.getShortName(opType, false));
	}
	
	private boolean isParameterGetter(JvmOperation method, JvmDeclaredType opType) {
		String methodName = method.getSimpleName();
		String parameter = getParameterOfGetter(method);
		return (methodName.startsWith("get") || methodName.startsWith("is")) && isParameter(parameter, opType);
	}
	
	private boolean isParameterSetter(JvmOperation method,JvmDeclaredType opType) {
		String methodName = method.getSimpleName();
		String parameter = getParameterOfSetter(method);
		return methodName.startsWith("set") && isParameter(parameter, opType);
	}
	
	private String getParameterOfGetter(JvmOperation method) {
		String methodName = method.getSimpleName();
		if (methodName.startsWith("is")) {
			return methodName.substring(2);
		} else {
			return methodName.substring(3);
		}
	}
	
	private String getParameterOfSetter(JvmOperation method) {
		String methodName = method.getSimpleName();
		return methodName.substring(3);

	}

}
