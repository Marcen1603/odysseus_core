package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodSelection;
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.AbstractIQLCompilerHelper;
import de.uniol.inf.is.odysseus.iql.qdl.lookup.QDLLookUp;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory;
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeUtils;

public class QDLCompilerHelper extends AbstractIQLCompilerHelper<QDLLookUp, QDLTypeFactory, QDLTypeUtils> {
	
	@Inject
	public QDLCompilerHelper(QDLLookUp lookUp, QDLTypeFactory typeFactory, QDLTypeUtils typeUtils) {
		super(lookUp, typeFactory, typeUtils);
	}
	
	public boolean isSource(JvmTypeReference typeRef) {
		return typeFactory.isSource(typeRef);
	}
	
	public boolean isSourceAttribute(JvmTypeReference typeRef, String name) {
		return typeFactory.isSourceAttribute(typeUtils.getShortName(typeRef, false), name);
	}
	
	public boolean isOperator(JvmTypeReference typeRef) {
		return typeFactory.isOperator(typeRef);
	}
	
	public boolean isParameter(String parameter, JvmTypeReference opType) {
		return typeFactory.isParameter(parameter, typeUtils.getShortName(opType, false));
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

	
	public String getLogicalOperatorName(JvmTypeReference typeRef) {
		String name = typeUtils.getShortName(typeRef, false);
		Class<? extends ILogicalOperator> op = typeFactory.getLogicalOperator(name);
		if (op != null) {
			return op.getSimpleName();
		}
		return "";		
	}
	
	
	

}
