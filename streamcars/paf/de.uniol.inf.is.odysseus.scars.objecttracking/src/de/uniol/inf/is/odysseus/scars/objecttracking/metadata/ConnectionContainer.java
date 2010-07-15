package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

/**
 * The ConnectionContainer contains an connection list where the rated connections between
 * objects are stored. It serves as a meta data so that in the objecttracking process
 * you could access the connections by object.getMetadata().get/setConnectionList().
 * 
 * @author Volker Janz
 *
 * @param <L> Datatype of the left object
 * @param <R> Datatype of the right object
 * @param <W> Datatype of the rating - has to extend java.lang.Number (Double, Integer, ...)
 */
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
	
	/**
	 * Returns a string with the actual connection list as:
	 * "[(left:right:rating)][(left:right:rating)][(left:right:rating)]..."
	 */
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
