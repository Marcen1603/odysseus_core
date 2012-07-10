/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.base.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.wrapper.base.model.AttributeConfiguration;

public class AttributeConfigurationImpl implements AttributeConfiguration {
    private final Map<String, Object> configurations = new ConcurrentHashMap<String, Object>();

    @Override
    public void clear() {
        this.configurations.clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.configurations.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.configurations.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return this.configurations.entrySet();
    }

    @Override
    public Object get(final Object key) {
        return this.configurations.get(key);
    }

    @Override
    public boolean isEmpty() {
        return this.configurations.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return this.configurations.keySet();
    }

    @Override
    public Object put(final String key, final Object value) {
        return this.configurations.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Object> objects) {
        this.configurations.putAll(objects);
    }

    @Override
    public Object remove(final Object key) {
        return this.configurations.remove(key);
    }

    @Override
    public int size() {
        return this.configurations.size();
    }

    @Override
    public String toString() {
        return "AttributeConfigurationImpl [configurations=" + this.configurations + "]";
    }

    @Override
    public Collection<Object> values() {
        return this.configurations.values();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((configurations == null) ? 0 : configurations.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AttributeConfigurationImpl other = (AttributeConfigurationImpl) obj;
        if (configurations == null) {
            if (other.configurations != null)
                return false;
        }
        else if (!configurations.equals(other.configurations))
            return false;
        return true;
    }

}
