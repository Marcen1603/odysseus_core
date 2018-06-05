/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.signal.physicaloperator.aggregation.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SpectralCentroidPartialAggregate<R> extends AbstractPartialAggregate<R> {

    /**
     *
     */
    private static final long serialVersionUID = -5799788273428657629L;
    private double sumCentroid;
    private double sumIntensities;

    public SpectralCentroidPartialAggregate() {
    }

    public SpectralCentroidPartialAggregate(final Number magnitude, final Number centerFrequency) {
        this.add(magnitude, centerFrequency);
    }

    public SpectralCentroidPartialAggregate(final SpectralCentroidPartialAggregate<R> partialAggregate) {
        this.sumCentroid = partialAggregate.sumCentroid;
        this.sumIntensities = partialAggregate.sumIntensities;
    }

    public Double getAggValue() {
        return new Double(this.sumCentroid / this.sumIntensities);
    }

    public void add(final Number magnitude, final Number centerFrequency) {
        if ((magnitude != null) && (centerFrequency != null)) {
            this.sumCentroid += centerFrequency.doubleValue() * magnitude.doubleValue();
            this.sumIntensities += magnitude.doubleValue();
        }
    }

    public void add(final SpectralCentroidPartialAggregate<?> value) {
        this.sumCentroid += value.sumCentroid;
        this.sumIntensities += value.sumIntensities;
    }

    @Override
    public SpectralCentroidPartialAggregate<R> clone() {
        return new SpectralCentroidPartialAggregate<>(this);
    }

    @Override
    public String toString() {
        return "SpectralCentroid= " + this.getAggValue();
    }

    //@SuppressWarnings("boxing")
    public static void main(final String[] args) {
        final SpectralCentroidPartialAggregate<?> agg = new SpectralCentroidPartialAggregate<>();
        agg.add(1.0, 1.0);
        agg.add(4.0, 2.0);
        agg.add(10.0, 3.0);
        for (int i = 0; i < 10000; i++) {
            agg.add(10.0, (double) i);
        }
        assert (agg.getAggValue() == 0.0);
        System.out.println(agg);
        agg.add(5.0, 5.0);
        assert (agg.getAggValue() == 5.0);
        System.out.println(agg);
    }

}
