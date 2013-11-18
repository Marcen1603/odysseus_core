/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.ontology.ontology;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerException;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.Activator;
import de.uniol.inf.is.odysseus.ontology.manager.impl.PredicateManagerImpl;
import de.uniol.inf.is.odysseus.ontology.manager.impl.QueryManagerImpl;
import de.uniol.inf.is.odysseus.ontology.manager.impl.SourceManagerImpl;
import de.uniol.inf.is.odysseus.ontology.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.DUL;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.QU;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.SSN;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "all" })
public class SensorOntology {
    private final String STORE = "sensorstore";
    private final String MODEL = "sensors";
    private Model tBox;
    private OntModel model;
    private static final String SSN_PREFIX = "PREFIX ssn: <" + SSN.getURI() + "> ";
    private static final String XSD_PREFIX = "PREFIX xsd: <" + XSD.getURI() + "> ";
    private static final String RDF_PREFIX = "PREFIX rdf: <" + RDF.getURI() + "> ";
    private static final String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.getURI() + "> ";
    private static final String OWL_PREFIX = "PREFIX owl: <" + OWL.getURI() + "> ";
    private static final String DUL_PREFIX = "PREFIX dul: <" + DUL.getURI() + "> ";
    private static final String QU_PREFIX = "PREFIX qu: <" + QU.getURI() + "> ";
    private static final String ODY_PREFIX = "PREFIX ody: <" + ODYSSEUS.getURI() + "> ";

    private final SourceManagerImpl sourceManager;
    private final PredicateManagerImpl predicateManager;
    private final QueryManagerImpl queryManager;

    /**
     * Class constructor.
     * 
     */
    public SensorOntology() {
        this.sourceManager = new SourceManagerImpl(this.getABox());
        this.predicateManager = new PredicateManagerImpl(this.getABox());
        this.queryManager = new QueryManagerImpl(this.getABox());
    }

    /**
     * @param sensingDevice
     *            The sensing device
     */
    public void createSensingDevice(final SensingDevice sensingDevice) {
        this.sourceManager.createSensingDevice(sensingDevice);

    }

    /**
     * @param featureOfInterest
     *            The feature of interest
     */
    public void createFeatureOfInterest(FeatureOfInterest featureOfInterest) {
        this.sourceManager.createFeatureOfInterest(featureOfInterest);
    }

    public List<SensingDevice> getSensingdeviceByProperty(final SDFAttribute attribute) {
        // return
        // this.queryManager.getSensingDevicesByObservedProperty(attribute);
        throw new IllegalArgumentException("Not implemented yet");
    }

    public List<SensingDevice> getAllSensingDevices() {
        List<SensingDevice> sensingDevices = this.queryManager.getAllSensingDevices();
        for (SensingDevice sensingDevice : sensingDevices) {
            // System.out.println( getConditionPredicates(sensingDevice));
        }
        return sensingDevices;
    }

    public SensingDevice getSensingDevice(final String name) {
        return this.queryManager.getSensingDevice(URI.create(ODYSSEUS.NS + name));
    }

    /**
     * @return
     */
    public List<FeatureOfInterest> getAllFeaturesOfInterest() {
        List<FeatureOfInterest> featuresOfInterest = this.queryManager.getAllFeaturesOfInterest();
        for (FeatureOfInterest featureOfInterest : featuresOfInterest) {
            // System.out.println( getConditionPredicates(sensingDevice));
        }
        return featuresOfInterest;
    }

    /**
     * @return
     */
    public List<Property> getAllProperties() {
        List<Property> properties = this.queryManager.getAllProperties();
        for (Property property : properties) {
            // System.out.println( getConditionPredicates(sensingDevice));
        }
        out();
        return properties;
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

    private Model getTBox() throws IOException {
        if (this.tBox == null) {
            this.tBox = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            URL file;
            if (Activator.getContext() != null) {
                file = Activator.getContext().getBundle().getEntry("owl/SSN.owl");
            }
            else {
                file = SensorOntology.class.getResource("owl/SSN.owl");
            }
            final BufferedReader reader = new BufferedReader(new InputStreamReader(file.openConnection().getInputStream()));
            this.tBox.read(reader, null);

        }
        return this.tBox;
    }

    private OntModel getABox() {
        if (this.model == null) {
            final File root = new File(this.STORE);
            root.mkdir();

            final ModelMaker maker = ModelFactory.createFileModelMaker(root.getAbsolutePath());
            final Model aBox = maker.openModel(this.MODEL, false);

            Reasoner reasoner = null;
            try {
                reasoner = ReasonerRegistry.getOWLMiniReasoner().bindSchema(this.getTBox());
            }
            catch (ReasonerException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            final OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_MINI_RULE_INF);
            spec.setReasoner(reasoner);
            this.model = ModelFactory.createOntologyModel(spec, aBox);
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
        this.getABox().write(System.out, FileUtils.langXMLAbbrev);
    }


}
