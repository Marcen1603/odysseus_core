package de.uniol.inf.is.odysseus.rcp.viewer.model.meta;


public interface IMetadataChangeListener<M> {

	public void metadataChanged( IMetadataModel<M> sender, String changedMetadataItemType );
	
}
