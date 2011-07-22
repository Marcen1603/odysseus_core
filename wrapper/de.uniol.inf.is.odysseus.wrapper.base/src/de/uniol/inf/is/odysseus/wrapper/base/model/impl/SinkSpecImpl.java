package de.uniol.inf.is.odysseus.wrapper.base.model.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.wrapper.base.model.SinkConfiguration;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public class SinkSpecImpl implements SinkSpec {
    private final String name;
    private final List<String> schema = new ArrayList<String>();
    private SinkConfiguration configuration;

    public SinkSpecImpl(final String name) {
        this.name = name;
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
    public void setConfiguration(final SinkConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SinkConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public String toString() {
        return "SinkImpl [name=" + this.name + ", schema=" + this.schema + ", configuration="
                + this.configuration + "]";
    }
}
