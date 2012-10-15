package de.uniol.inf.is.odysseus.core.metadata;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractMetaAttributeContainer<T extends IMetaAttribute> implements
		IMetaAttributeContainer<T> {
	
	private boolean inOrder;
	private Map<String, Object> additionalContent = new HashMap<String, Object>();
	
	private static final long serialVersionUID = 1480009485404803793L;

	@Override
	public boolean isInOrder() {
		return inOrder;
	}
	
	@Override
	public void setInOrder(boolean inOrder) {
		this.inOrder = inOrder;
	}
	
	@Override
	public Object getMetadata(String name){
		return additionalContent.get(name);
	}
	
	@Override
	public void setMetadata(String name, Object content){
		additionalContent.put(name, content);
	}

	abstract public AbstractMetaAttributeContainer<T> clone();

	
}
