package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.SWTSymbolElement;


public abstract class UnfreezableSWTSymbolElement<C> extends SWTSymbolElement<C> {

	@Override
	public void setEnabled( boolean isEnable ) {}
	

	@Override
	public boolean isEnabled() { return true; }
	
	@Override
	public void update( ) {}
}
