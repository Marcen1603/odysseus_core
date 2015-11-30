package de.uniol.inf.is.odysseus.net.data;

public interface IDistributedDataListener {

	public void distributedDataAdded( IDistributedData addedData );
	public void distributedDataModified( IDistributedData oldData, IDistributedData newData );
	public void distributedDataRemoved( IDistributedData removedData );
	
}
