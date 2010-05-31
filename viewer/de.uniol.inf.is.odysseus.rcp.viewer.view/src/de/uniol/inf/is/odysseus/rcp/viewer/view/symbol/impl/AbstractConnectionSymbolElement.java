package de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IConnectionView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.IConnectionSymbolElement;

public abstract class AbstractConnectionSymbolElement<C> implements IConnectionSymbolElement<C> {

	private boolean isEnabled = true;
	private IConnectionView<C> connection;
	
	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void setEnabled( boolean isEnabled ) {
		this.isEnabled = isEnabled;
	}
	
	@Override
	public void setConnectionView( IConnectionView<C> connView ) {
		connection = connView;
	}
	
	@Override
	public IConnectionView<C> getConnectionView() {
		return connection;
	}

}
