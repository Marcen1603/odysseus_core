/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.image.common.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
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

    public static final SDFDatatype IMAGE = new SDFDatatype("Image");

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
