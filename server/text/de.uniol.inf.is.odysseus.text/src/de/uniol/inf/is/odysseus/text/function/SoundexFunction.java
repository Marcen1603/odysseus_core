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
package de.uniol.inf.is.odysseus.text.function;

import org.apache.commons.codec.language.Soundex;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to compute the soundex of a string
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 * 
 */
public class SoundexFunction extends AbstractFunction<String> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3286522655596260036L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING }, { SDFDatatype.STRING } };

    public SoundexFunction() {
        super("soundex", 2, accTypes, SDFDatatype.STRING);
    }

    @Override
    public String getValue() {
        Soundex soundex = new Soundex();
        String soundexValue = soundex.soundex(getInputValue(0).toString());
        return soundexValue;
    }
}
