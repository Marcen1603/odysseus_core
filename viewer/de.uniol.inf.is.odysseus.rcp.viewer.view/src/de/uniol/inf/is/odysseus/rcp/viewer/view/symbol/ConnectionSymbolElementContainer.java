package de.uniol.inf.is.odysseus.rcp.viewer.view.symbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public final class ConnectionSymbolElementContainer<C> implements Iterable< IConnectionSymbolElement<C> >{

	private Collection<IConnectionSymbolElement<C>> symbols = new ArrayList<IConnectionSymbolElement<C>>();
	
	public void add( IConnectionSymbolElement<C> symbol ) {
		if( !symbols.contains( symbol ))
			symbols.add( symbol );
	}
	
	public void remove( IConnectionSymbolElement<C> symbol ) {
		symbols.remove( symbol );
	}
	
	public int getSize() {
		return symbols.size();
	}
	
	public void clear() {
		symbols.clear();
	}

	@Override
	public Iterator< IConnectionSymbolElement<C> > iterator() {
		return symbols.iterator();
	}

}
