package de.uniol.inf.is.odysseus.iql.basic.typing.factory;

import java.util.Collection;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef;
import de.uniol.inf.is.odysseus.iql.basic.typing.IQLSystemType;




public interface IIQLTypeFactory {
		
	String getFileExtension();
	boolean isSystemFile(String fileName);
	IQLFile getSystemFile(String fileName);
	Collection<IQLFile> getSystemFiles();	
	ResourceSet getSystemResourceSet();
	
	Collection<JvmType> getVisibleTypes(Resource context);
	boolean isImportNeeded(JvmType type, String text);
	Collection<Bundle> getDependencies(IQLFile file);
	Collection<String> getImplicitImports();
	
	JvmTypeReference getTypeRef(Class<?> javaType);	
	JvmTypeReference getTypeRef(JvmType typeRef);	
	JvmTypeReference getTypeRef(String name);

	IQLSystemType addSystemType(IQLTypeDef typeDef, Class<?> javaType);
	void removeSystemType(String name);
	
	JvmType getInnerType(JvmTypeReference typeRef, boolean array);
	
	String getImportName(JvmType type);	
	String getSimpleName(JvmType type,  String text, boolean wrapper, boolean array);
	
	String getLongName(JvmTypeReference typeRef, boolean array);
	String getLongName(JvmType type, boolean array);
	String getLongName(JvmTypeReference typeRef, boolean array,	boolean parameterized);

	String getShortName(JvmTypeReference typeRef, boolean array);
	String getShortName(JvmType type, boolean array);
	
	boolean isArray(JvmTypeReference typeRef);
	boolean isUserDefinedType(JvmTypeReference typeRef, boolean array);
	boolean isUserDefinedType(JvmType type, boolean array);	
	
	boolean isByte(JvmTypeReference typeRef, boolean wrapper);
	boolean isShort(JvmTypeReference typeRef, boolean wrapper);
	boolean isInt(JvmTypeReference typeRef, boolean wrapper);
	boolean isLong(JvmTypeReference typeRef, boolean wrapper);
	boolean isFloat(JvmTypeReference typeRef, boolean wrapper);
	boolean isDouble(JvmTypeReference typeRef, boolean wrapper);
	
	boolean isByte(JvmTypeReference typeRef);
	boolean isShort(JvmTypeReference typeRef);
	boolean isInt(JvmTypeReference typeRef);
	boolean isLong(JvmTypeReference typeRef);
	boolean isFloat(JvmTypeReference typeRef);
	boolean isDouble(JvmTypeReference typeRef);
	
	boolean isString(JvmTypeReference typeRef);	
	boolean isBoolean(JvmTypeReference typeRef);
	
	boolean isVoid(JvmTypeReference typeRef);

	boolean isMap(JvmTypeReference typeRef);
	boolean isList(JvmTypeReference typeRef);
	boolean isClonable(JvmTypeReference typeRef);
	
	int getArraySize(JvmType type);	
	
}
