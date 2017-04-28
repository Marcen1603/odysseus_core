package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.List;

public class SPIndex {
	List<RNode> RNodes;
	List<IndexEntry> indexEntries;

	
	
	public SPIndex(List<RNode> rNodes, List<IndexEntry> indexEntries) {
		super();
		RNodes = rNodes;
		this.indexEntries = indexEntries;
	}

	public List<RNode> getRNodes() {
		return RNodes;
	}

	public void setRNodes(List<RNode> rNodes) {
		RNodes = rNodes;
	}

	public List<IndexEntry> getIndexEntries() {
		return indexEntries;
	}

	public void setIndexEntries(List<IndexEntry> indexEntries) {
		this.indexEntries = indexEntries;
	}
	
	public void addSP(long[] roleIDs,AbstractSecurityPunctuation sp){
		this.indexEntries.add(indexEntries.size(), new IndexEntry(roleIDs,sp));
	}
}
