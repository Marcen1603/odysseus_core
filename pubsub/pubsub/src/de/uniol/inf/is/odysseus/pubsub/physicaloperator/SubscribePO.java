package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * 
 * @author ChrisToenjesDeye
 *
 * @param <T>
 */
public class SubscribePO<T extends IStreamObject<?>> extends AbstractPipe<T, T>{

	private ArrayList<IPredicate<? super T>> predicates;

	
	public SubscribePO(List<IPredicate<? super T>> predicates)  {
        super();
        initPredicates(predicates);
    }

	public SubscribePO(SubscribePO<T> splitPO) {
		super();
        initPredicates(splitPO.predicates);
    }

	@Override
	protected void process_open() throws OpenFailedException {
		//BrokerService.subscribe("Broker_1", predicates, this);
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new SubscribePO<T>(this);
	}
	
	
	private void initPredicates(List<IPredicate<? super T>> predicates) {
        this.predicates = new ArrayList<IPredicate<? super T>>(predicates.size());
        for (IPredicate<? super T> p: predicates){
            this.predicates.add(p.clone());
        }
    }

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		transfer(object);
	}
	

}
