/*
 * Copyright 2013 The Odysseus Team
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

package de.uniol.inf.is.odysseus.probabilistic.math.genz;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ChlrdrResult {
    public Matrix ch;
    public Matrix as;
    public Matrix bs;

    /**
     * 
     * @param ch
     * @param as
     * @param bs
     */
    public ChlrdrResult(final Matrix ch, final Matrix as, final Matrix bs) {
        super();
        this.ch = ch;
        this.as = as;
        this.bs = bs;
    }
}
