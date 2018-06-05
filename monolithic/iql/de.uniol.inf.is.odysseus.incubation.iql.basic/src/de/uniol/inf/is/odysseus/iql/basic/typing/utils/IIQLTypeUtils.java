package de.uniol.inf.is.odysseus.iql.basic.typing.utils;


import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;


public interface IIQLTypeUtils {
	
	
	JvmTypeReference createTypeRef(JvmType typeRef);
	JvmTypeReference createTypeRef(String name, Notifier context);
	JvmTypeReference createTypeRef(Class<?> javaType, Notifier context);
	
	JvmType getInnerType(JvmTypeReference typeRef, boolean array);
		
	String getLongName(JvmTypeReference typeRef, boolean array);
	String getLongName(JvmType type, boolean array);

	String getShortName(JvmTypeReference typeRef, boolean array);
	String getShortName(JvmType type, boolean array);
	
	int getArraySize(JvmType type);	
	int getArrayDim(JvmTypeReference type);	

	boolean isArray(JvmTypeReference typeRef);
	boolean isUserDefinedType(JvmTypeReference typeRef, boolean array);
	boolean isUserDefinedType(JvmType type, boolean array);	
	
	boolean isByte(JvmTypeReference typeRef, boolean wrapper);
	boolean isShort(JvmTypeReference typeRef, boolean wrapper);
	boolean isInt(JvmTypeReference typeRef, boolean wrapper);
	boolean isLong(JvmTypeReference typeRef, boolean wrapper);
	boolean isFloat(JvmTypeReference typeRef, boolean wrapper);
	boolean isDouble(JvmTypeReference typeRef, boolean wrapper);
	boolean isCharacter(JvmTypeReference typeRef, boolean wrapper);

	boolean isByte(JvmTypeReference typeRef);
	boolean isShort(JvmTypeReference typeRef);
	boolean isInt(JvmTypeReference typeRef);
	boolean isLong(JvmTypeReference typeRef);
	boolean isFloat(JvmTypeReference typeRef);
	boolean isDouble(JvmTypeReference typeRef);
	boolean isCharacter(JvmTypeReference typeRef);

	boolean isString(JvmTypeReference typeRef);	
	boolean isBoolean(JvmTypeReference typeRef);
	
	boolean isVoid(JvmTypeReference typeRef);

	boolean isPrimitive(JvmTypeReference parameterType);
	boolean isPrimitive(JvmType type);
	
	boolean isSetter(JvmOperation method);
	boolean isGetter(JvmOperation method);
	
	String getNameWithoutSetterPrefix(JvmOperation method);
	String getNameWithoutGetterPrefix(JvmOperation method);
	JvmTypeReference getComponentType(JvmTypeReference arrayTypeRef);
	
	JvmTypeReference getWrapper(JvmTypeReference valueType, Notifier context);


}
