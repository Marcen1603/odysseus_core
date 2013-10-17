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
package de.uniol.inf.is.odysseus.probabilistic.sensor.ontology;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SSN {
    /**
     * <p>
     * The RDF model that holds the vocabulary terms
     * </p>
     */
    private static Model m_model = ModelFactory.createDefaultModel();

    /**
     * <p>
     * The namespace of the vocabulary as a string ({@value})
     * </p>
     */
    public static final String NS = "http://purl.oclc.org/NET/ssnx/ssn#";

    /**
     * <p>
     * The namespace of the vocabulary as a string
     * </p>
     * 
     * @see #NS
     */
    public static String getURI() {
        return NS;
    }

    /**
     * <p>
     * The namespace of the vocabulary as a resource
     * </p>
     */
    public static final Resource NAMESPACE = m_model.createResource(NS);

    // Vocabulary properties
    // /////////////////////////
    /**
     * has property: A relation between a FeatureOfInterest and a Property of
     * that feature.
     */
    public static final Property hasProperty = m_model.createProperty(NS + "hasProperty");
    /**
     * made observation: Relation between a Sensor and Observations it has made.
     */
    public static final Property madeObservation = m_model.createProperty(NS + "madeObservation");
    /**
     * sensing method used: A (measurement) procedure is a detailed description
     * of a measurement according to one or more measurement principles and to a
     * given measurement method, based on a measurement model and including any
     * calculation to obtain a measurement result [VIM 2.6]
     */
    public static final Property sensingMethodUsed = m_model.createProperty(NS + "sensingMethodUsed");
    /**
     * has measurement capability: Relation from a Sensor to a
     * MeasurementCapability describing the measurement properties of the
     * sensor.
     */
    public static final Property hasMeasurementCapability = m_model.createProperty(NS + "hasMeasurementCapability");
    /** has value. */
    public static final Property hasValue = m_model.createProperty(NS + "hasValue");
    /**
     * observation sampling time: The sampling time is the time that the result
     * applies to the feature-of-interest. This is the time usually required for
     * geospatial analysis of the result.
     */
    public static final Property observationSamplingTime = m_model.createProperty(NS + "observationSamplingTime");
    /** null. */
    public static final Property observedBy = m_model.createProperty(NS + "observedBy");
    /**
     * attached system: Relation between a Platform and any Systems (e.g.,
     * Sensors) that are attached to the Platform.
     */
    public static final Property attachedSystem = m_model.createProperty(NS + "attachedSystem");
    /**
     * has measurement property: Relation from a MeasurementCapability to a
     * MeasurementProperty. For example, to an accuracy (see notes at
     * MeasurementCapability).
     */
    public static final Property hasMeasurementProperty = m_model.createProperty(NS + "hasMeasurementProperty");
    /** end time. */
    public static final Property endTime = m_model.createProperty(NS + "endTime");
    /**
     * deployment process part: Has part relation between a deployment process
     * and its constituent processes.
     */
    public static final Property deploymentProcessPart = m_model.createProperty(NS + "deploymentProcessPart");
    /**
     * observation result: Relation linking an Observation (i.e., a description
     * of the context, the Situation, in which the observatioin was made) and a
     * Result, which contains a value representing the value associated with the
     * observed Property.
     */
    public static final Property observationResult = m_model.createProperty(NS + "observationResult");
    /**
     * in deployment: Relation between a Platform and a Deployment, recording
     * that the object was used as a platform for a system/sensor for a
     * particular deployment: as in this PhysicalObject is acting as a Platform
     * inDeployment Deployment.
     */
    public static final Property inDeployment = m_model.createProperty(NS + "inDeployment");
    /**
     * has deployment: Relation between a System and a Deployment, recording
     * that the System/Sensor was deployed in that Deployment.
     */
    public static final Property hasDeployment = m_model.createProperty(NS + "hasDeployment");
    /**
     * has survival property: Relation from a SurvivalRange to a Property
     * describing the survial range of a system. For example, to the temperature
     * extreme that a system can withstand before being considered damaged.
     */
    public static final Property hasSurvivalProperty = m_model.createProperty(NS + "hasSurvivalProperty");
    /**
     * implements: A relation between an entity that implements a method in some
     * executable way and the description of an algorithm, procedure or method.
     * For example, between a Sensor and the scientific measuring method that
     * the
     * Sensor uses to observe a Property.
     */
    public static final Property implement = m_model.createProperty(NS + "implements");
    /**
     * on platform: Relation between a System (e.g., a Sensor) and a Platform.
     * The relation locates the sensor relative to other described entities
     * entities: i.e., the Sensor s1's location is Platform p1. More precise
     * locations for sensors in space (relative to other entities, where
     * attached
     * to another entity, or in 3D space) are made using DOLCE's Regions
     * (SpaceRegion).
     */
    public static final Property onPlatform = m_model.createProperty(NS + "onPlatform");
    /** has survival range: A Relation from a System to a SurvivalRange. */
    public static final Property hasSurvivalRange = m_model.createProperty(NS + "hasSurvivalRange");
    /** deployed system: Relation between a deployment and the deployed system. */
    public static final Property deployedSystem = m_model.createProperty(NS + "deployedSystem");
    /**
     * observed property: Relation linking an Observation to the Property that
     * was observed. The observedProperty should be a Property (hasProperty) of
     * the FeatureOfInterest (linked by featureOfInterest) of this observation.
     */
    public static final Property observedProperty = m_model.createProperty(NS + "observedProperty");
    /**
     * has operating range: Relation from a System to an OperatingRange
     * describing the normal operating environment of the System.
     */
    public static final Property hasOperatingRange = m_model.createProperty(NS + "hasOperatingRange");
    /**
     * quality of observation: Relation linking an Observation to the adjudged
     * quality of the result. This is of course complimentary to the
     * MeasurementCapability information recorded for the Sensor that made the
     * Observation.
     */
    public static final Property qualityOfObservation = m_model.createProperty(NS + "qualityOfObservation");
    /**
     * has operating property: Relation from an OperatingRange to a Property.
     * For example, to a battery lifetime.
     */
    public static final Property hasOperatingProperty = m_model.createProperty(NS + "hasOperatingProperty");
    /**
     * feature of interest: A relation between an observation and the entity
     * whose quality was observed. For example, in an observation of the weight
     * of a person, the feature of interest is the person and the quality is
     * weight.
     */
    public static final Property featureOfInterest = m_model.createProperty(NS + "featureOfInterest");
    /**
     * implemented by: A relation between the description of an algorithm,
     * procedure or method and an entity that implements that method in some
     * executable way. For example, between a scientific measuring method and a
     * sensor the senses via that method.
     */
    public static final Property implementedBy = m_model.createProperty(NS + "implementedBy");
    /** has subsystem: Haspart relation between a system and its parts. */
    public static final Property hasSubSystem = m_model.createProperty(NS + "hasSubSystem");
    /**
     * observation result time: The result time shall describe the time when the
     * result became available, typically when the procedure associated with the
     * observation was completed For some observations this is identical to the
     * phenomenonTime. However, there are important cases where they
     * differ.[O&M]
     */
    public static final Property observationResultTime = m_model.createProperty(NS + "observationResultTime");
    /** start time. */
    public static final Property startTime = m_model.createProperty(NS + "startTime");
    /**
     * is property of: Relation between a FeatureOfInterest and a Property (a
     * Quality observable by a sensor) of that feature.
     */
    public static final Property isPropertyOf = m_model.createProperty(NS + "isPropertyOf");
    /**
     * deployed on platform: Relation between a deployment and the platform on
     * which the system was deployed.
     */
    public static final Property deployedOnPlatform = m_model.createProperty(NS + "deployedOnPlatform");

    // Vocabulary classes
    // /////////////////////////

    /**
     * Accuracy: The closeness of agreement between the value of an observation
     * and the true value of the observed quality.
     */
    public static final Resource Accuracy = m_model.createResource(NS + "Accuracy");
    /**
     * detection limit: An observed value for which the probability of falsely
     * claiming the absence of a component in a material is Î², given a
     * probability Î± of falsely claiming its presence.
     */
    public static final Resource DetectionLimit = m_model.createResource(NS + "DetectionLimit");
    /**
     * Frequency: The smallest possible time between one observation and the
     * next.
     */
    public static final Resource Frequency = m_model.createResource(NS + "Frequency");
    /**
     * Condition: Used to specify ranges for qualities that act as conditions on
     * a system/sensor's operation. For example, wind speed of 10-60m/s is
     * expressed as a condition linking a quality, wind speed, a unit of
     * measurement, metres per second, and a set of values, 10-60, and may be
     * used as the condition on a MeasurementProperty, for example, to state
     * that
     * a sensor has a particular accuracy in that condition.
     */
    public static final Resource Condition = m_model.createResource(NS + "Condition");
    /**
     * Sensor Output: A sensor outputs a piece of information (an observed
     * value), the value itself being represented by an ObservationValue.
     */
    public static final Resource SensorOutput = m_model.createResource(NS + "SensorOutput");
    /**
     * Precision: The closeness of agreement between replicate observations on
     * an unchanged or similar quality value: i.e., a measure of a sensor's
     * ability to consitently reproduce an observation.
     */
    public static final Resource Precision = m_model.createResource(NS + "Precision");
    /**
     * Resolution: The smallest difference in the value of a quality being
     * observed that would result in perceptably different values of observation
     * results.
     */
    public static final Resource Resolution = m_model.createResource(NS + "Resolution");
    /**
     * Feature of Interest: A feature is an abstraction of real world phenomena
     * (thing, person, event, etc).
     */
    public static final Resource FeatureOfInterest = m_model.createResource(NS + "FeatureOfInterest");
    /**
     * Sensor: A sensor can do (implements) sensing: that is, a sensor is any
     * entity that can follow a sensing method and thus observe some Property of
     * a FeatureOfInterest. Sensors may be physical devices, computational
     * methods, a laboratory setup with a person following a method, or any
     * other
     * thing that can follow a Sensing Method to observe a Property.
     */
    public static final Resource Sensor = m_model.createResource(NS + "Sensor");
    /**
     * Operating Property: An identifiable characteristic of the environmental
     * and other conditions in which the sensor is intended to operate. May
     * include power ranges, power sources, standard configurations, attachments
     * and the like.
     */
    public static final Resource OperatingProperty = m_model.createResource(NS + "OperatingProperty");
    /**
     * Operating Power Range: Power range in which system/sensor is expected to
     * operate.
     */
    public static final Resource OperatingPowerRange = m_model.createResource(NS + "OperatingPowerRange");
    /**
     * Maintenance Schedule: Schedule of maintenance for a system/sensor in the
     * specified conditions.
     */
    public static final Resource MaintenanceSchedule = m_model.createResource(NS + "MaintenanceSchedule");
    /**
     * Sensing: Sensing is a process that results in the estimation, or
     * calculation, of the value of a phenomenon.
     */
    public static final Resource Sensing = m_model.createResource(NS + "Sensing");
    /**
     * Latency: The time between a request for an observation and the sensor
     * providing a result.
     */
    public static final Resource Latency = m_model.createResource(NS + "Latency");
    /**
     * Survival Range: The conditions a sensor can be exposed to without damage:
     * i.e., the sensor continues to operate as defined using
     * MeasurementCapability. If, however, the SurvivalRange is exceeded, the
     * sensor is 'damaged' and MeasurementCapability specifications may no
     * longer
     * hold.
     */
    public static final Resource SurvivalRange = m_model.createResource(NS + "SurvivalRange");
    /** Sensing Device: A sensing device is a device that implements sensing. */
    public static final Resource SensingDevice = m_model.createResource(NS + "SensingDevice");
    /**
     * Sensitivity: Sensitivity is the quotient of the change in a result of
     * sensor and the corresponding change in a value of a quality being
     * observed.
     */
    public static final Resource Sensitivity = m_model.createResource(NS + "Sensitivity");
    /**
     * Stimulus: An Event in the real world that 'triggers' the sensor. The
     * properties associated to the stimulus may be different to eventual
     * observed property. It is the event, not the object that triggers the
     * sensor.
     */
    public static final Resource Stimulus = m_model.createResource(NS + "Stimulus");
    /**
     * Measurement Range: The set of values that the sensor can return as the
     * result of an observation under the defined conditions with the defined
     * measurement properties. (If no conditions are specified or the conditions
     * do not specify a range for the observed qualities, the measurement range
     * is to be taken as the condition for the observed qualities.)
     */
    public static final Resource MeasurementRange = m_model.createResource(NS + "MeasurementRange");
    /**
     * System: System is a unit of abstraction for pieces of infrastructure (and
     * we largely care that they are) for sensing. A system has components, its
     * subsystems, which are other systems.
     */
    public static final Resource System = m_model.createResource(NS + "System");
    /**
     * System Lifetime: Total useful life of a sensor/system (expressed as total
     * life since manufacture, time in use, number of operations, etc.).
     */
    public static final Resource SystemLifetime = m_model.createResource(NS + "SystemLifetime");
    /**
     * Property: An observable Quality of an Event or Object. That is, not a
     * quality of an abstract entity as is also allowed by DUL's Quality, but
     * rather an aspect of an entity that is intrinsic to and cannot exist
     * without the entity and is observable by a sensor.
     */
    public static final Resource Property = m_model.createResource(NS + "Property");
    /**
     * Measurement Property: An identifiable and observable characteristic of a
     * sensor's observations or ability to make observations.
     */
    public static final Resource MeasurementProperty = m_model.createResource(NS + "MeasurementProperty");
    /**
     * Process: A process has an output and possibly inputs and, for a composite
     * process, describes the temporal and dataflow dependencies and
     * relationships amongst its parts. [SSN XG]
     */
    public static final Resource Process = m_model.createResource(NS + "Process");
    /**
     * Deployment: The ongoing Process of Entities (for the purposes of this
     * ontology, mainly sensors) deployed for a particular purpose. For example,
     * a particular Sensor deployed on a Platform, or a whole network of Sensors
     * deployed for an observation campaign. The deployment may have sub
     * processes, such as installation, maintenance, addition, and
     * decomissioning
     * and removal.
     */
    public static final Resource Deployment = m_model.createResource(NS + "Deployment");
    /**
     * Measurement Capability: Collects together measurement properties
     * (accuracy, range, precision, etc) and the environmental conditions in
     * which those properties hold, representing a specification of a sensor's
     * capability in those conditions.The conditions specified here are those
     * that affect the measurement properties, while those in OperatingRange
     * represent the sensor's standard operating conditions, including
     * conditions
     * that don't affect the observations.
     */
    public static final Resource MeasurementCapability = m_model.createResource(NS + "MeasurementCapability");
    /** Battery Lifetime: Total useful life of a battery. */
    public static final Resource BatteryLifetime = m_model.createResource(NS + "BatteryLifetime");
    /**
     * Input: Any information that is provided to a process for its use [MMI
     * OntDev]
     */
    public static final Resource Input = m_model.createResource(NS + "Input");
    /**
     * Drift: A, continuous or incremental, change in the reported values of
     * observations over time for an unchanging quality.
     */
    public static final Resource Drift = m_model.createResource(NS + "Drift");
    /**
     * Deployment-related Process: Place to group all the various Processes
     * related to Deployment. For example, as well as Deplyment, installation,
     * maintenance, deployment of further sensors and the like would all be
     * classified under DeploymentRelatedProcess.
     */
    public static final Resource DeploymentRelatedProcess = m_model.createResource(NS + "DeploymentRelatedProcess");
    /**
     * Sensor Input: An Event in the real world that 'triggers' the sensor. The
     * properties associated to the stimulus may be different to eventual
     * observed property. It is the event, not the object that triggers the
     * sensor.
     */
    public static final Resource SensorInput = m_model.createResource(NS + "SensorInput");
    /**
     * Response time: The time between a (step) change inthe value of an
     * observed quality and a sensor (possibly with specified error) 'settling'
     * on an observed value.
     */
    public static final Resource ResponseTime = m_model.createResource(NS + "ResponseTime");
    /**
     * Observation Value: The value of the result of an Observation. An
     * Observation has a result which is the output of some sensor, the result
     * is an information object that encodes some value for a Feature.
     */
    public static final Resource ObservationValue = m_model.createResource(NS + "ObservationValue");
    /**
     * Sensor Data Sheet: A data sheet records properties of a sensor. A data
     * sheet might describe for example the accuracy in various conditions, the
     * power use, the types of connectors that the sensor has, etc. Generally a
     * sensor's properties are recorded directly (with hasMeasurementCapability,
     * for example), but the data sheet can be used for example to record the
     * manufacturers specifications verses observed capabilites, or if more is
     * known than the manufacturer specifies, etc. The data sheet is an
     * information object about the sensor's properties, rather than a direct
     * link to the actual properties themselves.
     */
    public static final Resource SensorDataSheet = m_model.createResource(NS + "SensorDataSheet");
    /**
     * Observation: An Observation is a Situation in which a Sensing method has
     * been used to estimate or calculate a value of a Property of a
     * FeatureOfInterest. Links to Sensing and Sensor describe what made the
     * Observation and how; links to Property and Feature detail what was
     * sensed;
     * the result is the output of a Sensor; other metadata details times etc.
     */
    public static final Resource Observation = m_model.createResource(NS + "Observation");
    /**
     * Selectivity: Selectivity is a property of a sensor whereby it provides
     * observed values for one or more qualities such that the values of each
     * quality are independent of other qualities in the phenomenon, body, or
     * substance being investigated.
     */
    public static final Resource Selectivity = m_model.createResource(NS + "Selectivity");
    /**
     * Survival Property: An identifiable characteristic that represents the
     * extent of the sensors useful life. Might include for example total
     * battery life or number of recharges, or, for sensors that are used only a
     * fixed number of times, the number of observations that can be made before
     * the sensing capability is depleted.
     */
    public static final Resource SurvivalProperty = m_model.createResource(NS + "SurvivalProperty");
    /**
     * Device: A device is a physical piece of technology - a system in a box.
     * Devices may of course be built of smaller devices and software components
     * (i.e. systems have components).
     */
    public static final Resource Device = m_model.createResource(NS + "Device");
    /**
     * Operating Range: The environmental conditions and characteristics of a
     * system/sensor's normal operating environment. Can be used to specify for
     * example the standard environmental conditions in which the sensor is
     * expected to operate (a Condition with no OperatingProperty), or how the
     * environmental and other operating properties relate: i.e., that the
     * maintenance schedule or power requirements differ according to the
     * conditions.
     */
    public static final Resource OperatingRange = m_model.createResource(NS + "OperatingRange");
    /** Output: Any information that is reported from a process. [MMI OntDev] */
    public static final Resource Output = m_model.createResource(NS + "Output");
    /**
     * Platform: An Entity to which other Entities can be attached - particuarly
     * Sensors and other Platforms. For example, a post might act as the
     * Platform, a bouy might act as a Platform, or a fish might act as a
     * Platform for an attached sensor.
     */
    public static final Resource Platform = m_model.createResource(NS + "Platform");
    // Vocabulary individuals
    // /////////////////////////
}
