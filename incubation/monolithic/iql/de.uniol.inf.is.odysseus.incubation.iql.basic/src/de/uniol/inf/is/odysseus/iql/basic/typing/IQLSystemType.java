package de.uniol.inf.is.odysseus.iql.basic.typing;

import org.eclipse.xtext.common.types.JvmGenericType;

public class IQLSystemType {
	
	private final JvmGenericType iqlTypeDef;
	
	private final Class<?> javaType;
	
	
	public IQLSystemType(JvmGenericType iqlTypeDef, Class<?> javaType) {
		this.iqlTypeDef = iqlTypeDef;
		this.javaType = javaType;
	}


	public JvmGenericType getIqlTypeDef() {
		return iqlTypeDef;
	}


	public Class<?> getJavaType() {
		return javaType;
	}
	
	


}
