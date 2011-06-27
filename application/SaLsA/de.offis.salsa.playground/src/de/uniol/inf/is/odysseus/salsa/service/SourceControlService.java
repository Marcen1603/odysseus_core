package de.uniol.inf.is.odysseus.salsa.service;

import java.util.Map;
import java.util.Set;

public interface SourceControlService {

    Set<String> getSourceAdapters();

    void registerSource(String adapterType, String name, Map<String, String> sourceConfiguration,
            Map<String, Map<String, String>> attributeConfigurations);

    void unregisterSource(String name);
}
