package de.uniol.inf.is.odysseus.net.data;

public interface IDistributedDataListener {
	
	public void distributedDataManagerStarted( IDistributedDataManager sender );

	public void distributedDataAdded( IDistributedDataManager sender, IDistributedData addedData );
	public void distributedDataModified( IDistributedDataManager sender, IDistributedData oldData, IDistributedData newData );
	public void distributedDataRemoved( IDistributedDataManager sender, IDistributedData removedData );
	
	public void distributedDataManagerStopped( IDistributedDataManager sender );
}
