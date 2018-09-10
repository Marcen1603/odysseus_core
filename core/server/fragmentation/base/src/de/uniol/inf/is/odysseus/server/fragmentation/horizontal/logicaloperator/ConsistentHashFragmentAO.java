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
package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@LogicalOperator(name = "CONSISTENTHASHFRAGMENT", minInputPorts = 1, maxInputPorts = 1, doc = "Can be used to fragment incoming streams", category = { LogicalOperatorCategory.PROCESSING })
public class ConsistentHashFragmentAO extends AbstractDynamicFragmentAO {
    /**
     * 
     */
    private static final long serialVersionUID = 9163660715952300257L;

    /**
     * The URIs of the attributes forming the hash key, if the key is not the
     * whole tuple.
     */
    private List<SDFAttribute> fragmentAttributes;


    private List<String> stringAttributes;

    /**
     * Constructs a new {@link ConsistentHashFragmentAO}.
     * 
     * @see UnaryLogicalOp#UnaryLogicalOp()
     */
    public ConsistentHashFragmentAO() {
        super();
    }

    /**
     * Constructs a new {@link ConsistentHashFragmentAO} as a copy of an
     * existing one.
     * 
     * @param fragmentAO
     *            The {@link ConsistentHashFragmentAO} to be copied.
     * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
     */
    public ConsistentHashFragmentAO(ConsistentHashFragmentAO fragmentAO) {
        super(fragmentAO);
        if (fragmentAO.fragmentAttributes != null) {
            this.fragmentAttributes = Lists.newArrayList(fragmentAO.fragmentAttributes);
        }
    }

    @Override
    public AbstractLogicalOperator clone() {
        return new ConsistentHashFragmentAO(this);
    }

    /**
     * Returns the URIs of the attributes forming the hash key, if the key is
     * not the whole tuple.
     */
    @GetParameter(name = "ATTRIBUTES")
    public List<SDFAttribute> getAttributes() {
        return fragmentAttributes;
    }

    /**
     * Sets the URIs of the attributes forming the hash key, if the key is not
     * the whole tuple.
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = true, isList = true)
    public void setAttributes(List<SDFAttribute> uris) {

        this.fragmentAttributes = uris;

        List<String> attributes = Lists.newArrayList();
        for (SDFAttribute uri : uris) {
            attributes.add("'" + uri.getAttributeName() + "'");
        }
        this.addParameterInfo("ATTRIBUTES", attributes);

    }

    public void setStringAttributes(List<String> list) {
        this.stringAttributes = list;
    }

    @Override
    public void initialize() {
        if (stringAttributes != null) {
            this.fragmentAttributes = Lists.newArrayList();
            SDFSchema inputSchema = getInputSchema(0);
            for (String attributeNameToFind : stringAttributes) {
                fragmentAttributes.add(inputSchema.findAttribute(attributeNameToFind));
            }
        }
    }


}
