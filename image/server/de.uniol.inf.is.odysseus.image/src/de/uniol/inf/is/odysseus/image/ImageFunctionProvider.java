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
package de.uniol.inf.is.odysseus.image;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.image.functions.CountFacesFunction;
import de.uniol.inf.is.odysseus.image.functions.FillImageFunction;
import de.uniol.inf.is.odysseus.image.functions.GetImageFunction;
import de.uniol.inf.is.odysseus.image.functions.InverseImageFunction;
import de.uniol.inf.is.odysseus.image.functions.ResizeImageFunction;
import de.uniol.inf.is.odysseus.image.functions.RotateImageFunction;
import de.uniol.inf.is.odysseus.image.functions.SetImageFunction;
import de.uniol.inf.is.odysseus.image.functions.SubImageFunction;
import de.uniol.inf.is.odysseus.image.functions.ToImageFromBufferFunction;
import de.uniol.inf.is.odysseus.image.functions.ToImageFunction;
import de.uniol.inf.is.odysseus.image.functions.ToImageMatrixFunction;
import de.uniol.inf.is.odysseus.image.functions.ToMatrixFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ImageFunctionProvider implements IFunctionProvider {
    private static final Logger LOG = LoggerFactory.getLogger(ImageFunctionProvider.class);

    public ImageFunctionProvider() {
    }

    @Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<IMepFunction<?>>();
        functions.add(new RotateImageFunction());
        functions.add(new SubImageFunction());
        functions.add(new ResizeImageFunction());
        functions.add(new FillImageFunction());
        functions.add(new InverseImageFunction());
        functions.add(new ToImageFunction());
        functions.add(new ToImageMatrixFunction());
        functions.add(new ToMatrixFunction());
        functions.add(new SetImageFunction());
        functions.add(new GetImageFunction());
        functions.add(new CountFacesFunction());
        functions.add(new ToImageFromBufferFunction());
        
        ImageFunctionProvider.LOG.trace(String.format("Register functions: %s", functions));
        return functions;
    }

}
