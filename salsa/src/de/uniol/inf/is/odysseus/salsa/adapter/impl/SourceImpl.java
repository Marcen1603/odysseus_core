package de.uniol.inf.is.odysseus.salsa.adapter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.salsa.adapter.AttributeConfiguration;
import de.uniol.inf.is.odysseus.salsa.adapter.Source;
import de.uniol.inf.is.odysseus.salsa.adapter.SourceConfiguration;

public class SourceImpl implements Source {

    private final String name;
    private final List<String> schema = new ArrayList<String>();
    private SourceConfiguration configuration;
    private final Map<String, AttributeConfiguration> attributeConfigurations = new HashMap<String, AttributeConfiguration>();

    public SourceImpl(final String name) {
        this.name = name;

    }

    @Override
    public void addAttribute(final String name, final AttributeConfiguration configuration) {
        this.schema.add(name);
        this.attributeConfigurations.put(name, configuration);
    }

    @Override
    public AttributeConfiguration getAttributeConfiguration(final String name) {
        return this.attributeConfigurations.get(name);
    }

    @Override
    public Map<String, AttributeConfiguration> getAttributeConfigurations() {
        return this.attributeConfigurations;
    }

    @Override
    public SourceConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getSchema() {
        return this.schema;
    }

    @Override
    public void removeAttribute(final String name) {
        this.schema.remove(name);
        this.attributeConfigurations.remove(name);
    }

    @Override
    public void setConfiguration(final SourceConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return "SourceImpl [name=" + this.name + ", schema=" + this.schema + ", configuration="
                + this.configuration + ", attributeConfigurations=" + this.attributeConfigurations
                + "]";
    }

}
