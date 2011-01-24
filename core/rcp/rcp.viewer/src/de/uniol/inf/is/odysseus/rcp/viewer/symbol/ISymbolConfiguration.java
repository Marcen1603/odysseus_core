package de.uniol.inf.is.odysseus.rcp.viewer.symbol;

import java.util.Collection;
import java.util.Map;



public interface ISymbolConfiguration {

	public Map<String, Collection<SymbolElementInfo>> getMap();
	public Collection<SymbolElementInfo> getDefaultSymbolInfos();
	
}
