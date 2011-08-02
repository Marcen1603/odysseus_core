package de.uniol.inf.is.odysseus.wrapper.base.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.base.model.AttributeConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class SourceSpecImpl implements SourceSpec {

    private final String name;
    private final List<String> schema = new ArrayList<String>();
    private SourceConfiguration configuration;
    private final Map<String, AttributeConfiguration> attributeConfigurations = new HashMap<String, AttributeConfiguration>();

    public SourceSpecImpl(final String name) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((attributeConfigurations == null) ? 0 : attributeConfigurations.hashCode());
        result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
        result = prime * result + ((schema == null) ? 0 : schema.hashCode());
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
        SourceSpecImpl other = (SourceSpecImpl) obj;
        if (attributeConfigurations == null) {
            if (other.attributeConfigurations != null)
                return false;
        }
        else if (!attributeConfigurations.equals(other.attributeConfigurations))
            return false;
        if (configuration == null) {
            if (other.configuration != null)
                return false;
        }
        else if (!configuration.equals(other.configuration))
            return false;
        if (schema == null) {
            if (other.schema != null)
                return false;
        }
        else if (!schema.equals(other.schema))
            return false;
        return true;
    }

}
