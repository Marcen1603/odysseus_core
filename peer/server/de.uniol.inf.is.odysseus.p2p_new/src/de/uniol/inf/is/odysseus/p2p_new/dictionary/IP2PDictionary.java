package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

public interface IP2PDictionary {
	void addListener( IP2PDictionaryListener listener );
	void removeListener( IP2PDictionaryListener listener );
	
	Collection<SourceAdvertisement> getSources();
	Collection<SourceAdvertisement> getSources( String viewName );
	
	void importSource( SourceAdvertisement advertisement, String viewNameToUse ) throws PeerException, InvalidP2PSource;
	boolean isSourceNameAlreadyInUse(String sourceName);
	boolean removeSourceImport( SourceAdvertisement advertisement );
	boolean isImported( SourceAdvertisement advertisement );
	boolean isImported( String sourceName );
	Optional<String> getImportedSourceName( SourceAdvertisement advertisement );
	Optional<SourceAdvertisement> getImportedSource( String viewName );
	ImmutableList<SourceAdvertisement> getImportedSources();
	
	SourceAdvertisement exportSource( String viewName ) throws PeerException;
	Collection<SourceAdvertisement> exportSources( Collection<String> viewNames ) throws PeerException;
	boolean removeSourceExport( String viewName );
	boolean removeSourcesExport( Collection<String> viewNames);
	boolean isExported( String viewName );
	Optional<SourceAdvertisement> getExportedSource( String viewName );
	ImmutableList<SourceAdvertisement> getExportedSources();
	Optional<JxtaSenderAO> getExportingSenderAO( SourceAdvertisement advertisement);

}
