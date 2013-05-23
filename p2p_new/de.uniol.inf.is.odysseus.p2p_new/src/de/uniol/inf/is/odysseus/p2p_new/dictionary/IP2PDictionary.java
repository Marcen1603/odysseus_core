package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import net.jxta.id.ID;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.sources.ViewAdvertisement;

public interface IP2PDictionary {

	void addListener( IP2PDictionaryListener listener );
	void removeListener( IP2PDictionaryListener listener );
	
	void addView( ViewAdvertisement advertisement );
	void removeView( ViewAdvertisement advertisement );
	boolean existsView( ViewAdvertisement advertisement );
	boolean existsView( ID id );
	boolean existsView( String viewName );
	
	ImmutableList<ViewAdvertisement> getViews();
	ImmutableList<ViewAdvertisement> getViews( ID viewID );
	ImmutableList<ViewAdvertisement> getViews( String viewName );
	ImmutableList<ID> getViewIDs();
	
	void importView( ID viewID, String viewNameToUse ) throws PeerException;

}
