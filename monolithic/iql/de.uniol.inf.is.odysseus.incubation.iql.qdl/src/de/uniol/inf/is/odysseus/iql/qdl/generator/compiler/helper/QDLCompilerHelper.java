package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.AbstractIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.IQDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary;
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils;

public class QDLCompilerHelper extends AbstractIQLCompilerHelper<IQDLLookUp, IQDLTypeDictionary, IQDLTypeUtils> implements IQDLCompilerHelper {
	
	@Inject
	public QDLCompilerHelper(IQDLLookUp lookUp, IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(lookUp, typeDictionary, typeUtils);
	}
	
	@Override
	public boolean isSource(JvmTypeReference typeRef) {
		return lookUp.isSource(typeRef);
	}
	
	
	@Override
	public boolean isSourceAttribute(JvmTypeReference typeRef, String name) {
		return typeDictionary.isSourceAttribute(typeUtils.getShortName(typeRef, false), name);
	}
	
	@Override
	public boolean isOperator(JvmTypeReference typeRef) {
		return lookUp.isOperator(typeRef);
	}
	
	@Override
	public boolean isParameter(String parameter, JvmTypeReference opType) {
		return typeDictionary.isParameter(parameter, typeUtils.getShortName(opType, false));
	}
	
	@Override
	public String getParameterOfGetter(JvmOperation method) {
		String methodName = method.getSimpleName();
		if (methodName.startsWith("is")) {
			return methodName.substring(2);
		} else {
			return methodName.substring(3);
		}
	}
	
	@Override
	public String getParameterOfSetter(JvmOperation method) {
		String methodName = method.getSimpleName();
		return methodName.substring(3);

	}
	
	@Override
	public boolean isParameterGetter(JvmOperation method, JvmTypeReference opType) {
		String methodName = method.getSimpleName();
		String parameter = getParameterOfGetter(method);
		return (methodName.startsWith("get") || methodName.startsWith("is")) && isParameter(parameter, opType);
	}
	
	@Override
	public boolean isParameterSetter(JvmOperation method,JvmTypeReference opType) {
		String methodName = method.getSimpleName();
		String parameter = getParameterOfSetter(method);
		return methodName.startsWith("set") && isParameter(parameter, opType);
	}

	@Override
	public String getLogicalOperatorName(JvmTypeReference typeRef) {
		String name = typeUtils.getShortName(typeRef, false);
		Class<? extends ILogicalOperator> op = typeDictionary.getLogicalOperator(name);
		if (op != null) {
			return op.getSimpleName();
		}
		return "";		
	}
	
	
	

}
