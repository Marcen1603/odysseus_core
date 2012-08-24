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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.MultivariantCovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.math.PBox;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Probabilistic implements IProbabilistic {
    @SuppressWarnings("unused")
    private final Map<Integer, PBox>     pBoxes           = new HashMap<Integer, PBox>();

    /**
	 * 
	 */
    private static final long            serialVersionUID = -147594856639774242L;
    private MultivariantCovarianceMatrix covarianceMatrices;
    /** Tuple existence probability */
    private double                       existence;

    public Probabilistic() {
        this.existence = 1.0;
        this.covarianceMatrices = new MultivariantCovarianceMatrix(0);
    }

    public Probabilistic(final int size) {
        this.existence = 1.0;
        this.covarianceMatrices = new MultivariantCovarianceMatrix(size);
    }

    public Probabilistic(final Probabilistic probability) {
        this.existence = probability.existence;
        this.covarianceMatrices = probability.covarianceMatrices.clone();

    }

    public MultivariantCovarianceMatrix getCovarianceMatrices() {
        return covarianceMatrices;
    }

    @Override
    public void setCovarianceMatrices(MultivariantCovarianceMatrix covarianceMatrices) {
        this.covarianceMatrices = covarianceMatrices;
    }

    public CovarianceMatrix getCovarianceMatrix(byte id) {
        return covarianceMatrices.get(id);
    }

    @Override
    public String csvToString() {
        return "" + this.covarianceMatrices;
    }

    @Override
    public String csvToString(final boolean withMetada) {
        return this.csvToString();
    }

    @Override
    public String getCSVHeader() {
        return "probability";
    }

    @Override
    public IProbabilistic clone() {
        return new Probabilistic(this);
    }

    @Override
    public double getExistence() {
        return this.existence;
    }

    @Override
    public void setExistence(double existence) {
        this.existence = existence;
    }

    @Override
    public String toString() {
        return "TEP: " + this.existence;
    }

}
