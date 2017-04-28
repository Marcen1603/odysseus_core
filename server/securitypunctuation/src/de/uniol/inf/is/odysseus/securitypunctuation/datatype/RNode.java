package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.List;

public class RNode {
	long roleID;
	String role;
	List<IndexEntry> indexes;
	long rHead;
	long rTail;
	
	
	public RNode(){
		
	}
	
	public RNode(long roleID, String role, List<IndexEntry> indexes, long rHead, long rTail) {
		super();
		this.roleID = roleID;
		this.role = role;
		this.indexes = indexes;
		this.rHead = rHead;
		this.rTail = rTail;
	}
	
	
	@Override
	public String toString() {
		return "RNode [roleID=" + roleID + ", role=" + role + ", indexes=" + indexes + ", rHead=" + rHead + ", rTail="
				+ rTail + "]";
	}
	public long getRoleID() {
		return roleID;
	}
	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<IndexEntry> getIndexes() {
		return indexes;
	}
	public void setIndexes(List<IndexEntry> indexes) {
		this.indexes = indexes;
	}
	public long getrHead() {
		return rHead;
	}
	public void setrHead(long rHead) {
		this.rHead = rHead;
	}
	public long getrTail() {
		return rTail;
	}
	public void setrTail(long rTail) {
		this.rTail = rTail;
	}
}
