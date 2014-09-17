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
package de.uniol.inf.is.odysseus.wrapper.cameras.sdf.schema;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SDFBaseImageDatatype extends SDFDatatype {
    /**
     * 
     */
    private static final long serialVersionUID = -7712872213113223483L;

    public SDFBaseImageDatatype(final String URI) {
        super(URI);
    }

    public SDFBaseImageDatatype(final SDFDatatype sdfDatatype) {
        super(sdfDatatype);
    }

    public SDFBaseImageDatatype(final String datatypeName, final KindOfDatatype type, final SDFSchema schema) {
        super(datatypeName, type, schema);
    }

    public static final SDFDatatype BASE_IMAGE = new SDFDatatype("BaseImage");

    public boolean isImage() {
        return this.getURI().equals(SDFBaseImageDatatype.BASE_IMAGE.getURI());
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean compatibleTo(final SDFDatatype other) 
    {
        if (other instanceof SDFBaseImageDatatype) 
        {
            final SDFBaseImageDatatype otherDatatype = (SDFBaseImageDatatype) other;
            if (getURI().equals(otherDatatype.getURI()))
            if (this.isImage() && otherDatatype.isImage()) 
            {
                return true;
            }
        }
        return super.compatibleTo(other);
    }

    public static List<SDFDatatype> getTypes() {
        final List<SDFDatatype> types = new ArrayList<>();
        types.addAll(SDFDatatype.getTypes());
        types.add(SDFBaseImageDatatype.BASE_IMAGE);

        return types;
    }
}
