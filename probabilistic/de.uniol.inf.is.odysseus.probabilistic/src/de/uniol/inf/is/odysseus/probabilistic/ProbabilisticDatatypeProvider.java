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
package de.uniol.inf.is.odysseus.probabilistic;

import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticDatatypeProvider {
    public static IDataDictionary datadictionary = null;

    protected void bindDataDictionary(final IDataDictionary dd) {
        ProbabilisticDatatypeProvider.datadictionary = dd;
        try {
            ProbabilisticDatatypeProvider.datadictionary.addDatatype(
                    SDFProbabilisticDatatype.MULTIVARIATE_COVARIANCE_MATRIX.getURI(),
                    SDFProbabilisticDatatype.MULTIVARIATE_COVARIANCE_MATRIX);
            ProbabilisticDatatypeProvider.datadictionary.addDatatype(
                    SDFProbabilisticDatatype.COVARIANCE_MATRIX.getURI(),
                    SDFProbabilisticDatatype.COVARIANCE_MATRIX);
            ProbabilisticDatatypeProvider.datadictionary.addDatatype(
                    SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI(),
                    SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
            ProbabilisticDatatypeProvider.datadictionary.addDatatype(
                    SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE.getURI(),
                    SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
        }
        catch (final DataDictionaryException e) {
            e.printStackTrace();
        }
    }

    protected void unbindDataDictionary(final IDataDictionary dd) {
        try {
            dd.removeDatatype(SDFProbabilisticDatatype.MULTIVARIATE_COVARIANCE_MATRIX.getURI());
            dd.removeDatatype(SDFProbabilisticDatatype.COVARIANCE_MATRIX.getURI());
            dd.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI());
            dd.removeDatatype(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE.getURI());
        }
        catch (final DataDictionaryException e) {
            e.printStackTrace();
        }
        ProbabilisticDatatypeProvider.datadictionary = null;
    }
}
