package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IProbabilisticPredicate<T> extends IClone, Serializable {

    /**
     * Can be called to initialize a predicate bevor it can be
     * evaluated
     */
    public void init();

    /**
     * Evaluate the predicate with the input
     * 
     * @param input
     * @return
     */
    double evaluate(T input);

    /**
     * Evaluate a predicate with a left and a right part (e.g. in joins)
     * 
     * @param left
     * @param right
     * @return
     */
    double evaluate(T left, T right);

    /**
     * Checks if this predicate is contained in another predicate
     * 
     * @param o
     * @return true if contained, false if not or not decidable
     */
    boolean isContainedIn(IProbabilisticPredicate<?> o);

    /**
     * Get the list of all attributes in this predicate
     * 
     * @return
     */
    public List<SDFAttribute> getAttributes();

    /**
     * Compare two predicates
     * 
     * @param pred
     * @return true, if both predicates are equal
     */
    public boolean equals(IProbabilisticPredicate<T> pred);

    @Override
    public IProbabilisticPredicate<T> clone();
}
