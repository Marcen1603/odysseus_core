package de.uniol.inf.is.odysseus.salsa.adapter;

import java.util.List;
import java.util.Map;

public interface Source {

    void addAttribute(String name, AttributeConfiguration configuration);

    AttributeConfiguration getAttributeConfiguration(String name);

    Map<String, AttributeConfiguration> getAttributeConfigurations();

    SourceConfiguration getConfiguration();

    String getName();

    List<String> getSchema();

    void removeAttribute(String name);

    void setConfiguration(SourceConfiguration configuration);
}
