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
package de.uniol.inf.is.odysseus.wrapper.opcda.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class SDFOPCDADatatype extends SDFDatatype {

    /**
     * 
     */
    private static final long serialVersionUID = 7850101896218344675L;
    public static final SDFDatatype OPCVALUE = new SDFOPCDADatatype("OPCValue");

    /**
     * 
     * Class constructor.
     *
     * @param URI
     */
    public SDFOPCDADatatype(final String URI) {
        super(URI, true);
    }

    /**
     * 
     * Class constructor.
     *
     * @param sdfDatatype
     */
    public SDFOPCDADatatype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    /**
     * 
     * Class constructor.
     *
     * @param datatypeName
     * @param type
     * @param schema
     */
    public SDFOPCDADatatype(final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
        super(datatypeName, type, schema, true);
    }

    /**
     * 
     * Class constructor.
     *
     * @param datatypeName
     * @param type
     * @param subType
     */
    public SDFOPCDADatatype(final String datatypeName, final KindOfDatatype type, final SDFDatatype subType) {
        super(datatypeName, type, subType, true);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isNumeric() {
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean compatibleTo(final SDFDatatype other) {
        if (other instanceof SDFOPCDADatatype) {
            return true;
        }
        return super.compatibleTo(other);
    }

}
