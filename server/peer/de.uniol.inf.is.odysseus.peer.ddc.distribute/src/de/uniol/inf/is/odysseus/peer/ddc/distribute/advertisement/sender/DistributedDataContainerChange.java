package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender;

import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;

/**
 * Container class for holding DDC changes
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class DistributedDataContainerChange {

	public enum DistributedDataContainerChangeType {
		ddcEntryRemoved, ddcEntryAdded;
	}

	private DDCEntry ddcEntry;

	private DistributedDataContainerChangeType ddcChangeType;

	public DistributedDataContainerChange(DDCEntry ddcEntry,
			DistributedDataContainerChangeType ddcChangeType) {
		this.ddcEntry = ddcEntry;
		this.ddcChangeType = ddcChangeType;
	}

	public DDCEntry getDdcEntry() {
		return ddcEntry;
	}

	public void setDdcEntry(DDCEntry ddcEntry) {
		this.ddcEntry = ddcEntry;
	}

	public DistributedDataContainerChangeType getDdcChangeType() {
		return ddcChangeType;
	}

	public void setDdcChangeType(
			DistributedDataContainerChangeType ddcChangeType) {
		this.ddcChangeType = ddcChangeType;
	}
}
