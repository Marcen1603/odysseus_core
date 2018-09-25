package de.uniol.inf.is.odysseus.core.metadata;

abstract public class AbstractMetaAttribute implements IMetaAttribute {
	
	private static final long serialVersionUID = -7497027906886410189L;

	@Override
	public IMetaAttribute createInstance() {
		try {
			return this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Cannot create Metadata of type "+this);
		}
	}
	
	@Override
	abstract public IMetaAttribute clone();

}
