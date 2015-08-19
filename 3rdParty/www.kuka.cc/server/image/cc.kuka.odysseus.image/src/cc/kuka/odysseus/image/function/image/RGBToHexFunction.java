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
package cc.kuka.odysseus.image.function.image;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RGBToHexFunction extends AbstractFunction<String> {
    /**
     *
     */
    private static final long serialVersionUID = 2137175975337602714L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    /**
     *
     */
    public RGBToHexFunction() {
        super("RGBToHex", 3, RGBToHexFunction.ACC_TYPES, SDFDatatype.STRING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        final int r = this.getNumericalInputValue(0).intValue();
        final int g = this.getNumericalInputValue(1).intValue();
        final int b = this.getNumericalInputValue(2).intValue();

        return Integer.toHexString((r * 65536) + (g * 256) + b);
    }
}
