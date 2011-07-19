package de.uniol.inf.is.odysseus.wrapper.base.model;

import java.util.List;

public interface SinkSpec {
    String getName();

    List<String> getSchema();

    SinkConfiguration getConfiguration();

    void setConfiguration(SinkConfiguration configuration);

}
