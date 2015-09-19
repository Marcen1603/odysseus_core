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

import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ConsistentHashFragmentationInfoBundle extends FragmentationInfoBundle {
    /**
     * The attributes for the hash key, if given.
     */
    private Optional<List<String>> keyAttrbutes;

    /**
     * The attributes for the hash key.
     *
     * @param attributes
     *            A list of attribute names for the hash key.
     */
    public void setKeyAttributes(final Optional<List<String>> attributes) {
        this.keyAttrbutes = attributes;
    }

    /**
     * The attributes for the hash key.
     *
     * @return list of attribute names for the hash key or
     *         {@link Optional#absent()} if none is given.
     */
    public Optional<List<String>> getKeyAttributes() {
        return this.keyAttrbutes;
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        buffer.append("\nAttributes to build the hash key: " + this.keyAttrbutes);
        return buffer.toString();
    }
}
