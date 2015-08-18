/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Alexander
 *
 */
public class GpuProjectionPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// TODO Auto-generated method stub
		super.process_open();
	}
	
	@Override
	protected void process_next(T object, int port) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void process_close() {
		// TODO Auto-generated method stub
		super.process_close();
	}

}
