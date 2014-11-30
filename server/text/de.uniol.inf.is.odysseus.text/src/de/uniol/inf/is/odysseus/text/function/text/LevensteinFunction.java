/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.text.function.text;

import org.apache.commons.lang3.StringUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to compute the levenstein distance of a string
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class LevensteinFunction extends AbstractFunction<Integer> {

    private static final long serialVersionUID = 5254931226986934896L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.STRING }, { SDFDatatype.STRING } };

    public LevensteinFunction() {
        super("levenstein", 2, LevensteinFunction.ACC_TYPES, SDFDatatype.INTEGER);
    }

    @Override
    public Integer getValue() {
        return new Integer(StringUtils.getLevenshteinDistance(this.getInputValue(0).toString(), this.getInputValue(1).toString()));
    }

}
