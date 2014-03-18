/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions;

import java.util.Objects;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.storage.AssociativeStorageManager;
import de.uniol.inf.is.odysseus.core.physicaloperator.storage.IAssociativeStorage;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class StoredValueFunction extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 6045568038655218127L;
    private static final SDFDatatype[][] acceptedTypes = { { SDFDatatype.STRING }, { SDFDatatype.MATRIX_DOUBLE }, { SDFDatatype.MATRIX_DOUBLE } };

    public StoredValueFunction() {
    	super("storedValue",3, acceptedTypes,SDFDatatype.DOUBLE, false);
    }
    

    @Override
    public Double getValue() {
        String name = getInputValue(0);
        Object[] path = Doubles.asList(((double[][]) getInputValue(1))[0]).toArray();
        int[] index = Ints.toArray(Doubles.asList(((double[][]) getInputValue(2))[0]));
        Objects.requireNonNull(name);
        Objects.requireNonNull(path);
        Objects.requireNonNull(index);
        IAssociativeStorage<Tuple<?>> store = AssociativeStorageManager.get(name);
        if (store != null) {
            return store.get(path, index);
        }
        else {
            return 0.0;
        }
    }

}
