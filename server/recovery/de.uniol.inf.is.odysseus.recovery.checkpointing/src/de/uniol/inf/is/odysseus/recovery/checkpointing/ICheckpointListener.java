package de.uniol.inf.is.odysseus.recovery.checkpointing;

import java.io.Serializable;

/**
 * Interface for listeners to call, if a checkpoint is reached.
 * 
 * @author Michael Brand
 *
 */
public interface ICheckpointListener extends Serializable {

	/**
	 * Called, if a checkpoint is reached.
	 */
	public void onCheckpointReached() throws Exception;

}