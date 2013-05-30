package de.uniol.inf.is.odysseus.sentimentdetection.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

@SuppressWarnings({"rawtypes"})
public class SentimentDetectionPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	 private List<IPredicate<? super T>> predicates;
	 
	 
	 public SentimentDetectionPO(List<IPredicate<? super T>> predicates)  {
	        super();
	        initPredicates(predicates);
	    }
	 
	    public SentimentDetectionPO(SentimentDetectionPO<T> splitPO) {
	        super();
	        initPredicates(splitPO.predicates);
	    }
	    private void initPredicates(List<IPredicate<? super T>> predicates) {
	        this.predicates = new ArrayList<IPredicate<? super T>>(predicates.size());
	        for (IPredicate<? super T> p: predicates){
	            this.predicates.add(p.clone());
	        }
	    }
	 
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	  @Override
	    public void process_open() throws OpenFailedException{
	        super.process_open();
	        for (IPredicate<? super T> p: predicates){
	            p.init();
	        }       
	    }
	  
	  
	  @Override
	    protected void process_next(T object, int port) {
	        for (int i=0;i<predicates.size();i++){
	            if (predicates.get(i).evaluate(object)) {
	                transfer(object,i);
	                return;
	            }
	        }
	        transfer(object,predicates.size());
	    }
	     

	  @Override
	    public SentimentDetectionPO<T> clone() {
	        return new SentimentDetectionPO<T>(this);
	    }
	 

	    @Override
	    public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
	        if(!(ipo instanceof SentimentDetectionPO)) {
	            return false;
	        }
	        SentimentDetectionPO spo = (SentimentDetectionPO) ipo;
	        if(this.hasSameSources(spo) &&
	                this.predicates.size() == spo.predicates.size()) {
	            for(int i = 0; i<this.predicates.size(); i++) {
	                if(!this.predicates.get(i).equals(spo.predicates.get(i))) {
	                    return false;
	                }
	            }
	            return true;
	        }
	        return false;
	    }



}
