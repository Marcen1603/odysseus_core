package de.uniol.inf.is.odysseus.viewer.model.meta;


public interface IMetadataChangeListener<M> {

	public void metadataChanged( IMetadataModel<M> sender, String changedMetadataItemType );
	
}
