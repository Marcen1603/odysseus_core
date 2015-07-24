package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper;

import javax.inject.Inject;

import org.eclipse.xtext.common.types.JvmTypeReference;

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
	
	public String getParameterSetter(String parameter, JvmTypeReference opType) {
		return factory.getParameterSetterName(parameter, factory.getShortName(opType, false));
	}
	
	public String getParameterGetter(String parameter, JvmTypeReference opType) {
		return factory.getParameterGetterName(parameter, factory.getShortName(opType, false));
	}
	
	public boolean isParameterGetter(IQLMethodSelection m,JvmTypeReference opType) {
		String methodName = m.getMethod();
		String parameter = methodName.substring(3);
		return methodName.startsWith("get") && isParameter(parameter, opType);
	}
	
	public boolean isParameterSetter(IQLMethodSelection m,JvmTypeReference opType) {
		String methodName = m.getMethod();
		String parameter = methodName.substring(3);
		return methodName.startsWith("set") && isParameter(parameter, opType);
	}
	
	public String getParameterGetter(IQLMethodSelection m,JvmTypeReference opType) {
		return getParameterGetter(m.getMethod().substring(3), opType);
	}
	
	public String getParameterSetter(IQLMethodSelection m,JvmTypeReference opType) {
		return getParameterSetter(m.getMethod().substring(3), opType);

	}
	
	
	

}
