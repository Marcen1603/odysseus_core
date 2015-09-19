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
package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.consistenthash;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.HorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ConsistentHashFragmentAO;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ConsistentHashFragmentationQueryPartModificator extends HorizontalFragmentationQueryPartModificator {
    @Override
    public String getName() {
        return "fragmentation_horizontal_consistenthash";
    }

    @Override
    protected AbstractFragmentationParameterHelper createFragmentationHelper(final List<String> fragmentationParameters) {
        return new ConsistentHashFragmentationParameterHelper(fragmentationParameters);
    }

    @Override
    protected FragmentationInfoBundle createFragmentationInfoBundle(final AbstractFragmentationParameterHelper helper) {
        Preconditions.checkNotNull(helper, "Fragmentation helper must be not null!");
        Preconditions.checkArgument(helper instanceof ConsistentHashFragmentationParameterHelper, "The fragmentation helper must be a ConsistentHashFragmentationHelper!");

        final ConsistentHashFragmentationInfoBundle bundle = new ConsistentHashFragmentationInfoBundle();
        bundle.setKeyAttributes(((ConsistentHashFragmentationParameterHelper) helper).determineKeyAttributes());
        return bundle;
    }

    @Override
    protected ILogicalOperator createFragmentOperator(final int numFragments, final FragmentationInfoBundle bundle) throws QueryPartModificationException {
        Preconditions.checkNotNull(bundle, "Fragmentation info bundle must be not null!");
        Preconditions.checkArgument(bundle instanceof ConsistentHashFragmentationInfoBundle, "The fragmentation info bundle must be a ConsistentHashFragmentationInfoBundle!");

        final Optional<List<String>> attributes = ((ConsistentHashFragmentationInfoBundle) bundle).getKeyAttributes();

        final ConsistentHashFragmentAO fragmentAO = new ConsistentHashFragmentAO();
        if (attributes.isPresent()) {
            fragmentAO.setStringAttributes(attributes.get());
        }

        return fragmentAO;
    }
}
