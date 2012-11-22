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
package de.uniol.inf.is.odysseus.core.server.mep.functions;

import java.util.Calendar;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * Returns the month
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class MonthFunction extends AbstractFunction<Integer> {

    /**
     * 
     */
    private static final long          serialVersionUID = -3089145220450028398L;
    private static final SDFDatatype[] accTypes         = new SDFDatatype[] {};

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= 0) {
            throw new IllegalArgumentException(this.getSymbol() + " has no argument(s).");
        }
        return accTypes;
    }

    @Override
    public String getSymbol() {
        return "month";
    }

    @Override
    public Integer getValue() {
        // The first month is 1 not 0
        return Calendar.getInstance().get(Calendar.MONTH + 1);
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.INTEGER;
    }

}
