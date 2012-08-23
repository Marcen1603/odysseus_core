package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class CovarianceMatrix implements Serializable, Cloneable {
    private double[] values;
    private int      id;

    public CovarianceMatrix(int id, double[] values) {
        this.id = id;
        this.values = values;
    }
}
