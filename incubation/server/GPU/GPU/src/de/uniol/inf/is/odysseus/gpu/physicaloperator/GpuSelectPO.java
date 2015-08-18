/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author Alexander
 *
 */

public class GpuSelectPO<T extends IStreamObject<?>> extends SelectPO<T>{
	
	private IPredicate<? super T> predicate;
    private int heartbeatRate;
    private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<>();
	
    
    public GpuSelectPO(IPredicate<? super T> predicate) {
		super(predicate);
		
	}
	public GpuSelectPO(SelectPO<T> po) {
		super(po);
		
	}
	
	public int getHeartbeatRate() {
		return heartbeatRate;
	}
	public void setHeartbeatRate(int heartbeatRate) {
		this.heartbeatRate = heartbeatRate;
	}
	
	@Override
	public IPredicate<? super T> getPredicate() {
		// TODO Auto-generated method stub
		return super.getPredicate();
	}
	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return super.getOutputMode();
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub
		super.processPunctuation(punctuation, port);
	}
	@Override
	public void process_open() throws OpenFailedException {
		// TODO Auto-generated method stub
		super.process_open();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	@Override
	public IHeartbeatGenerationStrategy<T> getHeartbeatGenerationStrategy() {
		// TODO Auto-generated method stub
		return super.getHeartbeatGenerationStrategy();
	}
	@Override
	public void setHeartbeatGenerationStrategy(
			IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy) {
		// TODO Auto-generated method stub
		super.setHeartbeatGenerationStrategy(heartbeatGenerationStrategy);
	}
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		// TODO Auto-generated method stub
		return super.process_isSemanticallyEqual(ipo);
	}
	@Override
	public boolean isContainedIn(IPipe<T, T> ip) {
		// TODO Auto-generated method stub
		return super.isContainedIn(ip);
	}
	
	
	//ALex
	
	@Override
	protected void process_next(T object, int port) {
		// TODO Auto-generated method stub
		super.process_next(object, port);
	}
	
	
    
}
