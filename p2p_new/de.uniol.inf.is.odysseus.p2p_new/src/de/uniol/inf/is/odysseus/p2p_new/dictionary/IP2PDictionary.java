package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisement;

public interface IP2PDictionary {

	void addListener( IP2PDictionaryListener listener );
	void removeListener( IP2PDictionaryListener listener );
	
	void addSource( SourceAdvertisement advertisement );
	boolean removeSource( SourceAdvertisement advertisement );
	boolean existsSource( SourceAdvertisement advertisement );
	boolean existsSource( String viewName );
	ImmutableList<SourceAdvertisement> getSources();
	ImmutableList<SourceAdvertisement> getSources( String viewName );
	
	ImmutableList<SourceAdvertisement> getSame( SourceAdvertisement advertisement );
	boolean isSame( SourceAdvertisement a, SourceAdvertisement b );
	
	void importSource( SourceAdvertisement advertisement, String viewNameToUse ) throws PeerException;
	boolean removeSourceImport( SourceAdvertisement advertisement );
	boolean isImported( SourceAdvertisement advertisement );
	Optional<String> getImportedSourceName( SourceAdvertisement advertisement );
	Optional<SourceAdvertisement> getImportedSource( String viewName );
	ImmutableList<SourceAdvertisement> getImportedSources();
	
	SourceAdvertisement exportSource( String viewName, String queryBuildConfigurationName ) throws PeerException;
	boolean removeSourceExport( String viewName );
	boolean isExported( String viewName );
	Optional<SourceAdvertisement> getExportedSource( String viewName );
	ImmutableList<SourceAdvertisement> getExportedSources();
}
