package de.uniol.inf.is.odysseus.peer.ddc.file;


/**
 * A DDC file handler listener will be notified, if a DDC file has been loaded.
 * 
 * @author Michael Brand
 *
 */
public interface IDDCFileHandlerListener {

	/**
	 * Listener method for loaded DDC files.
	 */
	void ddcFileLoaded();

}