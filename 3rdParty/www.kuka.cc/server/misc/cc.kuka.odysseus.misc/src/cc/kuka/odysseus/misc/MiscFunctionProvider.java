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
package cc.kuka.odysseus.misc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.misc.function.miscellaneous.CelsiusToFahrenheitFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.DewPointFastFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.DewPointFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.FahrenheitToCelsiusFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.FahrenheitToKelvinFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.KelvinToFahrenheitFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.KilometerPerHourToMeterPerSecondFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.KilometerPerHourToMilesPerHourFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.MeterPerSecondToKilometerPerHourFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.MilesPerHourToKilometerPerHourFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.PythagoreanExpectationFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.PythagoreanExpectationWithExponentFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.SpeedOfLightFunction;
import cc.kuka.odysseus.misc.function.miscellaneous.SpeedOfSoundFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MiscFunctionProvider implements IFunctionProvider {
    private static final Logger LOG = LoggerFactory.getLogger(MiscFunctionProvider.class);

    public MiscFunctionProvider() {

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<>();
        try {
            functions.add(new CelsiusToFahrenheitFunction());
            functions.add(new FahrenheitToCelsiusFunction());
            functions.add(new FahrenheitToKelvinFunction());
            functions.add(new KelvinToFahrenheitFunction());
            functions.add(new KilometerPerHourToMeterPerSecondFunction());
            functions.add(new KilometerPerHourToMilesPerHourFunction());
            functions.add(new MeterPerSecondToKilometerPerHourFunction());
            functions.add(new MilesPerHourToKilometerPerHourFunction());
            functions.add(new SpeedOfSoundFunction());
            functions.add(new SpeedOfLightFunction());

            functions.add(new DewPointFunction());
            functions.add(new DewPointFastFunction());

            functions.add(new PythagoreanExpectationFunction());
            functions.add(new PythagoreanExpectationWithExponentFunction());

        }
        catch (final Exception e) {
            MiscFunctionProvider.LOG.error(e.getMessage(), e);
        }
        return functions;
    }

}
