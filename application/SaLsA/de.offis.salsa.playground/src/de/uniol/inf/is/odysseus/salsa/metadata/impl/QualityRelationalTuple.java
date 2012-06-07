package de.uniol.inf.is.odysseus.salsa.metadata.impl;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.salsa.metadata.Quality;

public class QualityRelationalTuple<T extends Quality> extends Tuple<T> implements
        Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6442070593364715220L;

    public QualityRelationalTuple() {
        super();
    }

    public QualityRelationalTuple(int attributeCount) {
        super(attributeCount, false);
    }

    public QualityRelationalTuple(QualityRelationalTuple<T> tuple) {
        super(tuple);
    }

    @Override
    public QualityRelationalTuple<T> clone() {
        return new QualityRelationalTuple<T>(this);
    }
}
