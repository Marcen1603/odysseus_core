package de.uniol.inf.is.odysseus.rcp.viewer.view.symbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;



public final class SymbolElementContainer<C> implements Iterable< ISymbolElement<C> >{

	private Collection<ISymbolElement<C>> symbols = new ArrayList<ISymbolElement<C>>();
	
	public void add( ISymbolElement<C> symbol ) {
		if( !symbols.contains( symbol ))
			symbols.add( symbol );
	}
	
	public void remove( ISymbolElement<C> symbol ) {
		symbols.remove( symbol );
	}
	
	public int getSize() {
		return symbols.size();
	}
	
	public void clear() {
		symbols.clear();
	}

	@Override
	public Iterator< ISymbolElement<C> > iterator() {
		return symbols.iterator();
	}

}
