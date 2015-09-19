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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.HorizontalFragmentationParameterHelper;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ConsistentHashFragmentationParameterHelper extends HorizontalFragmentationParameterHelper {
    /**
     * The logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ConsistentHashFragmentationParameterHelper.class);

    /**
     * The index of the parameter identifying the first attribute to build a
     * hash key.
     */
    public static final int PARAMETER_INDEX_FIRST_HASHKEY_ATTRIBUTE = 2;

    /**
     * Creates a new fragmentation helper.
     *
     * @param fragmentationParameters
     *            The parameters for fragmentation.
     * @param strategy
     *            The fragmentation strategy.
     */
    public ConsistentHashFragmentationParameterHelper(final List<String> fragmentationParameters) {
        super(fragmentationParameters);
    }

    /**
     * Determines the attributes to build a hash key, if given.
     *
     * @return A list containing the names of the attributes to build a hash
     *         key. {@link Optional#absent()} if no attributes could be found.
     */
    public Optional<List<String>> determineKeyAttributes() {
        if (this.mFragmentationParameters.size() <= ConsistentHashFragmentationParameterHelper.PARAMETER_INDEX_FIRST_HASHKEY_ATTRIBUTE) {
            ConsistentHashFragmentationParameterHelper.LOG.debug("Found no attributes to form a key.");
            return Optional.absent();
        }

        final List<String> attributes = Lists
                .newArrayList(this.mFragmentationParameters.subList(ConsistentHashFragmentationParameterHelper.PARAMETER_INDEX_FIRST_HASHKEY_ATTRIBUTE, this.mFragmentationParameters.size()));
        ConsistentHashFragmentationParameterHelper.LOG.debug("Found '" + attributes + "' as attributes to form a key.");
        return Optional.of(attributes);
    }

}
