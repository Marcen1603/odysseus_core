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
package de.uniol.inf.is.odysseus.image.functions;

import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToImageMatrixFunction extends AbstractFunction<Image> {
    /**
     * 
     */
    private static final long serialVersionUID = 8688410794703166746L;

    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.MATRIXS };

    /**
 * 
 */
    public ToImageMatrixFunction() {
        super("toImage", 1, ToImageMatrixFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getValue() {
        final double data[][] = this.getInputValue(0);
        Objects.requireNonNull(data);
        Preconditions.checkArgument(data.length > 0, "Invalid dimension");
        Preconditions.checkArgument(data[0].length > 0, "Invalid dimension");
        Image image = new Image(data);
        return image;
    }
}
