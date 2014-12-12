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

import org.apache.commons.codec.language.ColognePhonetic;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to compute the cologne phonetic of a string
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ColognePhoneticFunction extends AbstractFunction<String> {

    private static final long serialVersionUID = 1265565609372371657L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.STRING } };

    public ColognePhoneticFunction() {
        super("colognephonetic", 1, ColognePhoneticFunction.ACC_TYPES, SDFDatatype.STRING);
    }

    @Override
    public String getValue() {
        final ColognePhonetic colognePhonetic = new ColognePhonetic();
        final String colognePhoneticValue = colognePhonetic.colognePhonetic(this.getInputValue(0).toString());
        return colognePhoneticValue;
    }

}