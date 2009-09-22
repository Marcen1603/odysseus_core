package de.uniol.inf.is.odysseus.base.planmanagement;

/**
 * Describes an object which provides informations about it and their modules.
 * Used for objects which use OSGi-services.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IInfoProvider {
	/**
	 * Provides an string with informations of the registered modules and the
	 * current state of this object.
	 * 
	 * @return String with informations of the registered modules and the
	 *         current state of this object.
	 */
	public String getInfos();
}
