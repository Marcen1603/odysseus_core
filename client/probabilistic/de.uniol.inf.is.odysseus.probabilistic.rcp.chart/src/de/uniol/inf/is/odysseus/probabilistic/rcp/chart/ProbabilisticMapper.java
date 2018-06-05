/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package de.uniol.inf.is.odysseus.probabilistic.rcp.chart;

import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticMapper implements net.ericaro.surfaceplotter.Mapper {

    /** The mixtures. */
    private MultivariateMixtureDistribution distribution;

    /**
     * 
     * @param mixture
     *            The normal distribution mixture
     */
    public final void setup(final MultivariateMixtureDistribution mixture) {
        this.distribution = mixture;
    }

    /*
     * 
     * @see net.ericaro.surfaceplotter.Mapper#f1(float, float)
     */
    @Override
    public final float f1(final float x, final float y) {
        return (float) this.distribution.density(new double[] { x, y });
    }

    /*
     * 
     * @see net.ericaro.surfaceplotter.Mapper#f2(float, float)
     */
    @Override
    public final float f2(final float x, final float y) {
        return 0;
    }
}
