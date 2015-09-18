package de.uniol.inf.is.odysseus.iql.basic.typing;

import org.eclipse.xtext.common.types.JvmGenericType;

public class IQLSystemType {
	
	private final JvmGenericType type;	
	
	private final Class<?> javaType;
	
	public IQLSystemType(JvmGenericType type, Class<?> javaType) {
		this.type = type;
		this.javaType = javaType;
	}

	public JvmGenericType getType() {
		return type;
	}
	
	
	public Class<?> getJavaType() {
		return javaType;
	}
}
