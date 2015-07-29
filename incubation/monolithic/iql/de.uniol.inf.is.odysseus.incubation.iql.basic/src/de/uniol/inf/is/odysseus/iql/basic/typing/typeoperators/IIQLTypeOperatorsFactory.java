package de.uniol.inf.is.odysseus.iql.basic.typing.typeoperators;

import org.eclipse.xtext.common.types.JvmTypeReference;

public interface IIQLTypeOperatorsFactory {
	JvmTypeReference getTypeOperators(ITypeOperators typeOperators);

	ITypeOperators getTypeOperators(JvmTypeReference typeRef);
	ITypeOperators getTypeOperators(JvmTypeReference typeRef, String attribute);
	ITypeOperators getTypeOperators(JvmTypeReference typeRef, String method, int args);
	boolean hasTypeOperators(JvmTypeReference typeRef);
	boolean hasTypeOperators(JvmTypeReference typeRef, String attribute);
	boolean hasTypeOperators(JvmTypeReference typeRef, String method, int args);

	void addTypeOperators(ITypeOperators typeOperators);
	void removeTypeOperators(ITypeOperators typeOperators);
}
