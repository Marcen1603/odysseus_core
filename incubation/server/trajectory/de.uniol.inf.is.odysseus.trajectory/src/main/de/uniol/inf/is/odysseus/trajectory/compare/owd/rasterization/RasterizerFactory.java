package de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractSimpleFactory;

public class RasterizerFactory extends AbstractSimpleFactory<IRasterizer> {

	private final static RasterizerFactory INSTANCE = new RasterizerFactory();
	
	public static RasterizerFactory getInstance() {
		return INSTANCE;
	}
	
	private RasterizerFactory() { }
	
	@Override
	protected IRasterizer create() {
		return new AdvancedBresenhamRasterizer();
	}

}
