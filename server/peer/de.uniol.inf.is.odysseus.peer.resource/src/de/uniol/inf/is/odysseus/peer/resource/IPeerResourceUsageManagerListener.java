package de.uniol.inf.is.odysseus.peer.resource;


public interface IPeerResourceUsageManagerListener {

	public void remoteResourceUsageChanged( IPeerResourceUsageManager sender, IResourceUsage remoteUsage );
	public void localResourceUsageChanged( IPeerResourceUsageManager sender, IResourceUsage localUsage );
}
