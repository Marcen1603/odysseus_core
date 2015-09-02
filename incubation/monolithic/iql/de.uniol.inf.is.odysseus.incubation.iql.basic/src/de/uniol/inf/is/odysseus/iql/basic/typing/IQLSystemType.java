package de.uniol.inf.is.odysseus.iql.basic.typing;

import org.eclipse.xtext.common.types.JvmDeclaredType;

public class IQLSystemType {
	
	private final JvmDeclaredType type;	
	
	private final Class<?> javaType;
	
	public IQLSystemType(JvmDeclaredType type, Class<?> javaType) {
		this.type = type;
		this.javaType = javaType;
	}

	public JvmDeclaredType getType() {
		return type;
	}
	
	
	public Class<?> getJavaType() {
		return javaType;
	}
}
