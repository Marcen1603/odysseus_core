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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NthPartialAggregate<T> implements IPartialAggregate<T> {
    T elem;
    int n;

    public NthPartialAggregate(T elem) {
        setElem(elem);
        setN(1);
    }

    public NthPartialAggregate(IPartialAggregate<T> p) {
        setElem(((NthPartialAggregate<T>) p).getElem());
        setN(((NthPartialAggregate<T>) p).getN());
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public T getElem() {
        return elem;
    }

    public void setElem(T elem) {
        this.elem = elem;
    }

    public void addElem(T elem) {
        this.setElem(elem);
        this.setN(getN() + 1);

    }

    @Override
    public String toString() {
        return "" + elem;
    }

    @Override
    public NthPartialAggregate<T> clone() {
        return new NthPartialAggregate<T>(this);
    }

}
