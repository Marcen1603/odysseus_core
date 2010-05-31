package de.uniol.inf.is.odysseus.rcp.viewer.view.manage.impl;

import java.util.Collection;

import de.uniol.inf.is.odysseus.rcp.viewer.model.ctrl.IController;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.manage.IView;

public abstract class AbstractView<C> implements IView<C> {

	private IController<C> controller;
	
	public AbstractView( IController<C> controller ) {
		if( controller == null ) 
			throw new IllegalArgumentException("controller is null!");
		
			
		this.controller = controller;
	}
	
	public final void setVisible( Collection<? extends INodeView<C>> nodes, boolean isVisible ) {
		if( nodes == null || nodes.isEmpty() )
			return;
 
		for( INodeView<C> node : nodes ) {
			node.setVisible( isVisible );
		}
	}
	
	public void setEnabled( Collection<? extends INodeView<C>> nodes, boolean isEnabled ) {
		if( nodes == null || nodes.isEmpty() )
			return;
		
		for( INodeView<C> nodeDisplay : nodes ) {
			nodeDisplay.setEnabled( isEnabled );
		}
	}

	@Override
	public IController<C> getController() {
		return controller;
	}
	
}
