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
package cc.kuka.odysseus.signal;

import java.util.ArrayList;
import java.util.List;

import cc.kuka.odysseus.signal.function.math.ComplexDivideOperator;
import cc.kuka.odysseus.signal.function.math.ComplexMinusOperator;
import cc.kuka.odysseus.signal.function.math.ComplexMultiplyOperator;
import cc.kuka.odysseus.signal.function.math.ComplexPlusOperator;
import cc.kuka.odysseus.signal.function.signal.ExponentialFunction;
import cc.kuka.odysseus.signal.function.signal.ImaginaryFunction;
import cc.kuka.odysseus.signal.function.signal.MagnitudeFunction;
import cc.kuka.odysseus.signal.function.signal.RealFunction;
import cc.kuka.odysseus.signal.function.transform.ToComplexFunction;
import cc.kuka.odysseus.signal.function.transform.ToStringFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SignalFunctionProvider implements IFunctionProvider {

    public SignalFunctionProvider() {
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<>();
        functions.add(new ComplexDivideOperator());
        functions.add(new ComplexMultiplyOperator());
        functions.add(new ComplexPlusOperator());
        functions.add(new ComplexMinusOperator());

        functions.add(new RealFunction());
        functions.add(new ImaginaryFunction());
        functions.add(new MagnitudeFunction());
        functions.add(new ExponentialFunction());

        functions.add(new ToComplexFunction());
        functions.add(new ToStringFunction());

        return functions;
    }

}
