package de.uniol.inf.is.odysseus.viewer.view.symbol;

public interface ISymbolElementFactory<C> {

	public ISymbolElement<C> createForNode( SymbolElementInfo info );
	public IConnectionSymbolElement<C> createForConnection( String type );
	public ISymbolElement<C> createDefaultSymbolElement();

}
