package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

public class UtmPointCreatorFactory extends AbstractFactory<UtmPointCreator, Integer>{

	private static final UtmPointCreatorFactory INSTANCE;
	
	public UtmPointCreatorFactory getInstance() {
		return INSTANCE;
	}
	
	
	private UtmPointCreatorFactory() { }
	
	@Override
	protected Integer convertKey(Integer key) {
		return key;
	}

	@Override
	protected UtmPointCreator createProduct(Integer convertedKey) {
		return new UtmPointCreator(convertedKey);
	}

}
