package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerService;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;

/**
 * 
 * @author ChrisToenjesDeye
 *
 * @param <T>
 */
public class SubscribePO<T extends IStreamObject<?>> extends AbstractPipe<T, T>{

	private List<IPredicate<? super T>> predicates;
	private SDFSchema schema;
	private String brokerName;
	
	public SubscribePO(List<IPredicate<? super T>> predicates, String brokername, SDFSchema schema)  {
        super();
        initPredicates(predicates);
        this.brokerName = brokername;
        this.schema = schema;
    }

	public SubscribePO(SubscribePO<T> splitPO) {
		super();
		this.brokerName = splitPO.brokerName;
		this.schema = splitPO.schema;
        initPredicates(splitPO.predicates);
    }

	@Override
	protected void process_open() throws OpenFailedException {
		IBroker<T> b = BrokerService.getBrokerByName(brokerName);
		b.subscribe(predicates, this);
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
