package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;

public interface IP2PDictionary {
	void addListener( IP2PDictionaryListener listener );
	void removeListener( IP2PDictionaryListener listener );
	
	boolean checkSource(SourceAdvertisement srcAdvertisement);
	boolean existsSource( SourceAdvertisement advertisement );
	boolean existsSource( String viewName );
	ImmutableList<SourceAdvertisement> getSources();
	ImmutableList<SourceAdvertisement> getSources( String viewName );
	
	ImmutableList<SourceAdvertisement> getSame( SourceAdvertisement advertisement );
	boolean isSame( SourceAdvertisement a, SourceAdvertisement b );
	
	void importSource( SourceAdvertisement advertisement, String viewNameToUse ) throws PeerException, InvalidP2PSource;
	boolean removeSourceImport( SourceAdvertisement advertisement );
	boolean isImported( SourceAdvertisement advertisement );
	boolean isImported( String sourceName );
	Optional<String> getImportedSourceName( SourceAdvertisement advertisement );
	Optional<SourceAdvertisement> getImportedSource( String viewName );
	ImmutableList<SourceAdvertisement> getImportedSources();
	
	SourceAdvertisement exportSource( String viewName, String queryBuildConfigurationName ) throws PeerException;
	boolean removeSourceExport( String viewName );
	boolean isExported( String viewName );
	Optional<SourceAdvertisement> getExportedSource( String viewName );
	ImmutableList<SourceAdvertisement> getExportedSources();
	
	ImmutableList<PeerID> getRemotePeerIDs();
	boolean existsRemotePeer( PeerID peerID );
	boolean existsRemotePeer( String peerName );
	Optional<String> getRemotePeerName( PeerID peerID );
	Optional<String> getRemotePeerAddress( PeerID peerID );
	PeerID getLocalPeerID();
	String getLocalPeerName();
	PeerGroupID getLocalPeerGroupID();
	String getLocalPeerGroupName();
}
