/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.mining.clustering.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.mining.NonNumericAttributeException;

/**
 * This class is a super class for logical clustering operators. It specifies
 * two output schemes that can be used concrete logical clustering operators.
 * One output schema is for clustered data points and one for the clusters.
 * 
 * @author Kolja Blohm
 * 
 */
public abstract class AbstractClusteringAO extends UnaryLogicalOp {

    private static final long serialVersionUID = 5930667720134167936L;
    protected List<SDFAttribute> attributes;

    /**
     * Standard constructor.
     * 
     */
    protected AbstractClusteringAO() {

    }

    /**
     * Copy constructor.
     * 
     * @param copy
     *            the AbstractClusteringAO to copy.
     */
    public AbstractClusteringAO(AbstractClusteringAO copy) {
        super(copy);
        this.attributes = new ArrayList<SDFAttribute>(copy.getAttributes());
    }

    /**
     * Returns the list of attributes that should be used for clustering.
     * 
     * @return the list of attributes.
     */
    public List<SDFAttribute> getAttributes() {
        return attributes;
    }

    /**
     * Returns a list of indices identifying the positions of the attributes in
     * the input schema that should be used for clustering.
     * 
     * @return the list of indices identifying the positions of the attributes
     *         which should be used for clustering.
     * 
     */
    public int[] determineRestrictList() {
        return calcRestrictList(this.getInputSchema().getAttributes(), attributes);
    }

    /**
     * Returns a list of indices identifying the positions of the attributes in
     * the input schema that should be used for clustering.
     * 
     * @param inputSchema
     *            the input scheme.
     * @param attributes
     *            the list of attributes that should be used for clustering.
     * @return the list of indices identifying the positions of the attributes
     *         which should be used for clustering.
     */

    public static int[] calcRestrictList(Collection<SDFAttribute> inputSchema, List<SDFAttribute> attributes) {
        int[] ret = new int[attributes.size()];
        int i = 0;
        for (SDFAttribute a : attributes) {
            int j = 0;
            int k = i;
            for (SDFAttribute b : inputSchema) {
                if (b.equals(a)) {
                    ret[i++] = j;
                }
                ++j;
            }
            if (k == i) {
                throw new IllegalArgumentException("no such attribute: " + a);
            }
        }
        return ret;
    }

    /**
     * Sets the list of attributes that should be used for clustering.
     * 
     * @param attributes
     *            the list of attributes.
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, isList = true)
    public void setAttributes(List<SDFAttribute> attributes) {
        this.attributes = attributes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
     * #getOutputSchema ()
     */
    @Override
    public SDFSchema getOutputSchema() {
        List<SDFAttribute> elems = new ArrayList<SDFAttribute>();
        SDFAttribute id = new SDFAttribute(null, "cluster_id", SDFDatatype.INTEGER);
        elems.add(id);
        elems.addAll(getInputSchema().clone().getAttributes());
        SDFSchema outputSchema = new SDFSchema("Cluster", elems);

        return outputSchema;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * # getOutputSchema(int)
     */
    @Override
    public SDFSchema getOutputSchema(int port) {

        if (port == 0) {
            return getOutputSchema();
        }
        List<SDFAttribute> attribs = new ArrayList<SDFAttribute>();
        SDFAttribute idA = new SDFAttribute(null, "cluster_id", SDFDatatype.INTEGER);
        attribs.add(idA);
        SDFAttribute idCount = new SDFAttribute(null, "cluster_count", SDFDatatype.LONG);
        attribs.add(idCount);
        attribs.addAll(attributes);
        SDFSchema clusterSchema = new SDFSchema("Cluster", attribs);
        return clusterSchema;

    }

    @Override
    public boolean isValid() {
        Iterator<SDFAttribute> iter = attributes.iterator();
        while (iter.hasNext()) {
            SDFAttribute attribute = iter.next();
            if (!attribute.getDatatype().isNumeric()) {
                addError(new NonNumericAttributeException(attribute));
                return false;
            }
        }
        return true;
    }
}
