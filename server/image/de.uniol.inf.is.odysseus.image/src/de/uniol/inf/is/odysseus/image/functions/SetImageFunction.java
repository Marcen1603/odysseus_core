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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SetImageFunction extends AbstractFunction<Image> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7634166024232641502L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE }, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    /**
 * 
 */
    public SetImageFunction() {
        super("set", 4, SetImageFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
    }

    @Override
	public Image getValue() {
        final Image image = (Image) this.getInputValue(0);
        final int x = this.getNumericalInputValue(1).intValue();
        final int y = this.getNumericalInputValue(2).intValue();
        final double value = this.getNumericalInputValue(3);

		Objects.requireNonNull(image);
		
        image.set(x, y, (int)value);
        return image;
    }
}
