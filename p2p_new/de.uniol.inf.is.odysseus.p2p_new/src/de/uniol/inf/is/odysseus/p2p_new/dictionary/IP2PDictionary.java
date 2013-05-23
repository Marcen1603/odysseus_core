package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.sources.ViewAdvertisement;

public interface IP2PDictionary {

	void addListener( IP2PDictionaryListener listener );
	void removeListener( IP2PDictionaryListener listener );
	
	void addView( ViewAdvertisement advertisement );
	void removeView( ViewAdvertisement advertisement );
	boolean existsView( ViewAdvertisement advertisement );
	boolean existsView( String viewName );
	
	ImmutableList<ViewAdvertisement> getViews();
	ImmutableList<ViewAdvertisement> getViews( String viewName );
	
	ImmutableList<ViewAdvertisement> getSame( ViewAdvertisement advertisement );
	boolean isSame( ViewAdvertisement a, ViewAdvertisement b );
	
	void importView( ViewAdvertisement advertisement, String viewNameToUse ) throws PeerException;
	boolean isImported( ViewAdvertisement advertisement );
	Optional<String> getImportedViewName( ViewAdvertisement advertisement );
	Optional<ViewAdvertisement> getImportedAdvertisement( String viewName );
}
