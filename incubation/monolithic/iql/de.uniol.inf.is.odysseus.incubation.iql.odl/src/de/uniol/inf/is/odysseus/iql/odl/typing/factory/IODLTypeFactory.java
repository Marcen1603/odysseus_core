package de.uniol.inf.is.odysseus.iql.odl.typing.factory;

import java.util.Collection;

import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory;

public interface IODLTypeFactory extends IIQLTypeFactory{
	public static final String PARAMETER_KEY_TYPE = "keytype";
	public static final String PARAMETER_TYPE = "type";
	public static final String OPERATOR_OUTPUT_MODE = "outputMode";
	public static final String OPERATOR_PERSISTENT = "persistent";
	
	
	void addParameter(JvmTypeReference parameterType,JvmTypeReference parameterValueType);

	JvmTypeReference getParameterType(JvmTypeReference valueType);

	Collection<JvmTypeReference> getAllParameterValues();

	Collection<JvmTypeReference> getAllParameterTypes();

}
