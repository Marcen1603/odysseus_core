/*
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class EMPO<T extends ITimeInterval> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
    private DefaultTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> area;
    private int[] attributes;

    public EMPO(int[] attributes, int mixtures) {
        this.attributes = attributes;
        area = new EMTISweepArea(attributes, mixtures);
    }

    public EMPO(EMPO<T> emPO) {
        super(emPO);
        this.attributes = emPO.attributes.clone();
        this.area = emPO.area.clone();
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    @Override
    protected void process_next(ProbabilisticTuple<T> object, int port) {
        NormalDistributionMixture[] distributions = object.getDistributions();
        ProbabilisticTuple<T> outputVal = object.clone();
        synchronized (area) {
            area.insert(object);
        }

        Map<NormalDistribution, Double> components = new HashMap<NormalDistribution, Double>();
        EMTISweepArea emArea = (EMTISweepArea) this.area;
        for (int i = 0; i < emArea.getMixtures(); i++) {
            NormalDistribution distribution = new NormalDistribution(emArea.getMean(i).getColumn(0), CovarianceMatrixUtils.fromMatrix(emArea.getCovarianceMatrix(i)));
            components.put(distribution, emArea.getWeight(i));
        }
        NormalDistributionMixture mixture = new NormalDistributionMixture(components);
        mixture.setAttributes(attributes);
        NormalDistributionMixture[] outputValDistributions = new NormalDistributionMixture[distributions.length + 1];

        for (int a = 0; a < this.attributes.length; a++) {
            outputVal.setAttribute(this.attributes[a], new ProbabilisticContinuousDouble(distributions.length));
        }
        System.arraycopy(distributions, 0, outputValDistributions, 0, distributions.length);
        outputValDistributions[distributions.length] = mixture;
        outputVal.setDistributions(outputValDistributions);

        this.transfer(outputVal);
    }

    @Override
    public AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
        return new EMPO<T>(this);
    }

    /**
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
        attr.add(new SDFAttribute("", "a", SDFDatatype.DOUBLE));
        attr.add(new SDFAttribute("", "b", SDFDatatype.DOUBLE));
        attr.add(new SDFAttribute("", "c", SDFDatatype.DOUBLE));
        attr.add(new SDFAttribute("", "d", SDFDatatype.DOUBLE));

        SDFSchema schema = new SDFSchema("", attr);
        Object[] attributes1 = new Object[] { 1.0, 1.0, 6.0, 11.0 };
        Object[] attributes2 = new Object[] { 2.0, 2.0, 5.0, 12.0 };
        Object[] attributes3 = new Object[] { 3.0, 3.0, 7.0, 13.0 };
        Object[] attributes4 = new Object[] { 4.0, 4.0, 8.0, 14.0 };
        Object[] attributes5 = new Object[] { 5.0, 5.0, 9.0, 15.0 };
        Object[] attributes6 = new Object[] { 6.0, 6.0, 10.0, 16.0 };

        ProbabilisticTuple<ITimeInterval> tuple1 = new ProbabilisticTuple<>(attributes1, true);
        tuple1.setMetadata(new TimeIntervalProbabilistic());
        tuple1.getMetadata().setStart(PointInTime.currentPointInTime());

        ProbabilisticTuple<ITimeInterval> tuple2 = new ProbabilisticTuple<>(attributes2, true);
        tuple2.setMetadata(new TimeIntervalProbabilistic());
        tuple2.getMetadata().setStart(PointInTime.currentPointInTime());

        ProbabilisticTuple<ITimeInterval> tuple3 = new ProbabilisticTuple<>(attributes3, true);
        tuple3.setMetadata(new TimeIntervalProbabilistic());
        tuple3.getMetadata().setStart(PointInTime.currentPointInTime());

        ProbabilisticTuple<ITimeInterval> tuple4 = new ProbabilisticTuple<>(attributes4, true);
        tuple4.setMetadata(new TimeIntervalProbabilistic());
        tuple4.getMetadata().setStart(PointInTime.currentPointInTime());

        ProbabilisticTuple<ITimeInterval> tuple5 = new ProbabilisticTuple<>(attributes5, true);
        tuple5.setMetadata(new TimeIntervalProbabilistic());
        tuple5.getMetadata().setStart(PointInTime.currentPointInTime());

        ProbabilisticTuple<ITimeInterval> tuple6 = new ProbabilisticTuple<>(attributes6, true);
        tuple6.setMetadata(new TimeIntervalProbabilistic());
        tuple6.getMetadata().setStart(PointInTime.currentPointInTime());

        EMPO<ITimeInterval> em = new EMPO<ITimeInterval>(new int[] { 0, 1, 3 }, 3);
        for (int i = 0; i < 10; i++) {
            em.process_next(tuple1.clone(), 0);
            em.process_next(tuple2.clone(), 0);
            em.process_next(tuple3.clone(), 0);
            em.process_next(tuple4.clone(), 0);
            em.process_next(tuple5.clone(), 0);
            em.process_next(tuple6.clone(), 0);
        }
    }
}
