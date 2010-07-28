package de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolElement;

public abstract class AbstractSymbolElement<C> implements ISymbolElement<C> {

	private boolean isEnabled;
		
	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void setEnabled( boolean isEnabled ) {
		this.isEnabled = isEnabled;
	}
	
	private INodeView<C> nodeView;

	@Override
	public void setNodeView( INodeView<C> nodeView ) {
		this.nodeView = nodeView;
	}
	
	@Override
	public INodeView<C> getNodeView() {
		return nodeView;
	}

}
