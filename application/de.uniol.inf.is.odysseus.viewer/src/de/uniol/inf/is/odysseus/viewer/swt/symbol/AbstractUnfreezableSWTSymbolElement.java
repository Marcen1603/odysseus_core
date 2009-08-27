package de.uniol.inf.is.odysseus.viewer.swt.symbol;


public abstract class AbstractUnfreezableSWTSymbolElement<C> extends AbstractSWTSymbolElement<C> {

	@Override
	public void setEnabled( boolean isEnable ) {}
	

	@Override
	public boolean isEnabled() { return true; }
	
	@Override
	public void update( ) {}
}
