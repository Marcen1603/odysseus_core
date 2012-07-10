/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.salsa.metadata.impl;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.salsa.metadata.Quality;

public class QualityRelationalTuple<T extends Quality> extends Tuple<T> implements
        Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6442070593364715220L;

    public QualityRelationalTuple() {
        super();
    }

    public QualityRelationalTuple(int attributeCount) {
        super(attributeCount, false);
    }

    public QualityRelationalTuple(QualityRelationalTuple<T> tuple) {
        super(tuple);
    }

    @Override
    public QualityRelationalTuple<T> clone() {
        return new QualityRelationalTuple<T>(this);
    }
}
