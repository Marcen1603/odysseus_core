/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;

/**
 * @author Alexander
 *
 */

public class GpuSelectPO<T extends IStreamObject<?>> extends SelectPO<T>{

	
	// standard const
	@SuppressWarnings("unchecked")
	public GpuSelectPO(SelectAO ao) {
		super((IPredicate<? super T>) ao.getPredicate());
	}
	
	// copy const
	public GpuSelectPO(GpuSelectPO<T> po) {
		super(po);
	}
	
	@Override
	public void process_open() throws OpenFailedException {
		super.process_open();
		
		// SetName() ??
		// getPredicate()
		
		// CUDA-Vorbereitung
		// --> evtl. OpenFailedException(...)
	}
	
    @Override
    protected void process_next(T object, int port) {
    	// no super.process_next!!!
    	
    	// CUDA-Kram
    	
    	// --> transfer(Ergebnis v. CUDA)
    }
    
    @Override
    protected void process_close() {
    	super.process_close();
    	
    	// CUDA-Nachbereitung
    }
    
    
}
