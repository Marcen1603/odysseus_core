/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.storage.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;
import cc.kuka.odysseus.ontology.storage.Activator;
import cc.kuka.odysseus.ontology.storage.manager.impl.PredicateManagerImpl;
import cc.kuka.odysseus.ontology.storage.manager.impl.QueryManagerImpl;
import cc.kuka.odysseus.ontology.storage.manager.impl.SourceManagerImpl;
import cc.kuka.odysseus.ontology.storage.vocabulary.DUL;
import cc.kuka.odysseus.ontology.storage.vocabulary.ODYSSEUS;
import cc.kuka.odysseus.ontology.storage.vocabulary.QU;
import cc.kuka.odysseus.ontology.storage.vocabulary.SSN;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@SuppressWarnings({ "all" })
public class SensorOntology {
    private static final Logger LOG = LoggerFactory.getLogger(SensorOntology.class);

    private final String STORE = "sensorstore";
    private final String MODEL = "sensors";
    private OntModel model;
    private static final String SSN_PREFIX = "PREFIX ssn: <" + SSN.getURI() + "> ";
    private static final String XSD_PREFIX = "PREFIX xsd: <" + XSD.getURI() + "> ";
    private static final String RDF_PREFIX = "PREFIX rdf: <" + RDF.getURI() + "> ";
    private static final String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.getURI() + "> ";
    private static final String OWL_PREFIX = "PREFIX owl: <" + OWL.getURI() + "> ";
    private static final String DUL_PREFIX = "PREFIX dul: <" + DUL.getURI() + "> ";
    private static final String QU_PREFIX = "PREFIX qu: <" + QU.getURI() + "> ";
    private static final String ODY_PREFIX = "PREFIX ody: <" + ODYSSEUS.getURI() + "> ";

    private SourceManagerImpl sourceManager;
    private PredicateManagerImpl predicateManager;
    private QueryManagerImpl queryManager;

    /**
     * Class constructor.
     *
     */
    public SensorOntology() {
        try {
            this.sourceManager = new SourceManagerImpl(this.getModel());
            this.predicateManager = new PredicateManagerImpl(this.getModel());
            this.queryManager = new QueryManagerImpl(this.getModel());
        }
        catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param sensingDevice
     *            The sensing device
     */
    public void createSensingDevice(final SensingDevice sensingDevice) {
        this.sourceManager.createSensingDevice(sensingDevice);
        this.queryManager.clearCache();
    }

    /**
     * @param featureOfInterest
     *            The feature of interest
     */
    public void createFeatureOfInterest(final FeatureOfInterest featureOfInterest) {
        this.sourceManager.createFeatureOfInterest(featureOfInterest);
        this.queryManager.clearCache();

    }

    /**
     *
     * @param sensingDevice
     * @param measurementCapability
     */
    public void createMeasurementCapability(final SensingDevice sensingDevice, final MeasurementCapability measurementCapability) {
        this.sourceManager.createMeasurementCapability(sensingDevice, measurementCapability);
        this.queryManager.clearCache();
    }

    /**
     *
     * @param measurementCapability
     * @param condition
     */
    public void createCondition(final MeasurementCapability measurementCapability, final Condition condition) {
        this.sourceManager.createCondition(measurementCapability, condition);
        this.queryManager.clearCache();
        this.queryManager.clearCache();
    }

    /**
     *
     * @param featureOfInterest
     * @param property
     */
    public void createProperty(final FeatureOfInterest featureOfInterest, final Property property) {
        this.sourceManager.createProperty(featureOfInterest, property);
        this.queryManager.clearCache();

    }

    /**
     *
     * @param measurementCapability
     * @param measurementProperty
     */
    public void createMeasurementProperty(final MeasurementCapability measurementCapability, final MeasurementProperty measurementProperty) {
        this.sourceManager.createMeasurementProperty(measurementCapability, measurementProperty);
        this.queryManager.clearCache();

    }

    public List<SensingDevice> getSensingdeviceByProperty(final SDFAttribute attribute) {
        // return
        // this.queryManager.getSensingDevicesByObservedProperty(attribute);
        throw new IllegalArgumentException("Not implemented yet");
    }

    public List<SensingDevice> getAllSensingDevices() {
        final List<SensingDevice> sensingDevices = this.queryManager.getAllSensingDevices();
        return sensingDevices;
    }

    public SensingDevice getSensingDevice(final String name) {
        return this.queryManager.getSensingDevice(URI.create(ODYSSEUS.NS + name));
    }

    /**
     * @return
     */
    public List<FeatureOfInterest> getAllFeaturesOfInterest() {
        final List<FeatureOfInterest> featuresOfInterest = this.queryManager.getAllFeaturesOfInterest();
        return featuresOfInterest;
    }

    /**
     * @return
     */
    public List<Property> getAllProperties() {
        final List<Property> properties = this.queryManager.getAllProperties();
        return properties;
    }

    public List<SensingDevice> getSensingDevices(final String featureOfInterest, final String sensingDevice, final String measurementCapability) {
        final List<SensingDevice> sensingDevices = this.queryManager.getSensingDevices(featureOfInterest, sensingDevice, measurementCapability);
        return sensingDevices;
    }

    public List<SensingDevice> getSensingDevices(final String sensingDevice, final String measurementCapability) {
        final List<SensingDevice> sensingDevices = this.queryManager.getSensingDevices(sensingDevice, measurementCapability);
        return sensingDevices;
    }
    public List<String> getAttributes(final Property property) {
        final List<String> attributes = this.queryManager.getAttributes(property);
        return attributes;
    }

    /**
     *
     */
    public void clearCache() {
        this.queryManager.clearCache();
    }

    // public Map<SDFAttribute, Map<Property, String>>
    // getConditionPredicates(final SensingDevice sensingDevice) {
    // Map<SDFAttribute, Map<Property, String>> attributePropertyMapping = new
    // HashMap<SDFAttribute, Map<Property, String>>();
    // List<SensingDevice> requiredSources = new ArrayList<SensingDevice>();
    // for (SDFAttribute attribute : sensingDevice.getSchema()) {
    // List<MeasurementCapability> measurementCapabilities =
    // sensingDevice.getHasMeasurementCapabilities(attribute);
    // attributePropertyMapping.put(attribute, new HashMap<Property, String>());
    // for (MeasurementCapability measurementCapability :
    // measurementCapabilities) {
    //
    // List<AbstractCondition> conditions =
    // measurementCapability.getConditions();
    // for (AbstractCondition condition : conditions) {
    // SDFAttribute conditionAttribute = condition.getAttribute();
    // if (!sensingDevice.getSchema().contains(conditionAttribute)) {
    // List<SensingDevice> otherSensingDevice =
    // queryManager.getSensingDevicesByObservedProperty(conditionAttribute);
    // if (otherSensingDevice.size() > 0) {
    // requiredSources.add(otherSensingDevice.get(0));
    // }
    // }
    //
    // Interval conditionInterval = condition.getInterval();
    //
    // List<MeasurementProperty> measurementProperties =
    // measurementCapability.getMeasurementProperties();
    // for (MeasurementProperty measurementProperty : measurementProperties) {
    // Property property = measurementProperty.getProperty();
    // Interval measurementPropertyInterval = measurementProperty.getInterval();
    //
    // // Create an expression like:
    // // eif(attr > inf AND attr < sup, toInterval(min,max),
    // // toInterval(1.0,1.0))
    // StringBuilder valueString = new StringBuilder();
    // valueString.append("eif(");
    // valueString.append(conditionAttribute.getQualName());
    // valueString.append(" > ");
    // valueString.append(conditionInterval.inf());
    // valueString.append(" AND ");
    // valueString.append(conditionAttribute.getQualName());
    // valueString.append(" < ");
    // valueString.append(conditionInterval.sup());
    // valueString.append(",");
    // valueString.append("toInterval(");
    // valueString.append(measurementPropertyInterval.inf());
    // valueString.append(",");
    // valueString.append(measurementPropertyInterval.sup());
    // valueString.append("),toInterval(1.0,1.0)");
    // valueString.append(")");
    //
    // System.out.println(valueString.toString());
    // attributePropertyMapping.get(attribute).put(property,
    // valueString.toString());
    // }
    //
    // }
    //
    // }
    // }
    // return attributePropertyMapping;
    // }

    private void createModel(final File root) throws IOException {
        root.mkdir();
        final ModelMaker maker = ModelFactory.createFileModelMaker(root.getAbsolutePath());
        final OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, null);
        model.setNsPrefix("ssn", SSN.getURI());
        model.setNsPrefix("xsd", XSD.getURI());
        model.setNsPrefix("rdf", RDF.getURI());
        model.setNsPrefix("rdfs", RDFS.getURI());
        model.setNsPrefix("owl", OWL.getURI());
        model.setNsPrefix("dul", DUL.getURI());
        model.setNsPrefix("qu", QU.getURI());
        model.setNsPrefix("ody", ODYSSEUS.getURI());
        URL file;
        if (Activator.getContext() != null) {
            file = Activator.getContext().getBundle().getEntry("owl/SSN.owl");
        }
        else {
            file = SensorOntology.class.getResource("owl/SSN.owl");
        }
        if (file != null) {
        	try{
            final BufferedReader reader = new BufferedReader(new InputStreamReader(file.openConnection().getInputStream()));
            model.read(reader, null);
        	}catch(Throwable e){
        		e.printStackTrace();
                model.read("http://purl.oclc.org/NET/ssnx/ssn");
        	}
        }
        else {
            model.read("http://purl.oclc.org/NET/ssnx/ssn");
        }
        model.close();
    }

    private OntModel getModel() throws IOException {
        if (this.model == null) {
            final String home = OdysseusConfiguration.instance.getHomeDir();
            final File root = new File(home, this.STORE);
            if (!root.exists()) {
                this.createModel(root);
            }
            final ModelMaker maker = ModelFactory.createFileModelMaker(root.getAbsolutePath());

            final OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
            ontModelSpec.setBaseModelMaker(maker);
            ontModelSpec.setDocumentManager(new OntDocumentManager());
            ontModelSpec.setReasoner(OntModelSpec.OWL_MEM_MICRO_RULE_INF.getReasoner());

            final Model base = maker.openModel(this.MODEL, false);
            this.model = ModelFactory.createOntologyModel(ontModelSpec, base);

            this.model.setNsPrefix("ssn", SSN.getURI());
            this.model.setNsPrefix("xsd", XSD.getURI());
            this.model.setNsPrefix("rdf", RDF.getURI());
            this.model.setNsPrefix("rdfs", RDFS.getURI());
            this.model.setNsPrefix("owl", OWL.getURI());
            this.model.setNsPrefix("dul", DUL.getURI());
            this.model.setNsPrefix("qu", QU.getURI());
            this.model.setNsPrefix("ody", ODYSSEUS.getURI());
        }
        return this.model;
    }

    public void out() {
        try {
            this.getModel().write(System.out, FileUtils.langXMLAbbrev);
        }
        catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
