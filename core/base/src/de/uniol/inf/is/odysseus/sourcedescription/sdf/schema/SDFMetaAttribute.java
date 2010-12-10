package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class SDFMetaAttribute extends SDFAttribute {
	
	private static final long serialVersionUID = 8656969628309815890L;
	private Class<? extends IMetaAttribute> theclass; 
	
	
	public SDFMetaAttribute(Class<? extends IMetaAttribute> metaAttributeClass) {
		super(metaAttributeClass.getName());	
		this.theclass = metaAttributeClass;
	}


	public Class<? extends IMetaAttribute> getMetaAttributeClass() {
		return theclass;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SDFMetaAttribute){
			SDFMetaAttribute other = (SDFMetaAttribute)obj;
			if(other.getMetaAttributeClass().equals(this.getMetaAttributeClass())){
				return true;
			}
			return false;
		}else{
			return false;
		}		
	}
			

}
