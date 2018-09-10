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
public class StoredLineFunction extends AbstractFunction<double[][]> {

    /**
     * 
     */
    private static final long serialVersionUID = 6045568038655218127L;
    private static final SDFDatatype[][] acceptedTypes = { { SDFDatatype.STRING }, SDFDatatype.MATRIXS, SDFDatatype.MATRIXS };

    public StoredLineFunction() {
        super("storedLine", 3, acceptedTypes, SDFDatatype.MATRIX_DOUBLE, false);
    }

    @Override
    public double[][] getValue() {
        String name = getInputValue(0);
        Object[] path = Doubles.asList(((double[][]) getInputValue(1))[0]).toArray();
        int[] index = Ints.toArray(Doubles.asList(((double[][]) getInputValue(2))[0]));
        getInputValue(2);
        Objects.requireNonNull(name);
        Objects.requireNonNull(path);
        Objects.requireNonNull(index);
        IAssociativeStorage<Tuple<?>> store = AssociativeStorageManager.get(name);
        if (store != null) {
            return new double[][] { store.getLine(path, new int[] { index[0] }) };
        }
        return new double[][] { { 0.0 } };
    }

}
