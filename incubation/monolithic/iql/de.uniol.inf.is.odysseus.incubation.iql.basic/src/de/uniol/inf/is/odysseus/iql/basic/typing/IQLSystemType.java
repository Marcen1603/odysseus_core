package de.uniol.inf.is.odysseus.iql.basic.typing;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDef;

public class IQLSystemType {
	
	private final IQLTypeDef iqlTypeDef;
	
	private final Class<?> javaType;
	
	
	public IQLSystemType(IQLTypeDef iqlTypeDef, Class<?> javaType) {
		this.iqlTypeDef = iqlTypeDef;
		this.javaType = javaType;
	}


	public IQLTypeDef getIqlTypeDef() {
		return iqlTypeDef;
	}


	public Class<?> getJavaType() {
		return javaType;
	}
	
	


}
