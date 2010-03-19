package de.uniol.inf.is.odysseus.broker.physicaloperator;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferFunction;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class LeftAfterRightTITransferFunction<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends TITransferFunction<T>{
	
	public LeftAfterRightTITransferFunction(){
		super();
	}
	
	
	@Override
	public void newElement(T object, int port) {	
			if(port==0){
			ITimeInterval minimum = null;
			synchronized (minTs) {
				minTs[port] = object.getMetadata();
				if (minTs[0]!=null){
				//if (minTs[0]!=null && minTs[1]!=null){
				//	minimum = TimeInterval.startsBefore(minTs[0], minTs[1]) ? minTs[0] : minTs[1];
					minimum = minTs[0];
				}
			}
			if (minimum != null){
				synchronized (super.out) {
					// don't use an iterator, it does NOT guarantee ordered traversal!
					T elem = this.out.peek();
					while(elem != null && TimeInterval.startsBeforeOrEqual(elem.getMetadata(), minimum)){
						this.out.poll();
						po.transfer(elem);
						elem = this.out.peek();
					}
				}
			}
			}else{
				synchronized (super.out) {
					// don't use an iterator, it does NOT guarantee ordered traversal!
					T elem = this.out.peek();
					while(elem != null){
						this.out.poll();
						po.transfer(elem);
						elem = this.out.peek();
					}
				}
			}
		
//		T elem = this.out.peek();
//		while(elem != null){
//			this.out.poll();
//			po.transfer(elem);
//			elem = this.out.peek();
//		}
//		
	}

	
	

}
