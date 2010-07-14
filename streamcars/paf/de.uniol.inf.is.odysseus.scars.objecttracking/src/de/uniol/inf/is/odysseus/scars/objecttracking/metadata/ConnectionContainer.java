package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

public class ConnectionContainer<L, R, W extends java.lang.Number> implements IConnectionContainer<L, R, W> {
	
	private ConnectionList<L, R, W> connectionList;
	
	ConnectionContainer() {
		this.connectionList = new ConnectionList<L, R, W>();
	}
	
	ConnectionContainer(ConnectionList<L, R, W> inilist) {
		this.setConnectionList(inilist);
	}

	public void setConnectionList(ConnectionList<L, R, W> list) {
		this.connectionList = list;
	}

	public ConnectionList<L, R, W> getConnectionList() {
		return this.connectionList;
	}
	
	@Override
	public IConnectionContainer<L, R, W> clone(){
		return new ConnectionContainer<L, R, W>(this.getConnectionList());
	}
	
	@Override
	public String toString(){
		if(this.connectionList == null || this.connectionList.size() == 0){
			return "no connections";
		}
		
		String cons = "[";
		for(Connection<L, R, W> con : this.connectionList) {
			cons += "(";
			cons += con.getLeft().toString();
			cons += ":";
			cons += con.getRight().toString();
			cons += ":";
			cons += con.getRating().toString();
			cons += ")";
		}
		cons += "]";
		
		return cons;
	}
	
}
