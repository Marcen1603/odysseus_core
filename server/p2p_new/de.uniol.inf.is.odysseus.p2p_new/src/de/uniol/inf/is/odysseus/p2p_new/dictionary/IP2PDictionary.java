package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import java.util.Collection;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

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
	Collection<SourceAdvertisement> exportSources( Collection<String> viewNames, String queryBuildConfigurationName ) throws PeerException;
	boolean removeSourceExport( String viewName );
	boolean removeSourcesExport( Collection<String> viewNames);
	boolean isExported( String viewName );
	Optional<SourceAdvertisement> getExportedSource( String viewName );
	ImmutableList<SourceAdvertisement> getExportedSources();
	Optional<JxtaSenderAO> getExportingSenderAO( SourceAdvertisement advertisement);
	
	ImmutableList<PeerID> getRemotePeerIDs();
	boolean existsRemotePeer( PeerID peerID );
	boolean existsRemotePeer( String peerName );
	Optional<String> getRemotePeerName( PeerID peerID );
	Optional<String> getRemotePeerAddress( PeerID peerID );

}
