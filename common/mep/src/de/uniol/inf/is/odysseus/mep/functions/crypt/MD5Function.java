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
package de.uniol.inf.is.odysseus.mep.functions.crypt;

/**
 * Returns the MD5 hash sum of a string.
 *
 * @author Christian Kuka <christian@kuka.cc>
 */
public class MD5Function extends AbstractDigestFunction {

    private static final long serialVersionUID = 2100227355791982930L;

    public MD5Function() {
        super("MD5");
    }

}
