/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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

package de.uniol.inf.is.odysseus.mep.matrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MatrixToStringFunction extends AbstractFunction<String> {

    /**
     * 
     */
    private static final long serialVersionUID = -7960728964867547435L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.MATRIXS };

    public MatrixToStringFunction() {
        super("toString", 1, accTypes, SDFDatatype.STRING);
    }

    @Override
    public String getValue() {
        double[][] data = (double[][]) getInputValue(0);
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < data.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("{");
            for (int j = 0; j < data[i].length; j++) {
                if (j != 0) {
                    sb.append(", ");
                }
                sb.append(data[i][j]);
            }
            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }
}
