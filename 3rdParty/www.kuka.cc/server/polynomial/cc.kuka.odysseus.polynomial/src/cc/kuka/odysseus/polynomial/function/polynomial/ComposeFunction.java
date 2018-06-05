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
package cc.kuka.odysseus.polynomial.function.polynomial;

import cc.kuka.odysseus.polynomial.datatype.Polynomial;
import cc.kuka.odysseus.polynomial.sdf.schema.SDFPolynomialDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ComposeFunction extends AbstractFunction<Polynomial> {
    /**
     *
     */
    private static final long serialVersionUID = -5251590247989390247L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFPolynomialDatatype.POLYNOMIAL }, { SDFPolynomialDatatype.POLYNOMIAL } };

    public ComposeFunction() {
        super("comp", 2, ComposeFunction.accTypes, SDFPolynomialDatatype.POLYNOMIAL);
    }

    @Override
    public Polynomial getValue() {
        final Polynomial a = this.getInputValue(0);
        final Polynomial b = this.getInputValue(1);
        return a.compose(b);
    }
}
