package de.uniol.inf.is.odysseus.iql.basic.typing.extension;

import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLExpression;

public interface IIQLTypeExtensionsDictionary {
	Collection<JvmField> getAllExtensionAttributes(JvmTypeReference typeRef, int[] visibilities);
	Collection<JvmOperation> getAllExtensionMethods(JvmTypeReference typeRef, int[] visibilities);

	boolean hasTypeExtensions(JvmTypeReference typeRef, String attribute);
	boolean hasTypeExtensions(JvmTypeReference typeRef, String method, IQLExpression argument);
	boolean hasTypeExtensions(JvmTypeReference typeRef, String method, List<IQLExpression> arguments);

	IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef, String attribute);
	IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef, String method, IQLExpression argument);
	IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef, String method, List<IQLExpression> arguments);

	JvmOperation getMethod(JvmTypeReference typeRef, String method, IQLExpression argument);
	JvmOperation getMethod(JvmTypeReference typeRef, String method, List<IQLExpression> arguments);

	IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef);
	boolean hasTypeExtensions(JvmTypeReference typeRef);
	void addTypeExtensions(IIQLTypeExtensions typeExtensions);
	void removeTypeExtensions(IIQLTypeExtensions typeExtensions);
	boolean ignoreFirstParameter(JvmOperation method);
}
