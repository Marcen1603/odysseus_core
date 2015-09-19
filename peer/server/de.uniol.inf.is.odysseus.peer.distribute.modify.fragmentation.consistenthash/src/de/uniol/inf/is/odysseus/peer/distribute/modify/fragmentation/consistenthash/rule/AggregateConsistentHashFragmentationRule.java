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
package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.consistenthash.rule;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.consistenthash.ConsistentHashFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.consistenthash.ConsistentHashFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule.AggregateHorizontalFragmentationRule;

/**
 * An aggregation can be part of a fragment for consistent hash horizontal
 * fragmentation strategies, if the grouping aggregates match the hash key or if
 * the aggregation function is AVG COUNT or SUM.
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class AggregateConsistentHashFragmentationRule extends AggregateHorizontalFragmentationRule<ConsistentHashFragmentationQueryPartModificator> {

    @Override
    public boolean canOperatorBePartOfFragments(final ConsistentHashFragmentationQueryPartModificator strategy, final AggregateAO operator, final AbstractFragmentationParameterHelper helper) {
        if (!this.needSpecialHandlingForQueryPart(null, operator, helper)) {
            return true;
        }
        return super.canOperatorBePartOfFragments(strategy, operator, helper);
    }

    @Override
    public boolean needSpecialHandlingForQueryPart(final ILogicalQueryPart part, final AggregateAO operator, final AbstractFragmentationParameterHelper helper) {
        Preconditions.checkArgument(helper instanceof ConsistentHashFragmentationParameterHelper, "Fragmentation helper must be a ConsistentHashFragmentationHelper");

        final Optional<List<String>> attributes = ((ConsistentHashFragmentationParameterHelper) helper).determineKeyAttributes();
        if (!attributes.isPresent()) {
            return true;
        }

        final Collection<SDFAttribute> aggregationAttributes = operator.getGroupingAttributes();
        for (final SDFAttribute attribute : aggregationAttributes) {
            if (attributes.get().contains(attribute.getAttributeName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Class<ConsistentHashFragmentationQueryPartModificator> getStrategyClass() {
        return ConsistentHashFragmentationQueryPartModificator.class;
    }

}