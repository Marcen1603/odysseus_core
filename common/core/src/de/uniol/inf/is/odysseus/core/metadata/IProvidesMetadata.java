package de.uniol.inf.is.odysseus.core.metadata;

public interface IProvidesMetadata<M extends IMetaAttribute> {

	/**
	 * Gets the default meta data used processing purposes (e.g. timestamps, priority or latency)
	 * This meta data is assigned to each object in the meta data creation po
	 *
	 * @return
	 */
	public M getMetadata();

	/**
	 * Set the metadata object of this tuple. Typically done from the meta data creation po
	 * @param metadata
	 */
	public void setMetadata(M metadata);
}
