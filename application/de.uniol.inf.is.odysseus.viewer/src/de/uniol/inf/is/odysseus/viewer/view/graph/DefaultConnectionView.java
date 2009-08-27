package de.uniol.inf.is.odysseus.viewer.view.graph;

import de.uniol.inf.is.odysseus.viewer.model.graph.IConnectionModel;
import de.uniol.inf.is.odysseus.viewer.view.symbol.ConnectionSymbolElementContainer;

public class DefaultConnectionView<C> implements IConnectionView<C> {

	private final IConnectionModel<C> data;
	private final INodeView<C> start;
	private final INodeView<C> end;
	private ConnectionSymbolElementContainer<C> symbol = new ConnectionSymbolElementContainer<C>(); 
	
	public DefaultConnectionView( IConnectionModel<C> data, INodeView<C> start, INodeView<C> end ) {
		this.data = data;
		this.start = start;
		this.end = end;
	}

	@Override
	public final IConnectionModel<C> getModelConnection() {
		return data;
	}
	
	@Override
	public final INodeView<C> getViewedStartNode() {
		return start;
	}
	
	@Override
	public final INodeView<C> getViewedEndNode() {
		return end;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "{ " );
		sb.append( start.toString() );
		sb.append( " --> " );
		sb.append(  end.toString()  );
		sb.append( " }" );
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals( Object obj ) {
		if( obj == null )
			return false;
		if( !(obj instanceof DefaultConnectionView))
			return false;
		if( obj == this )
			return true;
		
		DefaultConnectionView<C> d = (DefaultConnectionView<C>)obj;
		return d.start.equals(start) && d.end.equals( end );
	}

	@Override
	public ConnectionSymbolElementContainer<C> getSymbolContainer() {
		return symbol;
	}

	@Override
	public int hashCode() {
		return start.hashCode() + end.hashCode();
	}

}
