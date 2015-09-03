package de.uniol.inf.is.odysseus.recovery.protectionpoints;

import java.io.Serializable;

/**
 * Interface for listeners to call, if a protection point is reached.
 * 
 * @author Michael Brand
 *
 */
public interface IProtectionPointHandler extends Serializable {

	/**
	 * Called, if a protection point is reached.
	 * 
	 * @throws Exception
	 *             if any error occurs.
	 */
	public void onProtectionPointReached() throws Exception;

}