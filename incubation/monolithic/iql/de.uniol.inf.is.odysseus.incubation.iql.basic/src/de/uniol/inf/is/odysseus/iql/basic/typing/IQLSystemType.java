package de.uniol.inf.is.odysseus.iql.basic.typing;

import org.eclipse.xtext.common.types.JvmDeclaredType;

public class IQLSystemType {
	
	private final JvmDeclaredType type;	
	
	public IQLSystemType(JvmDeclaredType type) {
		this.type = type;
	}

	public JvmDeclaredType getType() {
		return type;
	}
}
