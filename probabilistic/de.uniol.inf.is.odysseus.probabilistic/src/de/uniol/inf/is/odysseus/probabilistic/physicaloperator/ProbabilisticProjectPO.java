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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticProjectPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {
    Logger                  logger = LoggerFactory.getLogger(ProbabilisticProjectPO.class);
    private final int[]     restrictList;
    private final SDFSchema inputSchema;
    private Integer[]       continuousAttributes;
    private RealMatrix[]    projectMatrixes;
    private Object[]        projectedOutContinuousAttributes;

    // private final int[] restrictMatrix

    public ProbabilisticProjectPO(SDFSchema inputSchema, final int[] restrictList) {
        this.inputSchema = inputSchema;
        this.restrictList = restrictList;
        init();
    }

    public ProbabilisticProjectPO(final ProbabilisticProjectPO<T> probabilisticProjectPO) {
        super();
        final int length = probabilisticProjectPO.restrictList.length;
        this.inputSchema = probabilisticProjectPO.inputSchema.clone();
        this.restrictList = new int[length];
        System.arraycopy(probabilisticProjectPO.restrictList, 0, this.restrictList, 0, length);
        init();
    }

    @Override
    public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    @Override
    protected void process_next(final Tuple<T> object, final int port) {
        // TODO Integrate over projected attributes
        final IProbabilistic probabilistic = (IProbabilistic) object.getMetadata();

        // TODO integrate/approximate over projected out attributes to calc TEP
        final ProbabilisticTuple<T> out = ((ProbabilisticTuple) object).restrict(this.restrictList, false);

        Object[] newAttrs = new Object[this.restrictList.length];
        List processedContinuousAttributes = new ArrayList(this.continuousAttributes.length);
        for (int i = 0; i < this.restrictList.length; i++) {
            int index = Arrays.binarySearch(this.continuousAttributes, this.restrictList[i]);
            if (index >= 0) {
                if (!processedContinuousAttributes.contains(this.restrictList[i])) {
                    ProbabilisticContinuousDouble attribute = (ProbabilisticContinuousDouble) object.getAttributes()[this.restrictList[i]];
                    NormalDistributionMixture distribution = attribute.getDistribution();
                    int dimension = distribution.getDimension();
                    int[] dependingAttributes = distribution.getAttributes();
                    int newDimension = 0;
                    for (int j = 0; j < dependingAttributes.length; j++) {
                        if (Arrays.binarySearch(this.projectedOutContinuousAttributes, dependingAttributes[j]) >= 0) {

                        }
                        else {
                            newDimension++;
                        }
                        processedContinuousAttributes.add(dependingAttributes[j]);
                    }

                    RealMatrix projectMatrix = MatrixUtils.createRealMatrix(dimension, newDimension);
                    for (NormalDistribution mixture : distribution.getMixtures().keySet()) {
                        RealMatrix covarianceMatrix = CovarianceMatrixUtils.toMatrix(mixture.getCovarianceMatrix());
                        mixture.setCovarianceMatrix(CovarianceMatrixUtils.fromMatrix(projectMatrix.multiply(
                                covarianceMatrix).multiply(projectMatrix.transpose())));
                    }
                }
            }
            else {
                newAttrs[i] = object.getAttributes()[this.restrictList[i]];
            }
        }

        // MatrixUtils.createRealMatrix();

        for (int index : continuousAttributes) {
            RealMatrix projectMatrix = projectMatrixes[index];
            int dimension = ((ProbabilisticContinuousDouble) out.getAttribute(index)).getDistribution().getDimension();

            Map<NormalDistribution, Double> mixtures = ((ProbabilisticContinuousDouble) out.getAttribute(index))
                    .getDistribution().getMixtures();

            for (NormalDistribution distribution : mixtures.keySet()) {
                RealMatrix covarianceMatrix = CovarianceMatrixUtils.toMatrix(distribution.getCovarianceMatrix());

                RealMatrix newCovarianceMatrix = projectMatrix.multiply(covarianceMatrix).multiply(
                        projectMatrix.transpose());
            }
        }

        this.transfer(out);
    }

    private void init() {

        List projectedOutContinuousAttributes = new ArrayList();
        List<Integer> continuousAttributes = new ArrayList<Integer>();
        List restrictList = Arrays.asList(this.restrictList);

        for (int i = 0; i < this.inputSchema.getAttributes().size(); i++) {
            SDFAttribute attribute = this.inputSchema.getAttribute(i);
            SDFDatatype datatype = attribute.getDatatype();
            if ((SDFProbabilisticDatatype.class.equals(datatype.getClass()))
                    && (((SDFProbabilisticDatatype) datatype).isContinuous())) {
                if (!restrictList.contains(i)) {
                    projectedOutContinuousAttributes.add(i);
                }
                else {
                    continuousAttributes.add((Integer) i);
                }
            }
        }
        this.continuousAttributes = continuousAttributes.toArray(new Integer[continuousAttributes.size()]);
        this.projectedOutContinuousAttributes = projectedOutContinuousAttributes
                .toArray(new Integer[projectedOutContinuousAttributes.size()]);
        Arrays.sort(this.continuousAttributes);
    }

    @Override
    public ProbabilisticProjectPO<T> clone() {
        return new ProbabilisticProjectPO<T>(this);
    }

}
