package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.collection.IPair;

public interface IRecoveryBackupInformation {

	public void setSharedQuery(ID sharedQuery);

	public ID getSharedQuery();
	
	public void setPQL(String pql);
	
	public String getPQL();
	
	public void setPeer(PeerID peer);
	
	public PeerID getPeer();

	public ImmutableCollection<IPair<String, PeerID>> getSubsequentPartsInformation();

	public void setSubsequentPartsInformation(
			Collection<IPair<String, PeerID>> info);

	public void removeSubsequentPartsInformation(IPair<String, PeerID> info);

	public void addSubsequentPartsInformation(IPair<String, PeerID> info);

}