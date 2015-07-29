package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.AbstractIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;

public class QDLCompilerHelper extends AbstractIQLCompilerHelper<QDLLookUp, QDLTypeFactory> {
	
	@Inject
	public QDLCompilerHelper(QDLLookUp lookUp, QDLTypeFactory factory) {
		super(lookUp, factory);
	}
	
	public boolean isSource(JvmTypeReference typeRef) {
		return factory.isSource(typeRef);
	}
	
	public boolean isSourceAttribute(JvmTypeReference typeRef, String name) {
		return factory.isSourceAttribute(factory.getShortName(typeRef, false), name);
	}
	
	public boolean isOperator(JvmTypeReference typeRef) {
		return factory.isOperator(typeRef);
	}
	
	public boolean isParameter(String parameter, JvmTypeReference opType) {
		return factory.isParameter(parameter, factory.getShortName(opType, false));
	}
	
	public String getParameterOfGetter(IQLMethodSelection m) {
		String methodName = m.getMethod().getSimpleName();
		if (methodName.startsWith("is")) {
			return methodName.substring(2);
		} else {
			return methodName.substring(3);
		}
	}
	
	public String getParameterOfSetter(IQLMethodSelection m) {
		String methodName = m.getMethod().getSimpleName();
		return methodName.substring(3);

	}
	
	public boolean isParameterGetter(IQLMethodSelection m,JvmTypeReference opType) {
		String methodName = m.getMethod().getSimpleName();
		String parameter = getParameterOfGetter(m);
		return (methodName.startsWith("get") || methodName.startsWith("is")) && isParameter(parameter, opType);
	}
	
	public boolean isParameterSetter(IQLMethodSelection m,JvmTypeReference opType) {
		String methodName = m.getMethod().getSimpleName();
		String parameter = getParameterOfSetter(m);
		return methodName.startsWith("set") && isParameter(parameter, opType);
	}
	
//	public String getParameterSetter(String parameter, JvmTypeReference opType) {
//		return factory.getParameterSetterName(parameter, factory.getShortName(opType, false));
//	}
//	
//	public String getParameterGetter(String parameter, JvmTypeReference opType) {
//		return factory.getParameterGetterName(parameter, factory.getShortName(opType, false));
//	}
	
//	public String getParameterGetter(IQLMethodSelection m,JvmTypeReference opType) {
//		return getParameterGetter(m.getMethod().getSimpleName().substring(3), opType);
//	}
//	
//	public String getParameterSetter(IQLMethodSelection m,JvmTypeReference opType) {
//		return getParameterSetter(m.getMethod().getSimpleName().substring(3), opType);
//
//	}
	
	public String getLogicalOperatorName(JvmTypeReference typeRef) {
		String name = factory.getShortName(typeRef, false);
		Class<? extends ILogicalOperator> op = factory.getLogicalOperator(name);
		if (op != null) {
			return op.getSimpleName();
		}
		return "";		
	}
	
	
	

}
