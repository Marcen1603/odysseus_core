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
package cc.kuka.odysseus.interval;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.interval.function.interval.AfterFunction;
import cc.kuka.odysseus.interval.function.interval.BeforeFunction;
import cc.kuka.odysseus.interval.function.interval.ContainsFunction;
import cc.kuka.odysseus.interval.function.interval.DuringFunction;
import cc.kuka.odysseus.interval.function.interval.EqualsFunction;
import cc.kuka.odysseus.interval.function.interval.FinishesFunction;
import cc.kuka.odysseus.interval.function.interval.InfFunction;
import cc.kuka.odysseus.interval.function.interval.IntervalDifferenceFunction;
import cc.kuka.odysseus.interval.function.interval.IntervalIntersectionFunction;
import cc.kuka.odysseus.interval.function.interval.IntervalUnionFunction;
import cc.kuka.odysseus.interval.function.interval.MeetsFunction;
import cc.kuka.odysseus.interval.function.interval.OverlapsFunction;
import cc.kuka.odysseus.interval.function.interval.StartsFunction;
import cc.kuka.odysseus.interval.function.interval.SupFunction;
import cc.kuka.odysseus.interval.function.math.IntervalDivisionOperator;
import cc.kuka.odysseus.interval.function.math.IntervalMinusOperator;
import cc.kuka.odysseus.interval.function.math.IntervalMultiplicationOperator;
import cc.kuka.odysseus.interval.function.math.IntervalPlusOperator;
import cc.kuka.odysseus.interval.function.math.IntervalPowerOperator;
import cc.kuka.odysseus.interval.function.transform.ToIntervalFunction;
import cc.kuka.odysseus.interval.function.transform.ToStringFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class IntervalFunctionProvider implements IFunctionProvider {
    private static final Logger LOG = LoggerFactory.getLogger(IntervalFunctionProvider.class);

    public IntervalFunctionProvider() {

    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<>();
        try {
            functions.add(new IntervalMinusOperator());
            functions.add(new IntervalPlusOperator());
            functions.add(new IntervalMultiplicationOperator());
            functions.add(new IntervalDivisionOperator());
            functions.add(new IntervalPowerOperator());

            functions.add(new IntervalUnionFunction());
            functions.add(new IntervalIntersectionFunction());
            functions.add(new IntervalDifferenceFunction());

            functions.add(new InfFunction());
            functions.add(new SupFunction());

            functions.add(new ToStringFunction());
            functions.add(new ToIntervalFunction());

            functions.add(new AfterFunction());
            functions.add(new BeforeFunction());
            functions.add(new ContainsFunction());
            functions.add(new DuringFunction());
            functions.add(new EqualsFunction());
            functions.add(new FinishesFunction());
            functions.add(new MeetsFunction());
            functions.add(new OverlapsFunction());
            functions.add(new StartsFunction());

        }
        catch (final Exception e) {
            IntervalFunctionProvider.LOG.error(e.getMessage(), e);
        }
        return functions;
    }
}
