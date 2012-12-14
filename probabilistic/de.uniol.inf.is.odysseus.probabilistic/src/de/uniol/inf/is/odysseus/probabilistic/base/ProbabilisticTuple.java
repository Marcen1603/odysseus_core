package de.uniol.inf.is.odysseus.probabilistic.base;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;

public class ProbabilisticTuple<T extends IMetaAttribute> extends Tuple<T> {

    /**
     * 
     */
    private static final long serialVersionUID = -4389825802466821416L;
    protected Object[]        distributions;

    public ProbabilisticTuple(Object[] attributes, boolean b) {
        super(attributes, b);
    }

    public ProbabilisticTuple(ProbabilisticTuple<T> copy, Object[] newAttrs, boolean b) {
        super(copy);
    }

    public ProbabilisticTuple(ProbabilisticTuple<T> copy) {
        super(copy);
        List<NormalDistributionMixture> continuousAttributes = new ArrayList<NormalDistributionMixture>();
        for (int i = 0; i < this.attributes.length; i++) {
            if ((this.attributes[i] != null) && (this.attributes[i].getClass() == ProbabilisticContinuousDouble.class)) {
                ProbabilisticContinuousDouble attribute = (ProbabilisticContinuousDouble) this.attributes[i];
                int index = continuousAttributes.indexOf(attribute.getDistribution());
                if (index >= 0) {
                    this.attributes[i] = new ProbabilisticContinuousDouble(attribute.getDimension(),
                            continuousAttributes.get(index));
                }
                else {
                    continuousAttributes.add(attribute.getDistribution());
                }

            }
        }
    }

    @Override
    public ProbabilisticTuple<T> restrict(int attr, boolean createNew) {
        // TODO Auto-generated method stub
        return (ProbabilisticTuple) super.restrict(attr, createNew);
    }

    @Override
    public ProbabilisticTuple<T> restrict(int[] attrList, boolean createNew) {
        // TODO Auto-generated method stub
        return (ProbabilisticTuple) super.restrict(attrList, createNew);
    }

    private ProbabilisticTuple<T> restrictCreation(boolean createNew, Object[] newAttrs) {
        if (createNew) {
            ProbabilisticTuple<T> newTuple = new ProbabilisticTuple<T>(this, newAttrs, false);
            return newTuple;
        }
        return this;
    }
}
