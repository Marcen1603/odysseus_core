/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.storage.IAssociativeStorage;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class AssociativeStoragePO<T extends Tuple<?>> extends AbstractPipe<T,T> {

    private IAssociativeStorage<T> store;
    private int[] hierachyPos;
    private int[] indexPos;
    private int valuePos;

    public AssociativeStoragePO(AssociativeStoragePO<T> storePO) {
        super();
        this.store = storePO.store;
        this.hierachyPos = storePO.hierachyPos;
        this.indexPos = storePO.indexPos;
        this.valuePos = storePO.valuePos;
    }

    public AssociativeStoragePO(IAssociativeStorage<T> store, int[] hierachyPos, int[] indexPos, int valuePos) {
        super();
        this.store = store;
        this.hierachyPos = hierachyPos;
        this.indexPos = indexPos;
        this.valuePos = valuePos;
    }

    @Override
    protected void process_next(T object, int port) {
        Object[] hierachy = new Object[this.hierachyPos.length];
        for (int i = 0; i < this.hierachyPos.length; i++) {
            hierachy[i] = object.getAttribute(this.hierachyPos[i]);
        }
        int[] index = new int[this.indexPos.length];
        for (int i = 0; i < this.indexPos.length; i++) {
            index[i] = ((Number) object.getAttribute(this.indexPos[i])).intValue();
        }
        Double value = (Double) object.getAttribute(valuePos);
        this.store.set(hierachy, index, value);
        transfer(object);
   }

    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {
    }

    @Override
    protected void process_close() {
        super.process_close();
    }

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
