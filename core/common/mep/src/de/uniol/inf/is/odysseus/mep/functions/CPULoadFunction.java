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
package de.uniol.inf.is.odysseus.mep.functions;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class CPULoadFunction extends AbstractFunction<Double> {

    /**
     * 
     */
    private static final long serialVersionUID = 4684887918311600538L;

    public CPULoadFunction() {
        super("load", 0, null, SDFDatatype.DOUBLE, false);
    }

    @Override
    public Double getValue() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        return operatingSystemMXBean.getSystemLoadAverage();
    }
}
