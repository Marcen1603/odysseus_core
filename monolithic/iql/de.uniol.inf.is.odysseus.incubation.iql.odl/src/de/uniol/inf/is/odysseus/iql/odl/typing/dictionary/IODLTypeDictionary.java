package de.uniol.inf.is.odysseus.iql.odl.typing.dictionary;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary;

public interface IODLTypeDictionary extends IIQLTypeDictionary{
	public static final String PARAMETER_KEY_TYPE = "keytype";
	public static final String PARAMETER_TYPE = "type";
	public static final String OPERATOR_OUTPUT_MODE = "outputMode";
	public static final String OPERATOR_PERSISTENT = "persistent";
	
	
	JvmTypeReference getParameterType(JvmTypeReference valueType, Notifier context);

	Collection<JvmTypeReference> getAllParameterValues(Notifier context);

	Collection<JvmTypeReference> getAllParameterTypes(Notifier context);
}
