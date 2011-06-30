package de.uniol.inf.is.odysseus.salsa.adapter.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.salsa.adapter.SourceConfiguration;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class SourceConfigurationImpl implements SourceConfiguration {
    private final Map<String, Object> configurations = new ConcurrentHashMap<String, Object>();;

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
    public Collection<Object> values() {
        return this.configurations.values();
    }
}
