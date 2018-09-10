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

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToImageFunction extends AbstractFunction<Image> {
    /**
     * 
     */
    private static final long serialVersionUID = -6078416764818576545L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public ToImageFunction() {
        super("toImage", 2, ToImageFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    @Override
    public Image getValue() {
        final int width = this.getNumericalInputValue(0).intValue();
        final int height = this.getNumericalInputValue(1).intValue();
		Preconditions.checkArgument(width > 0, "Invalid dimension");
		Preconditions.checkArgument(height > 0, "Invalid dimension");
        return new Image(width, height);
    }
}
