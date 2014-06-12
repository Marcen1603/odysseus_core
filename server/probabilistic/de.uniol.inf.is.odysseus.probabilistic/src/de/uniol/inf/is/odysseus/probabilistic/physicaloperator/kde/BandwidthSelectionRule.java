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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.kde;

import org.apache.commons.math3.util.FastMath;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class BandwidthSelectionRule {
    public static double scott(final int dataPoints, final int dimension) {
        return FastMath.pow(dataPoints, -1.0 / (dimension + 4.0));
    }

    public static double silverman(final int dataPoints, final int dimension) {
        return FastMath.pow((dataPoints * (dimension + 2.0)) / 4.0, (-1.0 / (dimension + 4.0)));
    }
}
