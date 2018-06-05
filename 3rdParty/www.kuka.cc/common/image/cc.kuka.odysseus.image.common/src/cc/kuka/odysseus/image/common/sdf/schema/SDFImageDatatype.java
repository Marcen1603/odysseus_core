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
package cc.kuka.odysseus.image.common.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SDFImageDatatype extends SDFDatatype {
    /**
     *
     */
    private static final long serialVersionUID = -7712872213113223483L;

    public SDFImageDatatype(final String URI) {
        super(URI);
    }

    public SDFImageDatatype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    public SDFImageDatatype(final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
        super(datatypeName, type, schema);
    }

    public static final SDFImageDatatype IMAGE = new SDFImageDatatype("Image");

    public boolean isImage() {
        return this.getURI().equals(SDFImageDatatype.IMAGE.getURI());
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean compatibleTo(final SDFDatatype other) {
        if (other instanceof SDFImageDatatype) {
            final SDFImageDatatype otherDatatype = (SDFImageDatatype) other;
            if (this.isImage() && otherDatatype.isImage()) {
                return true;
            }
        }
        return super.compatibleTo(other);
    }

    public static List<SDFDatatype> getTypes() {
        final List<SDFDatatype> types = new ArrayList<>();
        types.addAll(SDFDatatype.getTypes());
        types.add(SDFImageDatatype.IMAGE);

        return types;
    }
}
