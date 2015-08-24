package de.uniol.inf.is.odysseus.iql.basic.typing;

import org.eclipse.xtext.common.types.JvmDeclaredType;

public class IQLSystemType {
	
	private final JvmDeclaredType iqlTypeDef;
	
	private final Class<?> javaType;
	
	
	public IQLSystemType(JvmDeclaredType iqlTypeDef, Class<?> javaType) {
		this.iqlTypeDef = iqlTypeDef;
		this.javaType = javaType;
	}


	public JvmDeclaredType getIqlTypeDef() {
		return iqlTypeDef;
	}


	public Class<?> getJavaType() {
		return javaType;
	}
	
	


}
