package de.uniol.inf.is.odysseus.peer.ddc;

public interface IDDCListener {
	
	void ddcEntryAdded(DDCEntry ddcEntry);
	
	void ddcEntryRemoved(DDCEntry ddcEntry);
	
}
