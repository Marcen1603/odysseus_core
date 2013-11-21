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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;
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
	private static final Logger LOG = LoggerFactory
			.getLogger(SensorOntology.class);

	private final String STORE = "sensorstore";
	private final String MODEL = "sensors";
	private OntModel model;
	private static final String SSN_PREFIX = "PREFIX ssn: <" + SSN.getURI()
			+ "> ";
	private static final String XSD_PREFIX = "PREFIX xsd: <" + XSD.getURI()
			+ "> ";
	private static final String RDF_PREFIX = "PREFIX rdf: <" + RDF.getURI()
			+ "> ";
	private static final String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.getURI()
			+ "> ";
	private static final String OWL_PREFIX = "PREFIX owl: <" + OWL.getURI()
			+ "> ";
	private static final String DUL_PREFIX = "PREFIX dul: <" + DUL.getURI()
			+ "> ";
	private static final String QU_PREFIX = "PREFIX qu: <" + QU.getURI() + "> ";
	private static final String ODY_PREFIX = "PREFIX ody: <"
			+ ODYSSEUS.getURI() + "> ";

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
		} catch (IOException e) {
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

	}

	/**
	 * @param featureOfInterest
	 *            The feature of interest
	 */
	public void createFeatureOfInterest(FeatureOfInterest featureOfInterest) {
		this.sourceManager.createFeatureOfInterest(featureOfInterest);
	}

	public List<SensingDevice> getSensingdeviceByProperty(
			final SDFAttribute attribute) {
		// return
		// this.queryManager.getSensingDevicesByObservedProperty(attribute);
		throw new IllegalArgumentException("Not implemented yet");
	}

	public List<SensingDevice> getAllSensingDevices() {
		List<SensingDevice> sensingDevices = this.queryManager
				.getAllSensingDevices();
		for (SensingDevice sensingDevice : sensingDevices) {
			System.out.println(sensingDevice);
		}
		return sensingDevices;
	}

	public SensingDevice getSensingDevice(final String name) {
		return this.queryManager.getSensingDevice(URI
				.create(ODYSSEUS.NS + name));
	}

	/**
	 * @return
	 */
	public List<FeatureOfInterest> getAllFeaturesOfInterest() {
		List<FeatureOfInterest> featuresOfInterest = this.queryManager
				.getAllFeaturesOfInterest();
		for (FeatureOfInterest featureOfInterest : featuresOfInterest) {
			System.out.println(featureOfInterest);
		}
		return featuresOfInterest;
	}

	/**
	 * @return
	 */
	public List<Property> getAllProperties() {
		List<Property> properties = this.queryManager.getAllProperties();
		for (Property property : properties) {
			System.out.println(property);
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

	private void createModel(File root) throws IOException {
		root.mkdir();
		final ModelMaker maker = ModelFactory.createFileModelMaker(root
				.getAbsolutePath());
		OntModel model = ModelFactory.createOntologyModel(
				OntModelSpec.OWL_DL_MEM, null);
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
		} else {
			file = SensorOntology.class.getResource("owl/SSN.owl");
		}
		if (file != null) {
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(file.openConnection()
							.getInputStream()));
			model.read(reader, null);
		} else {
			model.read("http://purl.oclc.org/NET/ssnx/ssn");
		}
		model.close();
	}

	private OntModel getModel() throws IOException {
		if (this.model == null) {
			final File root = new File(this.STORE);
			if (!root.exists()) {
				createModel(root);
			}
			final ModelMaker maker = ModelFactory.createFileModelMaker(root
					.getAbsolutePath());

			OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
			ontModelSpec.setBaseModelMaker(maker);
			ontModelSpec.setDocumentManager(new OntDocumentManager());

			Model base = maker.openModel(this.MODEL, false);
			this.model = ModelFactory.createOntologyModel(ontModelSpec, base);
			//
			// final ModelMaker maker = ModelFactory.createFileModelMaker(root
			// .getAbsolutePath());
			//
			// this.dataModel = maker.openModel(this.MODEL, false);

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
