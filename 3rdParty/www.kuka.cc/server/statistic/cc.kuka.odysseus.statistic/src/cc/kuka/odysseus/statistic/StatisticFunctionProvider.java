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
package cc.kuka.odysseus.statistic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.statistic.function.BinominalCoefficientFunction;
import cc.kuka.odysseus.statistic.function.MultinominalCoefficientFunction;
import cc.kuka.odysseus.statistic.function.distance.BrayCurtisDistanceFunction;
import cc.kuka.odysseus.statistic.function.distance.BrayCurtisDistanceFunctionMatrix;
import cc.kuka.odysseus.statistic.function.distance.BrayCurtisDistanceFunctionVector;
import cc.kuka.odysseus.statistic.function.distance.ChebyshevDistanceFunction;
import cc.kuka.odysseus.statistic.function.distance.ChebyshevDistanceFunctionMatrix;
import cc.kuka.odysseus.statistic.function.distance.ChebyshevDistanceFunctionVector;
import cc.kuka.odysseus.statistic.function.distance.EuclideanDistanceFunction;
import cc.kuka.odysseus.statistic.function.distance.EuclideanDistanceFunctionMatrix;
import cc.kuka.odysseus.statistic.function.distance.EuclideanDistanceFunctionVector;
import cc.kuka.odysseus.statistic.function.distance.JaccardDistanceFunction;
import cc.kuka.odysseus.statistic.function.distance.JaccardDistanceFunctionMatrix;
import cc.kuka.odysseus.statistic.function.distance.JaccardDistanceFunctionVector;
import cc.kuka.odysseus.statistic.function.distance.ManhattanDistanceFunction;
import cc.kuka.odysseus.statistic.function.distance.ManhattanDistanceFunctionMatrix;
import cc.kuka.odysseus.statistic.function.distance.ManhattanDistanceFunctionVector;
import cc.kuka.odysseus.statistic.function.distance.MinkowskiDistanceFunction;
import cc.kuka.odysseus.statistic.function.distance.MinkowskiDistanceFunctionMatrix;
import cc.kuka.odysseus.statistic.function.distance.MinkowskiDistanceFunctionVector;
import cc.kuka.odysseus.statistic.function.distribution.BetaCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.BetaPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.BinomialCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.BinomialPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.CauchyCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.CauchyPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.ChiSquaredCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.ChiSquaredPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.ExponentialCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.ExponentialPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.FCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.FPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.GammaCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.GammaPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.HypergeometricCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.HypergeometricPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.LogNormalCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.LogNormalPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.NormalCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.NormalPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.PoissonCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.PoissonPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.TCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.TPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.WeibullCDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.WeibullPDFFunction;
import cc.kuka.odysseus.statistic.function.distribution.ZScoreFunction;
import cc.kuka.odysseus.statistic.function.distribution.ZScoreFunctionVector;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class StatisticFunctionProvider implements IFunctionProvider {
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(StatisticFunctionProvider.class);

    /**
     * Default constructor.
     */
    public StatisticFunctionProvider() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IMepFunction<?>> getFunctions() {
        final List<IMepFunction<?>> functions = new ArrayList<>();
        try {
            functions.add(new BinominalCoefficientFunction());
            functions.add(new MultinominalCoefficientFunction());
            // Distances
            functions.add(new EuclideanDistanceFunction());
            functions.add(new EuclideanDistanceFunctionVector());
            functions.add(new EuclideanDistanceFunctionMatrix());

            functions.add(new ManhattanDistanceFunction());
            functions.add(new ManhattanDistanceFunctionVector());
            functions.add(new ManhattanDistanceFunctionMatrix());

            functions.add(new MinkowskiDistanceFunction());
            functions.add(new MinkowskiDistanceFunctionVector());
            functions.add(new MinkowskiDistanceFunctionMatrix());

            functions.add(new JaccardDistanceFunction());
            functions.add(new JaccardDistanceFunctionVector());
            functions.add(new JaccardDistanceFunctionMatrix());

            functions.add(new BrayCurtisDistanceFunction());
            functions.add(new BrayCurtisDistanceFunctionVector());
            functions.add(new BrayCurtisDistanceFunctionMatrix());

            functions.add(new ChebyshevDistanceFunction());
            functions.add(new ChebyshevDistanceFunctionVector());
            functions.add(new ChebyshevDistanceFunctionMatrix());

            // Distributions
            functions.add(new BetaCDFFunction());
            functions.add(new BetaPDFFunction());

            functions.add(new BinomialCDFFunction());
            functions.add(new BinomialPDFFunction());

            functions.add(new CauchyCDFFunction());
            functions.add(new CauchyPDFFunction());

            functions.add(new ChiSquaredCDFFunction());
            functions.add(new ChiSquaredPDFFunction());

            functions.add(new ExponentialCDFFunction());
            functions.add(new ExponentialPDFFunction());

            functions.add(new FCDFFunction());
            functions.add(new FPDFFunction());

            functions.add(new GammaCDFFunction());
            functions.add(new GammaPDFFunction());

            functions.add(new HypergeometricCDFFunction());
            functions.add(new HypergeometricPDFFunction());

            functions.add(new LogNormalCDFFunction());
            functions.add(new LogNormalPDFFunction());

            functions.add(new NormalCDFFunction());
            functions.add(new NormalPDFFunction());

            functions.add(new PoissonCDFFunction());
            functions.add(new PoissonPDFFunction());

            functions.add(new TCDFFunction());
            functions.add(new TPDFFunction());

            functions.add(new WeibullCDFFunction());
            functions.add(new WeibullPDFFunction());

            functions.add(new ZScoreFunction());
            functions.add(new ZScoreFunctionVector());
        }
        catch (final Exception e) {
            StatisticFunctionProvider.LOG.error(e.getMessage(), e);
        }
        return functions;
    }
}
