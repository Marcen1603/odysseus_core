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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AssociativeStorageAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.consistenthash.ConsistentHashFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.consistenthash.ConsistentHashFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule.IFragmentationRule;

/**
 * An associative storage can be part of a fragment for consistent hash
 * horizontal fragmentation strategies, if the hierarchy matches the hash key.
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class AssociativeStorageConsistentHashFragmentationRule implements IFragmentationRule<ConsistentHashFragmentationQueryPartModificator, AssociativeStorageAO> {

    @Override
    public boolean canOperatorBePartOfFragments(final ConsistentHashFragmentationQueryPartModificator strategy, final AssociativeStorageAO operator,
            final AbstractFragmentationParameterHelper helper) {
        return !this.needSpecialHandlingForQueryPart(null, operator, helper);
    }

    @Override
    public boolean needSpecialHandlingForQueryPart(final ILogicalQueryPart part, final AssociativeStorageAO operator, final AbstractFragmentationParameterHelper helper) {
        Preconditions.checkArgument(helper instanceof ConsistentHashFragmentationParameterHelper, "Fragmentation helper must be a ConsistentHashFragmentationHelper");

        final Optional<List<String>> attributes = ((ConsistentHashFragmentationParameterHelper) helper).determineKeyAttributes();
        if (!attributes.isPresent()) {
            return true;
        }

        final Collection<SDFAttribute> hierarchyAttributes = operator.getHierarchyAttributes();
        for (final SDFAttribute attribute : hierarchyAttributes) {
            if (attributes.get().contains(attribute.getAttributeName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public AssociativeStorageAO specialHandling(final ILogicalQueryPart part, final AbstractFragmentationParameterHelper helper, final FragmentationInfoBundle bundle)
            throws QueryPartModificationException {
        // Nothing to do
        return null;
    }

    @Override
    public Class<ConsistentHashFragmentationQueryPartModificator> getStrategyClass() {
        return ConsistentHashFragmentationQueryPartModificator.class;
    }

    @Override
    public Class<AssociativeStorageAO> getOperatorClass() {
        return AssociativeStorageAO.class;
    }

}