package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;

public class DDCChange {

	public enum DDCChangeType {
		ddcEntryRemoved, ddcEntryAdded;
	}
	
	private DDCEntry ddcEntry;
	
	private DDCChangeType ddcChangeType;

	public DDCChange(DDCEntry ddcEntry, DDCChangeType ddcChangeType){
		this.ddcEntry = ddcEntry;
		this.ddcChangeType = ddcChangeType;
	}
	
	public DDCEntry getDdcEntry() {
		return ddcEntry;
	}

	public void setDdcEntry(DDCEntry ddcEntry) {
		this.ddcEntry = ddcEntry;
	}

	public DDCChangeType getDdcChangeType() {
		return ddcChangeType;
	}

	public void setDdcChangeType(DDCChangeType ddcChangeType) {
		this.ddcChangeType = ddcChangeType;
	}
}
