/**********************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ListPlusOperator extends AbstractBinaryOperator<List<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -3446257237116562102L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists(), SDFDatatype.getLists() };

    public ListPlusOperator() {
        super("+", accTypes, SDFDatatype.LIST);
    }

    @Override
    public int getPrecedence() {
        return 6;
    }

    @Override
    public List<?> getValue() {
        List<?> a = (List<?>) getInputValue(0);
        if(a == null) {
        	a = Collections.emptyList();
        }
        List<?> b = (List<?>) getInputValue(1);
        if(b == null) {
        	b = Collections.emptyList();
        }
        List<Object> list = new ArrayList<>(a.size() + b.size());
        list.addAll(a);
        list.addAll(b);
        return list;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean isLeftDistributiveWith(IOperator<List<?>> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(IOperator<List<?>> operator) {
        return false;
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }
}
