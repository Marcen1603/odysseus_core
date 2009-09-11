package de.uniol.inf.is.odysseus.metadata.base;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;


public class Container<Type, MetaType extends IMetaAttribute> extends MetaAttributeContainer<MetaType> {
	
	private static final long serialVersionUID = -581770058118444611L;
	public Type cargo;

	public Container(Type cargo) {
		this.cargo = cargo;
	}
	
	public Container(Type cargo, MetaType metadata){
		this.cargo = cargo;
		this.setMetadata(metadata);
	}

	public Container(Container<Type, MetaType> name) {
		super(name);
		this.cargo = name.cargo;
	}

	@Override
	public Container<Type, MetaType> clone() {
		return new Container<Type, MetaType>(this);
	}
	
	public Type getCargo(){
		return this.cargo;
	}
	
	public void setType(Type c){
		this.cargo = c;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Container: " + this.cargo.toString() + "   <|>   " + this.getMetadata().toString());
		return sb.toString();
	}
}
