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
package de.uniol.inf.is.odysseus.mep.functions.string;


/**
 * Returns a new string that is a substring of the value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SubStringFunction2 extends SubStringFunction {

	private static final long serialVersionUID = 3177285577975351278L;

	@Override
    public int getArity() {
        return 2;
    }

    @Override
    public String getValue() {
        return ((String) getInputValue(0)).substring(getNumericalInputValue(1).intValue());
    }

}
