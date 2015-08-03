package de.uniol.inf.is.odysseus.iql.basic.typing.extension;

import java.util.Collection;

import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;

public interface IIQLTypeExtensionsFactory {
	boolean hasTypeExtensions(JvmTypeReference typeRef);
	IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef);
	Collection<JvmField> getAllExtensionAttributes(JvmTypeReference typeRef, int[] visibilities);
	Collection<JvmOperation> getAllExtensionMethods(JvmTypeReference typeRef, int[] visibilities);

	boolean hasTypeExtensions(JvmTypeReference typeRef, String attribute);
	boolean hasTypeExtensions(JvmTypeReference typeRef, String method, int args);

	
	IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef, String attribute);
	IIQLTypeExtensions getTypeExtensions(JvmTypeReference typeRef, String method, int args);
	
	void addTypeExtensions(IIQLTypeExtensions typeExtensions);
	void removeTypeExtensions(IIQLTypeExtensions typeExtensions);
}
