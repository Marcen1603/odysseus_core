package de.uniol.inf.is.odysseus.wrapper.base;

import java.util.Map;
import java.util.Set;

public interface SourceControlService {
    Set<String> getSourceAdapters();
    @Deprecated
    void registerSource(String adapterType, String name, Map<String, String> sourceConfiguration,
            Map<String, Map<String, String>> attributesConfiguration);

    @Deprecated
    void unregisterSource(String name);
}
