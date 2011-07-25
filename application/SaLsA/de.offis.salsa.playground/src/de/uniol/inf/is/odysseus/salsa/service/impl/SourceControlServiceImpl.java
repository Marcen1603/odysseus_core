package de.uniol.inf.is.odysseus.salsa.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.salsa.adapter.AttributeConfiguration;
import de.uniol.inf.is.odysseus.salsa.adapter.Source;
import de.uniol.inf.is.odysseus.salsa.adapter.SourceAdapter;
import de.uniol.inf.is.odysseus.salsa.adapter.SourceConfiguration;
import de.uniol.inf.is.odysseus.salsa.adapter.impl.AttributeConfigurationImpl;
import de.uniol.inf.is.odysseus.salsa.adapter.impl.SourceConfigurationImpl;
import de.uniol.inf.is.odysseus.salsa.adapter.impl.SourceImpl;
import de.uniol.inf.is.odysseus.salsa.service.SourceControlService;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class SourceControlServiceImpl implements SourceControlService {
    private static Logger LOG = LoggerFactory.getLogger(SourceControlService.class);
    private static final String SETTINGS_FILE = "LaserScanner_Settings.json";
    private static final String SETTINGS_PATH = System.getProperty("user.home") + "/odysseus/";

    private final Map<String, List<SourceAdapter>> sourceAdapters = new ConcurrentHashMap<String, List<SourceAdapter>>();
    private final Map<String, Source> sources = new ConcurrentHashMap<String, Source>();
    private final Map<Source, SourceAdapter> sourceAdapterMapping = new ConcurrentHashMap<Source, SourceAdapter>();

    protected void activate(final ComponentContext context) {
        SourceControlServiceImpl.LOG.debug("Activating SourceControlService");
        this.loadConfigurationFile(SourceControlServiceImpl.SETTINGS_PATH  + "/" + SourceControlServiceImpl.SETTINGS_FILE);
    }

    protected void bindSourceAdapter(final SourceAdapter adapter) {
        SourceControlServiceImpl.LOG.debug("Bind source adapter {}", adapter.getName());
        if (!this.sourceAdapters.containsKey(adapter.getName())) {
            this.sourceAdapters.put(adapter.getName(), new ArrayList<SourceAdapter>());
        }
        this.sourceAdapters.get(adapter.getName()).add(adapter);
        // FIXME remove this line when the source adapters are in their own bundle
        this.loadConfigurationFile(SourceControlServiceImpl.SETTINGS_PATH + "/" + SourceControlServiceImpl.SETTINGS_FILE);
    }

    private void configureSensor(final JSONObject sensor) throws JSONException, URISyntaxException {
        final Map<String, String> sensorConfiguration = new HashMap<String, String>();
        final Map<String, Map<String, String>> attributesConfiguration = new HashMap<String, Map<String, String>>();
        final String name = sensor.getString("name");
        final String adapterType = sensor.getString("adapter");
        if (sensor.has("configuration")) {
            final JSONObject configurations = sensor.getJSONObject("configuration");
            final JSONArray configurationNames = configurations.names();
            for (int i = 0; i < configurationNames.length(); i++) {
                final String configurationName = configurationNames.getString(i);
                final String configurationValue = configurations.getString(configurationName);
                sensorConfiguration.put(configurationName, configurationValue);
            }
        }
        if (sensor.has("attributes")) {
            final JSONObject attributes = sensor.getJSONObject("attributes");
            final JSONArray attributeNames = attributes.names();
            for (int i = 0; i < attributeNames.length(); i++) {
                final String attributeName = attributeNames.getString(i);

                attributesConfiguration.put(attributeName, new HashMap<String, String>());
                final JSONArray attributeConfigurationNames = attributes.getJSONObject(
                        attributeName).names();
                for (int j = 0; j < attributeConfigurationNames.length(); j++) {
                    final String attributeConfigurationName = attributeConfigurationNames
                            .getString(j);
                    final String attributeConfigurationValue = attributes.getJSONObject(
                            attributeName).getString(attributeConfigurationName);

                    attributesConfiguration.get(attributeName).put(attributeConfigurationName,
                            attributeConfigurationValue);
                }
            }
        }
        this.registerSource(adapterType, name, sensorConfiguration, attributesConfiguration);

    }

    protected void deactivate(final ComponentContext context) {
    }

    @Override
    public Set<String> getSourceAdapters() {
        return Collections.unmodifiableSet(this.sourceAdapters.keySet());
    }

    private void loadConfigurationFile(final String configurationFile) {
        Scanner scanner = null;
        try {
            SourceControlServiceImpl.LOG.debug("Loading source configuration file {}",
                    configurationFile);
            scanner = new Scanner(new FileReader(configurationFile)); 
            final StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            final JSONArray json = new JSONArray(sb.toString());
            if (json.length() > 0) {
                for (int i = 0; i < json.length(); i++) {
                    try {
                        this.configureSensor(json.getJSONObject(i));
                    }
                    catch (final JSONException e) {
                        SourceControlServiceImpl.LOG.error(e.getMessage(), e);
                    }
                    catch (final URISyntaxException e) {
                        SourceControlServiceImpl.LOG.error(e.getMessage(), e);
                    }
                }
            }
        }
        catch (final FileNotFoundException e) {
            try {
            	 SourceControlServiceImpl.LOG.debug("Create source configuration file");
                (new File(configurationFile)).createNewFile();
            }
            catch (final IOException e2) {
                SourceControlServiceImpl.LOG.error(e2.getMessage(), e2);
            }
            SourceControlServiceImpl.LOG.error(e.getMessage(), e);
        }
        catch (final JSONException e) {
            SourceControlServiceImpl.LOG.error(e.getMessage(), e);
        }
        finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    @Override
    public void registerSource(final String adapterType, final String name,
            final Map<String, String> sourceConfiguration,
            final Map<String, Map<String, String>> attributeConfigurations) {
        if (this.getSourceAdapters().contains(adapterType)) {
            final SourceAdapter adapter = this.sourceAdapters.get(adapterType).get(0);
            final Source source = new SourceImpl(name);

            final SourceConfiguration configuration = new SourceConfigurationImpl();
            if (sourceConfiguration != null) {
                configuration.putAll(sourceConfiguration);
            }
            source.setConfiguration(configuration);
            if (attributeConfigurations != null) {
                for (final String attribute : attributeConfigurations.keySet()) {
                    final AttributeConfiguration attributeConfiguration = new AttributeConfigurationImpl();
                    source.addAttribute(attribute, attributeConfiguration);
                }
            }
            this.sources.put(source.getName(), source);

            adapter.registerSource(source);
            this.sourceAdapterMapping.put(source, adapter);
            SourceControlServiceImpl.LOG.debug("Register source {} at source adapter {}", name,
                    adapter.getName());
        }
    }

    protected void unbindSourceAdapter(final SourceAdapter adapter) {
        SourceControlServiceImpl.LOG.debug("Unbind source adapter {}", adapter.getName());
        if (this.sourceAdapters.containsKey(adapter.getName())) {
            this.sourceAdapters.get(adapter.getName()).remove(adapter);
        }
    }

    @Override
    public void unregisterSource(final String name) {
        final Source source = this.sources.get(name);
        final SourceAdapter adapter = this.sourceAdapterMapping.get(source);
        adapter.unregisterSource(source);
        this.sourceAdapterMapping.remove(source);
        this.sources.remove(name);
        SourceControlServiceImpl.LOG.debug("Unregister source {}", name);
    }
}
