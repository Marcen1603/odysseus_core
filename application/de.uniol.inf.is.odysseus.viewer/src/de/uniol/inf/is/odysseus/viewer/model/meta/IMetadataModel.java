package de.uniol.inf.is.odysseus.viewer.model.meta;

import java.util.Collection;

public interface IMetadataModel<M> {

	public Collection<String> getProvidedMetadataTypes();
	public M getMetadataItem( String metadataType );
	public void addMetadataItem( String type, M metaDataItem );
	public void removeMetadataItem( String type );
	public void resetMetadataItem( String type );

	public void addMetadataChangeListener( IMetadataChangeListener<M> listener );
	public void removeMetadataChangeListener( IMetadataChangeListener<M> listener );
	public void notifyMetadataChangedListener( String changedType );
}
