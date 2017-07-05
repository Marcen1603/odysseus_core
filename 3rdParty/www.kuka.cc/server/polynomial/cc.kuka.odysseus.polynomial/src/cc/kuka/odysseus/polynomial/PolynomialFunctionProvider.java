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
package cc.kuka.odysseus.polynomial;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.polynomial.function.math.MinusOperator;
import cc.kuka.odysseus.polynomial.function.math.MultiplicationOperator;
import cc.kuka.odysseus.polynomial.function.math.PlusOperator;
import cc.kuka.odysseus.polynomial.function.polynomial.ComposeFunction;
import cc.kuka.odysseus.polynomial.function.polynomial.DifferentiateFunction;
import cc.kuka.odysseus.polynomial.function.polynomial.EvalFunction;
import cc.kuka.odysseus.polynomial.function.polynomial.IntegrateFunction;
import cc.kuka.odysseus.polynomial.function.transform.ToPolynomialFunction;
import cc.kuka.odysseus.polynomial.function.transform.ToStringFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class PolynomialFunctionProvider implements IFunctionProvider {
    private static final Logger LOG = LoggerFactory.getLogger(PolynomialFunctionProvider.class);

    public PolynomialFunctionProvider() {

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<>();
        try {
            functions.add(new MinusOperator());
            functions.add(new PlusOperator());
            functions.add(new MultiplicationOperator());

            functions.add(new EvalFunction());
            functions.add(new ComposeFunction());
            functions.add(new DifferentiateFunction());
            functions.add(new IntegrateFunction());

            functions.add(new ToStringFunction());
            functions.add(new ToPolynomialFunction());
        }
        catch (final Exception e) {
            PolynomialFunctionProvider.LOG.error(e.getMessage(), e);
        }
        return functions;
    }
}
