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
package cc.kuka.odysseus.ontology.storage.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public final class DUL {

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
    public static final String NS = "http://www.loa-cnr.it/ontologies/DUL.owl#";

    /**
     * <p>
     * The namespace of the vocabulary as a string
     * </p>
     *
     * @see #NS
     */
    public static String getURI() {
        return DUL.NS;
    }

    /**
     * <p>
     * The namespace of the vocabulary as a resource
     * </p>
     */
    public static final Resource NAMESPACE = DUL.m_model.createResource(DUL.NS);

    // Vocabulary properties
    // /////////////////////////
    /**
     * has region: A relation between entities and regions, e.g. 'the number of
     * wheels of that truck is 12', 'the time of the experiment is August 9th,
     * 2004', 'the whale has been localized at 34 degrees E, 20 degrees S'.
     */
    public static final Property hasRegion = DUL.m_model.createProperty(DUL.NS + "hasRegion");
    /**
     * is participant in: A relation between an object and a process, e.g. 'John
     * took part in the discussion', 'a large mass of snow fell during the
     * avalanche', or 'a cook, some sugar, flour, etc. are all present in the
     * cooking of a cake'.
     */
    public static final Property isParticipantIn = DUL.m_model.createProperty(DUL.NS + "isParticipantIn");
    /**
     * satisfies: A relation between a Situation and a Description, e.g. the
     * execution of a Plan satisfies that plan.
     */
    public static final Property satisfies = DUL.m_model.createProperty(DUL.NS + "satisfies");
    /**
     * has location: A generic, relative localization, holding between any
     * entities. E.g. 'the cat is on the mat', 'Omar is in Samarcanda', 'the
     * wound is close to the femural artery'.For 'absolute' locations, see
     * SpaceRegion
     */
    public static final Property hasLocation = DUL.m_model.createProperty(DUL.NS + "hasLocation");
    /**
     * has participant: A relation between an object and a process, e.g. 'John
     * took part in the discussion', 'a large mass of snow fell during the
     * avalanche', or 'a cook, some sugar, flour, etc. are all present in the
     * cooking of a cake'.
     */
    public static final Property hasParticipant = DUL.m_model.createProperty(DUL.NS + "hasParticipant");
    /**
     * is setting for: A relation between situations and entities, e.g. 'this
     * morning I've prepared my coffee with a new fantastic Arabica', i.e.: the
     * preparation of my coffee this morning is the setting for (an amount of) a
     * new fantastic Arabica.
     */
    public static final Property isSettingFor = DUL.m_model.createProperty(DUL.NS + "isSettingFor");
    /**
     * has quality: A relation between entities and qualities, e.g. 'Dmitri's
     * skin is yellowish'.
     */
    public static final Property hasQuality = DUL.m_model.createProperty(DUL.NS + "hasQuality");
    /**
     * includes object: A relation between situations and objects, e.g. 'this
     * morning I've prepared my coffee and had my fingers burnt' (i.e.: the
     * preparation of my coffee this morning included me).
     */
    public static final Property includesObject = DUL.m_model.createProperty(DUL.NS + "includesObject");
    /**
     * is location of: A generic, relative localization, holding between any
     * entities. E.g. 'Rome is the seat of the Pope', 'the liver is the location
     * of the tumor'.For 'absolute' locations, see SpaceRegion
     */
    public static final Property isLocationOf = DUL.m_model.createProperty(DUL.NS + "isLocationOf");
    /**
     * describes: The relation between a Description and an Entity : a
     * Description gives a unity to a Collection of parts (the components), or
     * constituents, by assigning a Role to each of them in the context of a
     * whole Object (the system).A same Entity can be given different
     * descriptions, for example, an old cradle can be given a unifying
     * Description based on the original aesthetic design, the functionality it
     * was built for, or a new aesthetic functionality in which it can be used
     * as
     * a flower pot.
     */
    public static final Property describes = DUL.m_model.createProperty(DUL.NS + "describes");
    /** is object included in. */
    public static final Property isObjectIncludedIn = DUL.m_model.createProperty(DUL.NS + "isObjectIncludedIn");
    /**
     * has part: A schematic relation between any entities, e.g. 'the human body
     * has a brain as part', '20th century contains year 1923', 'World War II
     * includes the Pearl Harbour event'.Subproperties and restrictions can be
     * used to specialize hasPart for objects, events, etc.
     */
    public static final Property hasPart = DUL.m_model.createProperty(DUL.NS + "hasPart");
    /**
     * is described by: The relation between an Entity and a Description: a
     * Description gives a unity to a Collection of parts (the components), or
     * constituents, by assigning a Role to each of them in the context of a
     * whole Object (the system).A same Entity can be given different
     * descriptions, for example, an old cradle can be given a unifying
     * Description based on the original aesthetic design, the functionality it
     * was built for, or a new aesthetic functionality in which it can be used
     * as
     * a flower pot.
     */
    public static final Property isDescribedBy = DUL.m_model.createProperty(DUL.NS + "isDescribedBy");
    /**
     * is quality of: A relation between entities and qualities, e.g. 'Dmitri's
     * skin is yellowish'.
     */
    public static final Property isQualityOf = DUL.m_model.createProperty(DUL.NS + "isQualityOf");
    /**
     * è un concetto usato in: A more generic relation holding between a
     * Description and a Concept. In order to be used, a Concept must be
     * previously definedIn another Description
     */
    public static final Property isConceptUsedIn = DUL.m_model.createProperty(DUL.NS + "isConceptUsedIn");
    /**
     * classifica: A relation between a Concept and an Entity, e.g. the Role
     * 'student' classifies a Person 'John'.
     */
    public static final Property classifies = DUL.m_model.createProperty(DUL.NS + "classifies");
    /**
     * directly follows: The intransitive follows relation. For example,
     * Wednesday directly precedes Thursday. Directness of precedence depends on
     * the designer conceptualization.
     */
    public static final Property directlyFollows = DUL.m_model.createProperty(DUL.NS + "directlyFollows");
    /**
     * includes event: A relation between situations and events, e.g. 'this
     * morning I've prepared my coffee and had my fingers burnt' (i.e.: the
     * preparation of my coffee this morning included a burning of my fingers).
     */
    public static final Property includesEvent = DUL.m_model.createProperty(DUL.NS + "includesEvent");
    /**
     * è associato al concetto: Any relation between concepts, e.g.
     * superordinated, conceptual parthood, having a parameter, having a task,
     * superordination, etc.
     */
    public static final Property isRelatedToConcept = DUL.m_model.createProperty(DUL.NS + "isRelatedToConcept");
    /**
     * è classificato da: A relation between a Concept and an Entity, e.g. 'John
     * is considered a typical rude man'; your last concert constitutes the
     * achievement of a lifetime; '20-year-old means she's mature enough'.
     */
    public static final Property isClassifiedBy = DUL.m_model.createProperty(DUL.NS + "isClassifiedBy");
    /**
     * directly precedes: The intransitive precedes relation. For example,
     * Monday directly precedes Tuesday. Directness of precedence depends on the
     * designer conceptualization.
     */
    public static final Property directlyPrecedes = DUL.m_model.createProperty(DUL.NS + "directlyPrecedes");
    /**
     * has setting: A relation between entities and situations, e.g. 'this
     * morning I've prepared my coffee with a new fantastic Arabica', i.e.: (an
     * amount of) a new fantastic Arabica hasSetting the preparation of my
     * coffee
     * this morning.
     */
    public static final Property hasSetting = DUL.m_model.createProperty(DUL.NS + "hasSetting");
    /** is related to description: Any relation between descriptions. */
    public static final Property isRelatedToDescription = DUL.m_model.createProperty(DUL.NS + "isRelatedToDescription");
    /**
     * follows: A relation between entities, expressing a 'sequence' schema.
     * E.g. 'year 2000 follows 1999', 'preparing coffee' follows 'deciding what
     * coffee to use', 'II World War follows I World War', etc. It can be used
     * between tasks, processes or time intervals, and subproperties would fit
     * best in order to distinguish the different uses.
     */
    public static final Property follows = DUL.m_model.createProperty(DUL.NS + "follows");
    /**
     * usa il concetto: A generic relation holding between a Description and a
     * Concept. In order to be used, a Concept must be previously definedIn
     * another Description. This last condition cannot be encoded for object
     * properties in OWL.
     */
    public static final Property usesConcept = DUL.m_model.createProperty(DUL.NS + "usesConcept");
    /** is event included in. */
    public static final Property isEventIncludedIn = DUL.m_model.createProperty(DUL.NS + "isEventIncludedIn");
    /**
     * esprime: This is a large comment field for those who want to investigate
     * the different uses of the 'expresses' relation for modeling different
     * approaches to meaning characterization and modeling.For example, in all
     * these cases, some aspect of meaning is involved:- Beehive means
     * "a structure in which bees are kept, typically in the form of a dome or box."
     * (Oxford dictionary)- 'Beehive' is a synonym in noun synset 09218159
     * "beehive|hive" (WordNet)- 'the term Beehive can be interpreted as the
     * fact
     * of 'being a beehive', i.e. a relation that holds for concepts such as
     * Bee,
     * Honey, Hosting, etc.'- 'the text of Italian apiculture regulation
     * expresses a rule by which beehives should be kept at least one kilometer
     * away from inhabited areas'- 'the term Beehive expresses the concept
     * Beehive'- ''Beehive' for apiculturists does not express the same meaning
     * as for, say, fishermen'- 'Your meaning of 'Beautiful' does not seem to
     * fit
     * mine'- ''Beehive' is formally interpreted as the set of all beehives'-
     * 'from the term 'Beehive', we can build a vector space of statistically
     * significant cooccurring terms in the documents that contain it'- the
     * lexeme 'Belly' expresses the role 'Body_Part' in the frame
     * 'ObservableBodyParts' (FrameNet)As the examples suggest, the 'meaning of
     * meaning' is dependent on the background approach/theory that one assumes.
     * One can hardly make a summary of the too many approaches and theories of
     * meaning, therefore this relation is maybe the most controversial and
     * difficult to explain; normally, in such cases it would be better to give
     * up formalizing. However, the usefulness of having a 'semantic
     * abstraction'
     * in modeling information objects is so high (e.g. for the semantic web,
     * interoperability, reengineering, etc.), that we accept this challenging
     * task, although without taking any particular position in the debate. We
     * provide here some examples, which we want to generalize upon when using
     * the 'expresses' relation to model semantic aspects of social reality.In
     * the most common approach, lexicographers that write dictionaries,
     * glossaries, etc. assume that the meaning of a term is a paraphrase (or
     * 'gloss', or 'definition'). Another approach is provided by concept
     * schemes
     * like thesauri and lexicons, which assume that the meaning of a term is a
     * 'concept', encoded as a 'lemma', 'synset', or 'descriptor'.Still another
     * approach is that of psychologists and cognitive scientists, which often
     * assume that the meaning of an information object is a concept encoded in
     * the mind or cognitive system of an agent. A radically different approach
     * is taken by social scientists and semioticians, who usually assume that
     * meanings of an information object are spread across the communication
     * practices in which members of a community use that object.Another
     * approach
     * that tackles the distributed nature of meaning is assumed by geometrical
     * models of semantics, which assume that the meaning of an
     * InformationObject
     * (e.g. a word) results from the set of informational contexts (e.g. within
     * texts) in which that object is used similarly.The logical approach to
     * meaning is still different, since it assumes that the meaning of e.g. a
     * term is equivalent to the set of individuals that the term can be applied
     * to; for example, the meaning of 'Ali' is e.g. an individual person called
     * Ali, the meaning of 'Airplane' is e.g. the set of airplanes, etc.
     * Finally,
     * an approach taken by structuralist linguistics and frame semantics is
     * that
     * a meaning is the relational context in which an information object can be
     * applied; for example, a meaning of 'Airplane' is situated e.g. in the
     * context ('frame') of passenger airline flights.These different approaches
     * are not necessarily conflicting, and they mostly talk about different
     * aspects of so-called 'semantics'. They can be summarized and modelled
     * within DOLCE-Ultralite as follows (notice that such list is far from
     * exhaustive):(1) Informal meaning (as for linguistic or commonsense
     * semantics: a distinction is assumed between (informal) meaning and
     * reference; see isAbout for an alternative pattern on reference) -
     * Paraphrase meaning (as for lexicographic semantics). Here it is modelled
     * as the expresses relation between instances of InformationObject and
     * different instances of InformationObject that act as 'paraphrases' -
     * Conceptual meaning (as for 'concept scheme' semantics). Here it is
     * modelled as the expresses relation between instances of InformationObject
     * and instances of Concept - Relational meaning (as for frame semantics).
     * Here it is modelled as the expresses relation between instances of
     * InformationObject and instances of Description - Cognitive meaning (as
     * for
     * 'psychological' semantics). Here it is modelled as the expresses relation
     * between any instance of InformationObject and any different instance of
     * InformationObject that isRealizedBy a mental, cognitive or neural state
     * (depending on which theory of mind is assumed). Such states can be
     * considered here as instances of Process (occurring in the mind, cognitive
     * system, or neural system of an agent) - Cultural meaning (as for 'social
     * science' semantics). Here it is modelled as the expresses relation
     * between
     * instances of InformationObject and instances of SocialObject
     * (institutions, cultural paradigms, norms, social practices, etc.) -
     * Distributional meaning (as for geometrical models of meaning). Here it is
     * modelled as the expresses relation between any instance of
     * InformationObject and any different instance of InformationObject that
     * isFormallyRepresentedIn some (geometrical) Region (e.g. a vector
     * space)(2)
     * Formal meaning (as for logic and formal semantics: no distinction is
     * assumed between informal meaning and reference, therefore between
     * 'expresses' and 'isAbout', which can be used interchangeably) -
     * Object-level formal meaning (as in the traditional first-order logic
     * semantics). Here it is modelled as the expresses relation between an
     * instance of InformationObject and an instance of Collection that
     * isGroundingFor (in most cases) a Set; isGroundingFor is defined in the
     * ontology: http://www.loa-cnr.it/ontologies/IOLite.owl - Modal formal
     * meaning (as in possible-world semantics). Here it is modelled as the
     * expresses relation between an instance of InformationObject and an
     * instance of Collection that isGroundingFor a Set, and which isPartOf some
     * different instance of Collection that isGroundingFor a PossibleWorldThis
     * is only a first step to provide a framework, in which one can model
     * different aspects of meaning. A more developed ontology should approach
     * the problem of integrating the different uses of 'expresses', so that
     * different theories, resources, methods can interoperate.
     */
    public static final Property expresses = DUL.m_model.createProperty(DUL.NS + "expresses");
    /**
     * has data value: A datatype property that encodes values from a datatype
     * for an Entity. There are several ways to encode values in DOLCE
     * (Ultralite):1) Directly assert an xsd:_ value to an Entity by using
     * hasDataValue2) Assert a Region for an Entity by using hasRegion, and then
     * assert an xsd:_ value to that Region, by using hasRegionDataValue3)
     * Assert
     * a Quality for an Entity by using hasQuality, then assert a Region for
     * that
     * Quality, and assert an xsd:_ value to that Region, by using
     * hasRegionDataValue4) When the value is required, but not directly
     * observed, assert a Parameter for an xsd:_ value by using
     * hasParameterDataValue, and then associate the Parameter to an Entity by
     * using isConstraintFor5) When the value is required, but not directly
     * observed, you can also assert a Parameter for a Region by using
     * parametrizes, and then assert an xsd:_ value to that Region, by using
     * hasRegionDataValueThe five approaches obey different requirements. For
     * example, a simple value can be easily asserted by using pattern (1), but
     * if one needs to assert an interval between two values, a Region should be
     * introduced to materialize that interval, as pattern (2) suggests.
     * Furthermore, if one needs to distinguish the individual Quality of a
     * value, e.g. the particular nature of the density of a substance, pattern
     * (3) can be used. Patterns (4) and (5) should be used instead when a
     * constraint or a selection is modeled, independently from the actual
     * observation of values in the real world.
     */
    public static final Property hasDataValue = DUL.m_model.createProperty(DUL.NS + "hasDataValue");
    /**
     * definisce: A relation between a Description and a Concept, e.g. a
     * Workflow for a governmental Organization defines the Role 'officer', or
     * 'the Italian Traffic Law defines the role Vehicle'.
     */
    public static final Property defines = DUL.m_model.createProperty(DUL.NS + "defines");
    /**
     * is expressed by: A relation between a dul:SocialObject (the 'meaning')
     * and a dul:InformationObject (the 'expression'). For example: 'A Beehive
     * is a structure in which bees are kept, typically in the form of a dome or
     * box.' (Oxford dictionary)'; 'the term Beehive expresses the concept
     * Beehive in my apiculture ontology'.The intuition for 'meaning' is
     * intended
     * to be very broad. A separate, large comment is included in the encoding
     * of
     * 'expresses', for those who want to investigate more on what kind of
     * meaning can be represented in what form.
     */
    public static final Property isExpressedBy = DUL.m_model.createProperty(DUL.NS + "isExpressedBy");
    /**
     * has region data value: A datatype property that encodes values for a
     * Region, e.g. a float for the Region Height.
     */
    public static final Property hasRegionDataValue = DUL.m_model.createProperty(DUL.NS + "hasRegionDataValue");
    /**
     * is part of: A relation between any entities, e.g.'brain is a part of the
     * human body'.
     */
    public static final Property isPartOf = DUL.m_model.createProperty(DUL.NS + "isPartOf");
    /**
     * precedes: A relation between entities, expressing a 'sequence' schema.
     * E.g. 'year 1999 precedes 2000', 'deciding what coffee to use' precedes
     * 'preparing coffee', 'World War II follows World War I', 'in the Milan to
     * Rome autoroute, Bologna precedes Florence', etc.It can then be used
     * between tasks, processes, time intervals, spatially locate objects,
     * situations, etc. Subproperties can be defined in order to distinguish the
     * different uses.
     */
    public static final Property precedes = DUL.m_model.createProperty(DUL.NS + "precedes");
    /**
     * è definito in: A relation between a Description and a Concept, e.g. a
     * Workflow for a governmental Organization defines the Role 'officer', or
     * 'the Italian Traffic Law defines the role Vehicle'.
     */
    public static final Property isDefinedIn = DUL.m_model.createProperty(DUL.NS + "isDefinedIn");
    /**
     * is region for: A relation between entities and regions, e.g. 'the color
     * of my car is red'.
     */
    public static final Property isRegionFor = DUL.m_model.createProperty(DUL.NS + "isRegionFor");
    /**
     * is time of observation of: A relation to represent a (past, present or
     * future) TimeInterval at which an Entity is observable.In order to encode
     * a specific time, a data value should be related to the TimeInterval. An
     * alternative way of representing time is the datatype property:
     * hasIntervalDate
     */
    public static final Property isTimeOfObservationOf = DUL.m_model.createProperty(DUL.NS + "isTimeOfObservationOf");
    /**
     * expands: A partial order relation that holds between descriptions. It
     * represents the proper part relation between a description and another
     * description featuring the same properties as the former, with at least
     * one
     * additional one.Descriptions can be expanded either by adding other
     * descriptions as parts, or by refining concepts that are used by them. An
     * 'intention' to expand must be present (unless purely formal theories are
     * considered, but even in this case a criterion of relevance is usually
     * active).
     */
    public static final Property expands = DUL.m_model.createProperty(DUL.NS + "expands");
    /**
     * è un ruolo definito in: A relation between a description and a role, e.g.
     * the role 'Ingredient' is defined in the recipe for a cake.
     */
    public static final Property isRoleDefinedIn = DUL.m_model.createProperty(DUL.NS + "isRoleDefinedIn");
    /**
     * ha parametro: A Concept can have a Parameter that constrains the
     * attributes that a classified Entity can have in a certain Situation, e.g.
     * a 4WheelDriver Role definedIn the ItalianTrafficLaw has a MinimumAge
     * parameter on the Amount 16.
     */
    public static final Property hasParameter = DUL.m_model.createProperty(DUL.NS + "hasParameter");
    /** is agent included in. */
    public static final Property isAgentIncludedIn = DUL.m_model.createProperty(DUL.NS + "isAgentIncludedIn");
    /**
     * is precondition of: Direct precedence applied to situations. E.g.,
     * 'claiming to find nuclear weapons in a foreign country is a precondition
     * to declare war against it'.
     */
    public static final Property isPreconditionOf = DUL.m_model.createProperty(DUL.NS + "isPreconditionOf");
    /**
     * è un obiettivo per: A relation between roles and tasks, e.g. 'students
     * have the duty of giving exams' (i.e. the Role 'student' hasTask the Task
     * 'giving exams').
     */
    public static final Property isTaskOf = DUL.m_model.createProperty(DUL.NS + "isTaskOf");
    /** is agent involved in: Agent participation. */
    public static final Property isAgentInvolvedIn = DUL.m_model.createProperty(DUL.NS + "isAgentInvolvedIn");
    /**
     * esprime il concetto: A relation between an InformationObject and a
     * Concept , e.g. the term "dog" expresses the Concept "dog". For expressing
     * a relational meaning, see the more general object property: expresses
     */
    public static final Property expressesConcept = DUL.m_model.createProperty(DUL.NS + "expressesConcept");
    /**
     * esegue il task: A relation between an action and a task, e.g. 'putting
     * some water in a pot and putting the pot on a fire until the water starts
     * bubbling' executes the task 'boiling'.
     */
    public static final Property executesTask = DUL.m_model.createProperty(DUL.NS + "executesTask");
    /**
     * ha valore: Parametrizes values from a datatype. For example, a Parameter
     * MinimumAgeForDriving hasParameterDataValue 18 on datatype xsd:int, in the
     * Italian traffic code. In this example, MinimumAgeForDriving isDefinedIn
     * the Norm ItalianTrafficCodeAgeDriving.More complex parametrization
     * requires workarounds. E.g. AgeRangeForDrugUsage could parametrize data
     * value: 14 to 50 on the datatype: xsd:int. Since complex datatypes are not
     * allowed in OWL1.0, a solution to this can only work by creating two
     * 'sub-parameters': MinimumAgeForDrugUsage (that hasParameterDataValue 14)
     * and MaximumAgeForDrugUsage (that hasParameterDataValue 50), which are
     * components of (cf. hasComponent) the main Parameter
     * AgeRangeForDrugUsage.Ordering on subparameters can be created by using or
     * specializing the object property 'precedes'.
     */
    public static final Property hasParameterDataValue = DUL.m_model.createProperty(DUL.NS + "hasParameterDataValue");
    /**
     * is observable at: A relation to represent a (past, present or future)
     * TimeInterval at which an Entity is observable.In order to encode a
     * specific time, a data value should be related to the TimeInterval. An
     * alternative way of representing time is the datatype property:
     * hasIntervalDate
     */
    public static final Property isObservableAt = DUL.m_model.createProperty(DUL.NS + "isObservableAt");
    /**
     * includes time: A relation between situations and time intervals, e.g.
     * 'this morning I've prepared my coffee and had my fingers burnt' (i.e.:
     * preparing my coffee was held this morning). A data value attached to the
     * time interval typically complements this modelling pattern.
     */
    public static final Property includesTime = DUL.m_model.createProperty(DUL.NS + "includesTime");
    /** is action included in. */
    public static final Property isActionIncludedIn = DUL.m_model.createProperty(DUL.NS + "isActionIncludedIn");
    /**
     * definisce il task: A relation between a description and a task, e.g. the
     * recipe for a cake defines the task 'boil'.
     */
    public static final Property definesTask = DUL.m_model.createProperty(DUL.NS + "definesTask");
    /**
     * ha come obiettivo: A relation between roles and tasks, e.g. 'students
     * have the duty of giving exams' (i.e. the Role 'student' hasTask the Task
     * 'giving exams').
     */
    public static final Property hasTask = DUL.m_model.createProperty(DUL.NS + "hasTask");
    /** involves agent: Agent participation. */
    public static final Property involvesAgent = DUL.m_model.createProperty(DUL.NS + "involvesAgent");
    /**
     * is postcondition of: Direct succession applied to situations. E.g.,
     * 'Taking some rest is a postcondition of my search for a hotel'.
     */
    public static final Property isPostconditionOf = DUL.m_model.createProperty(DUL.NS + "isPostconditionOf");
    /** is time included in. */
    public static final Property isTimeIncludedIn = DUL.m_model.createProperty(DUL.NS + "isTimeIncludedIn");
    /**
     * has interval date: A datatype property that encodes values from xsd:date
     * for a TimeInterval; a same TimeInterval can have more than one xsd:date
     * value: begin date, end date, date at which the interval holds, as well as
     * dates expressed in different formats: xsd:gYear, xsd:dateTime, etc.
     */
    public static final Property hasIntervalDate = DUL.m_model.createProperty(DUL.NS + "hasIntervalDate");
    /**
     * has event date: A datatype property that encodes values from xsd:date for
     * an Event; a same Event can have more than one xsd:date value: begin date,
     * end date, date at which the interval holds, as well as dates expressed in
     * different formats: xsd:gYear, xsd:dateTime, etc.
     */
    public static final Property hasEventDate = DUL.m_model.createProperty(DUL.NS + "hasEventDate");
    /**
     * has precondition: Direct precedence applied to situations. E.g., 'A
     * precondition to declare war against a foreign country is claiming to find
     * nuclear weapons in it'.
     */
    public static final Property hasPrecondition = DUL.m_model.createProperty(DUL.NS + "hasPrecondition");
    /**
     * definisce il ruolo: A relation between a description and a role, e.g. the
     * recipe for a cake defines the role 'ingredient'.
     */
    public static final Property definesRole = DUL.m_model.createProperty(DUL.NS + "definesRole");
    /**
     * is constraint for: A relation between parameters and entities. It allows
     * to assert generic constraints (encoded as parameters), e.g.
     * MinimumAgeForDriving isConstraintFor John (where John is a legal subject
     * under the TrafficLaw).The intended semantics (not expressible in OWL) is
     * that a Parameter isConstraintFor and Entity if the Parameter
     * isParameterFor a Concept that classifies that Entity; moreover, it
     * entails
     * that a Parameter parametrizes a Region that isRegionFor that Entity. The
     * use in OWL is therefore a shortcut to annotate what Parameter constrains
     * what Entity
     */
    public static final Property isConstraintFor = DUL.m_model.createProperty(DUL.NS + "isConstraintFor");
    /**
     * ha vincolo: A relation between parameters and entities. It allows to
     * assert generic constraints (encoded as parameters), e.g.
     * MinimumAgeForDriving isConstraintFor John (where John is a legal subject
     * under the TrafficLaw).The intended semantics (not expressible in OWL) is
     * that a Parameter isParameterFor a Concept that classifies an Entity;
     * moreover, it entails that a Parameter parametrizes a Region that
     * isRegionFor that Entity.
     */
    public static final Property hasConstraint = DUL.m_model.createProperty(DUL.NS + "hasConstraint");
    /**
     * includes agent: A relation between situations and persons, e.g. 'this
     * morning I've prepared my coffee and had my fingers burnt' (i.e.: the
     * preparation of my coffee this morning included me).
     */
    public static final Property includesAgent = DUL.m_model.createProperty(DUL.NS + "includesAgent");
    /**
     * è eseguito mediante: A relation between an action and a task, e.g.
     * 'putting some water in a pot and putting the pot on a fire until the
     * water starts bubbling' executes the task 'boiling'.
     */
    public static final Property isExecutedIn = DUL.m_model.createProperty(DUL.NS + "isExecutedIn");
    /**
     * è un concetto espresso da: A relation between an InformationObject and a
     * Concept , e.g. the term "dog" expresses the Concept "dog". For expressing
     * a relational meaning, see the more general object property: expresses
     */
    public static final Property isConceptExpressedBy = DUL.m_model.createProperty(DUL.NS + "isConceptExpressedBy");
    /**
     * è un parametro per: A Concept can have a Parameter that constrains the
     * attributes that a classified Entity can have in a certain Situation, e.g.
     * a 4WheelDriver Role definedIn the ItalianTrafficLaw has a MinimumAge
     * parameter on the Amount 16.
     */
    public static final Property isParameterFor = DUL.m_model.createProperty(DUL.NS + "isParameterFor");
    /**
     * è subordinato a: Direct succession applied to concepts. E.g. the role
     * 'Officer' is subordinated to 'Director'.
     */
    public static final Property isSubordinatedTo = DUL.m_model.createProperty(DUL.NS + "isSubordinatedTo");
    /**
     * has postcondition: Direct succession applied to situations. E.g., 'A
     * postcondition of our Plan is to have things settled'.
     */
    public static final Property hasPostcondition = DUL.m_model.createProperty(DUL.NS + "hasPostcondition");
    /**
     * ha ruolo: A relation between an object and a role, e.g. the person 'John'
     * has role 'student'.
     */
    public static final Property hasRole = DUL.m_model.createProperty(DUL.NS + "hasRole");
    /**
     * è superordinato a: Direct precedence applied to concepts. E.g. the role
     * 'Executive' is superordinated to 'DepartmentManager'.
     */
    public static final Property isSuperordinatedTo = DUL.m_model.createProperty(DUL.NS + "isSuperordinatedTo");
    /**
     * è un ruolo di: A relation between an object and a role, e.g. 'student' is
     * the role of 'John'.
     */
    public static final Property isRoleOf = DUL.m_model.createProperty(DUL.NS + "isRoleOf");
    /**
     * è parametrizzato da: The relation between a Parameter, e.g. 'MajorAge',
     * and a Region, e.g. '>17 year'.
     */
    public static final Property isParametrizedBy = DUL.m_model.createProperty(DUL.NS + "isParametrizedBy");
    /**
     * includes action: A relation between situations and actions, e.g. 'this
     * morning I've prepared my coffee and had my fingers burnt' (i.e.: the
     * preparation of my coffee this morning included a burning of my fingers).
     */
    public static final Property includesAction = DUL.m_model.createProperty(DUL.NS + "includesAction");
    /**
     * is expanded in: A partial order relation that holds between descriptions.
     * It represents the proper part relation between a description and another
     * description featuring the same properties as the former, with at least
     * one
     * additional one.Descriptions can be expanded either by adding other
     * descriptions as parts, or by refining concepts that are used by them. An
     * 'intention' to expand must be present (unless purely formal theories are
     * considered, but even in this case a criterion of relevance is usually
     * active).
     */
    public static final Property isExpandedIn = DUL.m_model.createProperty(DUL.NS + "isExpandedIn");
    /**
     * parametrizza: The relation between a Parameter, e.g. 'MajorAgeLimit', and
     * a Region, e.g. '18_year'.For a more data-oriented relation, see
     * hasDataValue
     */
    public static final Property parametrizes = DUL.m_model.createProperty(DUL.NS + "parametrizes");
    /**
     * è un task definito in: A relation between a description and a task, e.g.
     * the task 'boil' is defined in a recipe for a cake.
     */
    public static final Property isTaskDefinedIn = DUL.m_model.createProperty(DUL.NS + "isTaskDefinedIn");
    /**
     * has component: The hasPart relation without transitivity, holding between
     * an Object (the system) and another (the component), and assuming a Design
     * that structures the Object.
     */
    public static final Property hasComponent = DUL.m_model.createProperty(DUL.NS + "hasComponent");
    /**
     * is component of: The hasPart relation without transitivity, holding
     * between an Object (the system) and another (the component), and assuming
     * a Design that structures the Object.
     */
    public static final Property isComponentOf = DUL.m_model.createProperty(DUL.NS + "isComponentOf");
    /** è caratterizzato da. */
    public static final Property isCharacterizedBy = DUL.m_model.createProperty(DUL.NS + "isCharacterizedBy");
    /**
     * realizes information about: The relation between entities and information
     * realizations, e.g. between Italy and a paper copy of the text of the
     * Italian Constitution.
     */
    public static final Property realizesInformationAbout = DUL.m_model.createProperty(DUL.NS + "realizesInformationAbout");
    /**
     * acts through: The relation holding between a PhysicalAgent and a
     * SocialAgent. In principle, a SocialAgent requires at least one
     * PhysicalAgent in order to act, but this dependency can be 'delegated',
     * e.g. a university can be acted for by a department, which is acted for by
     * physical agents. AKA isActedBy
     */
    public static final Property actsThrough = DUL.m_model.createProperty(DUL.NS + "actsThrough");
    /**
     * ricopre: A relation between concepts and collections, where a Concept is
     * said to cover a Collection; it corresponds to a link between the
     * (reified) intensional and extensional interpretations of a (reified)
     * class.E.g. the collection of vintage saxophones is covered by the Concept
     * 'Saxophone' with the Parameter 'Vintage'.
     */
    public static final Property covers = DUL.m_model.createProperty(DUL.NS + "covers");
    /**
     * characterizes: A relation between concepts and collections, where a
     * Concept is said to characterize a Collection; it corresponds to a link
     * between the (reified) intensional and extensional interpretations of a
     * _proper subset of_ a (reified) class. This is different from covers,
     * because it refers to an interpretation the entire reified class.E.g. the
     * collection of vintage saxophones is characterized by the Concept
     * 'manufactured by hand', while it gets covered by the Concept 'Saxophone'
     * with the Parameter 'Vintage'.
     */
    public static final Property characterizes = DUL.m_model.createProperty(DUL.NS + "characterizes");
    /**
     * is conceptualized by: A relation stating that an Agent is internally
     * representing a Description . E.g., 'John believes in the conspiracy
     * theory'; 'Niels Bohr created a solar-system metaphor for his atomic
     * theory'; 'Jacques assumes all swans are white'; 'the task force shares
     * the
     * attack plan'.
     */
    public static final Property isConceptualizedBy = DUL.m_model.createProperty(DUL.NS + "isConceptualizedBy");
    /**
     * is satisfied by: A relation between a Situation and a Description, e.g.
     * the execution of a Plan satisfies that plan.
     */
    public static final Property isSatisfiedBy = DUL.m_model.createProperty(DUL.NS + "isSatisfiedBy");
    /**
     * is reference of: A relation between information objects and any Entity
     * (including information objects). It can be used to talk about e.g.
     * entities are references of proper nouns: the proper noun 'Leonardo da
     * Vinci' isAbout the Person Leonardo da Vinci; as well as to talk about
     * sets
     * of entities that can be described by a common noun: the common noun
     * 'person' isAbout the set of all persons in a domain of discourse, which
     * can be represented in DOLCE-Ultralite as an individual of the class:
     * Collection .The isReferenceOf relation is irreflexive, differently from
     * its inverse isAbout.
     */
    public static final Property isReferenceOf = DUL.m_model.createProperty(DUL.NS + "isReferenceOf");
    /**
     * is specialized by: A partial order relation that holds between social
     * objects. It represents the subsumption relation between e.g. a Concept
     * and another Concept that is broader in extensional interpretation, but
     * narrowe in intensional interpretation.E.g. PhDStudent Role specializes
     * Student Role
     */
    public static final Property isSpecializedBy = DUL.m_model.createProperty(DUL.NS + "isSpecializedBy");
    /**
     * conceptualizes: A relation stating that an Agent is internally
     * representing a SocialObject: situations, descriptions, concepts, etc.
     * E.g., 'John believes in the conspiracy theory'; 'Niels Bohr created the
     * solar-system metaphor for the atomic theory'; 'Jacques assumes all swans
     * are white'; 'the task force members share the attack
     * plan'.Conceptualizations can be distinguished into different forms,
     * primarily based on the type of SocialObject that is conceptualized.
     * Descriptions and concepts can be 'assumed', situations can be 'believed'
     * or 'known', plans can be 'adopted', etc. (see ontology:
     * http://www.loa-cnr.it/ontologies/Conceptualization.owl.
     */
    public static final Property conceptualizes = DUL.m_model.createProperty(DUL.NS + "conceptualizes");
    /**
     * acts for: The relation holding between any Agent, and a SocialAgent. In
     * principle, a SocialAgent requires at least one PhysicalAgent in order to
     * act, but this dependency can be 'delegated'; e.g. a university can be
     * acted for by a department, which on its turm is acted for by physical
     * agents.
     */
    public static final Property actsFor = DUL.m_model.createProperty(DUL.NS + "actsFor");
    /**
     * overlaps: A schematic relation between any entities, e.g. 'the chest
     * region overlaps with the abdomen region', 'my spoken words overlap with
     * hers', 'the time of my leave overlaps with the time of your arrival',
     * 'fibromyalgia overlaps with other conditions'.Subproperties and
     * restrictions can be used to specialize overlaps for objects, events, time
     * intervals, etc.
     */
    public static final Property overlaps = DUL.m_model.createProperty(DUL.NS + "overlaps");
    /**
     * co-participates with: A relation between two objects participating in a
     * same Event; e.g., 'Vitas and Jimmy are playing tennis'.
     */
    public static final Property coparticipatesWith = DUL.m_model.createProperty(DUL.NS + "coparticipatesWith");
    /**
     * specializes: A partial order relation that holds between social objects.
     * It mainly represents the subsumption relation between e.g. a Concept or
     * Description and another Concept (resp. Description) that is broader in
     * extensional interpretation, but narrower in intensional interpretation.
     * For example, the role PhDStudent specializes the role Student.Another
     * possible use is between a Collection that isCoveredBy a Concept A, and
     * another Collection that isCoveredBy a Concept B that on its turm
     * specializes A. For example, the 70,000 series Selmer Mark VI saxophone
     * Collection specializes the Selmer Mark VI saxophone Collection.
     */
    public static final Property specializes = DUL.m_model.createProperty(DUL.NS + "specializes");
    /**
     * is introduced by: A relation between a Description and a SocialAgent,
     * e.g. a Constitutional Charter introduces the SocialAgent
     * 'PresidentOfRepublic'.
     */
    public static final Property isIntroducedBy = DUL.m_model.createProperty(DUL.NS + "isIntroducedBy");
    /**
     * has member: A relation between collections and entities, e.g. 'my
     * collection of saxophones includes an old Adolphe Sax original alto' (i.e.
     * my collection has member an Adolphe Sax alto).
     */
    public static final Property hasMember = DUL.m_model.createProperty(DUL.NS + "hasMember");
    /**
     * realizes: A relation between an information realization and an
     * information object, e.g. the paper copy of the Italian Constitution
     * realizes the text of the Constitution.
     */
    public static final Property realizes = DUL.m_model.createProperty(DUL.NS + "realizes");
    /**
     * è ricoperto da: A relation between concepts and collections, where a
     * Concept is said to cover a Collection; it corresponds to a link between
     * the (reified) intensional and extensional interpretations of a (reified)
     * class.E.g. the collection of vintage saxophones is covered by the Concept
     * 'Saxophone' with the Parameter 'Vintage'.
     */
    public static final Property isCoveredBy = DUL.m_model.createProperty(DUL.NS + "isCoveredBy");
    /**
     * is constituent of: 'Constituency' depends on some layering of the world
     * described by the ontology. For example, scientific granularities (e.g.
     * body-organ-tissue-cell) or ontological 'strata' (e.g.
     * social-mental-biological-physical) are typical layerings. Intuitively, a
     * constituent is a part belonging to a lower layer. Since layering is
     * actually a partition of the world described by the ontology, constituents
     * are not properly classified as parts, although this kinship can be
     * intuitive for common sense.A desirable advantage of this distinction is
     * that we are able to talk e.g. of physical constituents of non-physical
     * objects (e.g. systems), while this is not possible in terms of
     * parts.Example of are the persons constituting a social system, the
     * molecules constituting a person, the atoms constituting a river, etc. In
     * all these examples, we notice a typical discontinuity between the
     * constituted and the constituent object: e.g. a social system is
     * conceptualized at a different layer from the persons that constitute it,
     * a
     * person is conceptualized at a different layer from the molecules that
     * constitute them, and a river is conceptualized at a different layer from
     * the atoms that constitute it.
     */
    public static final Property isConstituentOf = DUL.m_model.createProperty(DUL.NS + "isConstituentOf");
    /**
     * has common boundary: A relation to encode either formal or informal
     * characterizations of 'boundaries' common to two different entities: an
     * Event that ends when another begins, two abstract regions that have a
     * common topological boundary, two objects that are said to be 'in contact'
     * from a commonsense perspective, etc.
     */
    public static final Property hasCommonBoundary = DUL.m_model.createProperty(DUL.NS + "hasCommonBoundary");
    /**
     * near to: Generic distance relation between any Entity(s). E.g. Rome is
     * near to Florence, astronomy is near to physics.
     */
    public static final Property nearTo = DUL.m_model.createProperty(DUL.NS + "nearTo");
    /**
     * unifies: A Collection has a unification criterion, provided by a
     * Description; for example, a community of practice can be unified by a
     * shared theory or interest, e.g. the community that makes research on
     * mirror neurons shares some core knowledge about mirror neurons, which can
     * be represented as a Description MirrorNeuronTheory that unifies the
     * community. There can be several unifying descriptions.
     */
    public static final Property unifies = DUL.m_model.createProperty(DUL.NS + "unifies");
    /**
     * concretely expresses: A relation between an InformationRealization and a
     * Description, e.g. 'the printout of the Italian Constitution
     * concretelyExpresses the Italian Constitution'. It should be supplied also
     * with a rule stating that the InformationRealization realizes an
     * InformationObject that expresses the Description
     */
    public static final Property concretelyExpresses = DUL.m_model.createProperty(DUL.NS + "concretelyExpresses");
    /**
     * is member of: A relation between collections and entities, e.g. 'the
     * Night Watch by Rembrandt is in the Rijksmuseum collection'; 'Davide is
     * member of the Pen Club', 'Igor is one the subjects chosen for the
     * experiment'.
     */
    public static final Property isMemberOf = DUL.m_model.createProperty(DUL.NS + "isMemberOf");
    /**
     * is reference of information realized by: The relation between entities
     * and information realizations, e.g. between Italy and a paper copy of the
     * text of the Italian Constitution.
     */
    public static final Property isReferenceOfInformationRealizedBy = DUL.m_model.createProperty(DUL.NS + "isReferenceOfInformationRealizedBy");
    /**
     * is about: A relation between information objects and any Entity
     * (including information objects). It can be used to talk about e.g.
     * entities are references of proper nouns: the proper noun 'Leonardo da
     * Vinci' isAbout the Person Leonardo da Vinci; as well as to talk about
     * sets
     * of entities that can be described by a common noun: the common noun
     * 'person' isAbout the set of all persons in a domain of discourse, which
     * can be represented in DOLCE-Ultralite as an individual of the class:
     * Collection .The isAbout relation is reflexive (not expressible in
     * OWL1.0),
     * because information objects are also about themselves.
     */
    public static final Property isAbout = DUL.m_model.createProperty(DUL.NS + "isAbout");
    /**
     * introduces: A relation between a Description and a SocialAgent, e.g. a
     * Constitutional Charter introduces the SocialAgent 'PresidentOfRepublic'.
     */
    public static final Property introduces = DUL.m_model.createProperty(DUL.NS + "introduces");
    /**
     * is unified by: A Collection has a unification criterion, provided by a
     * Description; for example, a community of practice can be unified by a
     * shared theory or interest, e.g. the community that makes research on
     * mirror neurons shares some core knowledge about mirror neurons, which can
     * be represented as a Description MirrorNeuronTheory that unifies the
     * community. There can be several unifying descriptions.
     */
    public static final Property isUnifiedBy = DUL.m_model.createProperty(DUL.NS + "isUnifiedBy");
    /**
     * has constituent: 'Constituency' depends on some layering of the world
     * described by the ontology. For example, scientific granularities (e.g.
     * body-organ-tissue-cell) or ontological 'strata' (e.g.
     * social-mental-biological-physical) are typical layerings. Intuitively, a
     * constituent is a part belonging to a lower layer. Since layering is
     * actually a partition of the world described by the ontology, constituents
     * are not properly classified as parts, although this kinship can be
     * intuitive for common sense.A desirable advantage of this distinction is
     * that we are able to talk e.g. of physical constituents of non-physical
     * objects (e.g. systems), while this is not possible in terms of
     * parts.Example of are the persons constituting a social system, the
     * molecules constituting a person, the atoms constituting a river, etc. In
     * all these examples, we notice a typical discontinuity between the
     * constituted and the constituent object: e.g. a social system is
     * conceptualized at a different layer from the persons that constitute it,
     * a
     * person is conceptualized at a different layer from the molecules that
     * constitute them, and a river is conceptualized at a different layer from
     * the atoms that constitute it.
     */
    public static final Property hasConstituent = DUL.m_model.createProperty(DUL.NS + "hasConstituent");
    /**
     * is concretely expressed by: A relation between an InformationRealization
     * and a Description, e.g. 'the printout of the Italian Constitution
     * concretelyExpresses the Italian Constitution'. It should be supplied also
     * with a rule stating that the InformationRealization realizes an
     * InformationObject that expresses the Description
     */
    public static final Property isConcretelyExpressedBy = DUL.m_model.createProperty(DUL.NS + "isConcretelyExpressedBy");
    /** has proxy. */
    public static final Property hasProxy = DUL.m_model.createProperty(DUL.NS + "hasProxy");
    /**
     * far from: Generic distance relation between any Entity(s). E.g. Rome is
     * far from Beijing, astronomy is far from necromancy.
     */
    public static final Property farFrom = DUL.m_model.createProperty(DUL.NS + "farFrom");
    /**
     * is realized by: A relation between an information realization and an
     * information object, e.g. the paper copy of the Italian Constitution
     * realizes the text of the Constitution.
     */
    public static final Property isRealizedBy = DUL.m_model.createProperty(DUL.NS + "isRealizedBy");
    /** mappable to. */
    public static final Property mappableTo = DUL.m_model.createProperty(DUL.NS + "mappableTo");

    // Vocabulary classes
    // /////////////////////////
    /**
     * Region: Any region in a dimensional space (a dimensional space is a
     * maximal Region), which can be used as a value for a quality of an Entity
     * . For example, TimeInterval, SpaceRegion, PhysicalAttribute, Amount,
     * SocialAttribute are all subclasses of Region. Regions are not data values
     * in the ordinary knowledge representation sense; in order to get patterns
     * for modelling data, see the properties: representsDataValue and
     * hasDataValue
     */
    public static final Resource Region = DUL.m_model.createResource(DUL.NS + "Region");
    /**
     * Situazione: A view, consistent with ('satisfying') a Description, on a
     * set of entities. It can also be seen as a 'relational context' created by
     * an observer on the basis of a 'frame' (i.e. a Description). For example,
     * a
     * PlanExecution is a context including some actions executed by agents
     * according to certain parameters and expected tasks to be achieved from a
     * Plan; a DiagnosedSituation is a context of observed entities that is
     * interpreted on the basis of a Diagnosis, etc.Situation is also able to
     * represent reified n-ary relations, where isSettingFor is the top-level
     * relation for all binary projections of the n-ary relation. If used in a
     * transformation pattern for n-ary relations, the designer should take care
     * of creating only one subclass of Situation for each n-ary relation,
     * otherwise the 'identification constraint' (Calvanese et al., IJCAI 2001)
     * could be violated.
     */
    public static final Resource Situation = DUL.m_model.createResource(DUL.NS + "Situation");
    /**
     * Physical object: Any Object that has a proper space region. The
     * prototypical physical object has also an associated mass, but the nature
     * of its mass can greatly vary based on the epistemological status of the
     * object (scientifically measured, subjectively possible, imaginary).
     */
    public static final Resource PhysicalObject = DUL.m_model.createResource(DUL.NS + "PhysicalObject");
    /**
     * Quality: Any aspect of an Entity (but not a part of it), which cannot
     * exist without that Entity. For example, the way the surface of a specific
     * PhysicalObject looks like is a Quality, while the encoding of that
     * Quality
     * into e.g. a PhysicalAttribute should be modeled as a Region. From the
     * design viewpoint, the Quality-Region distinction is useful only when
     * individual aspects of an Entity are considered in a domain of discourse.
     * For example, in an automotive context, it would be irrelevant to consider
     * the aspects of car windows for a specific car, unless the factory wants
     * to
     * check a specific window against design parameters (anomaly detection). On
     * the other hand, in an antiques context, the individual aspects for a
     * specific piece of furniture are a major focus of attention, and may
     * constitute the actual added value, because the design parameters for old
     * furniture are often not fixed, and may not be viewed as 'anomalies'.
     */
    public static final Resource Quality = DUL.m_model.createResource(DUL.NS + "Quality");
    /**
     * Oggetto informativo: A piece of information, such as a musical
     * composition, a text, a word, a picture, independently from how it is
     * concretely realized.
     */
    public static final Resource InformationObject = DUL.m_model.createResource(DUL.NS + "InformationObject");
    /**
     * Evento: Any physical, social, or mental process, event, or state.More
     * theoretically, events can be classified in different ways, possibly based
     * on 'aspect' (e.g. stative, continuous, accomplishement, achievement,
     * etc.), on 'agentivity' (e.g. intentional, natural, etc.), or on 'typical
     * participants' (e.g. human, physical, abstract, food, etc.).Here no
     * special
     * direction is taken, and the following explains why: events are related to
     * observable situations, and they can have different views at a same
     * time.If
     * a position has to be suggested here anyway, the participant-based
     * classification of events seems the most stable and appropriate for many
     * modelling problems.(1) Alternative aspectual viewsConsider a same event
     * 'rock erosion in the Sinni valley': it can be conceptualized as an
     * accomplishment (what has brought a certain state to occur), as an
     * achievement (the state resulting from a previous accomplishment), as a
     * punctual event (if we collapse the time interval of the erosion into a
     * time point), or as a transition (something that has changed a state to a
     * different one). In the erosion case, we could therefore have good
     * motivations to shift from one aspect to another: a) causation focus, b)
     * effectual focus, c) historical condensation d) transition (causality).The
     * different views refer to the same event, but are still different: how to
     * live with this seeming paradox? A typical solution e.g. in linguistics
     * (cf. Levin's aspectual classes) and in DOLCE Full (cf. WonderWeb D18
     * axiomatization) is to classify events based on aspectual differences. But
     * this solution would create different identities for a same event, where
     * the difference is only based on the modeller's attitude.An alternative
     * solution is applied here, and exploits the notion of (observable)
     * Situation; a Situation is a view, consistent with a Description, which
     * can
     * be observed of a set of entities. It can also be seen as a 'relational
     * context' created by an observer on the basis of a 'frame'. Therefore, a
     * Situation allows to create a context where each particular view can have
     * a
     * proper identity, while the Event preserves its own identity. For example,
     * ErosionAsAccomplishment is a Situation where rock erosion is observed as
     * a
     * process leading to a certain achievement: the conditions (roles,
     * parameters) that suggest such view are stated in a Description, which
     * acts
     * as a 'theory of accomplishments'. Similarly, ErosionAsTransition is a
     * Situation where rock erosion is observed as an event that has changed a
     * state to another: the conditions for such interpretation are stated in a
     * different Description, which acts as a 'theory of state
     * transitions'.Consider that in no case the Event is changed or enriched in
     * parts by the aspectual view.(2) Alternative intentionality viewsSimilarly
     * to aspectual views, several intentionality views can be provided for a
     * same Event. For example, one can investigate if an avalanche has been
     * caused by immediate natural forces, or if there is any hint of an
     * intentional effort to activate those natural forces.Also in this case,
     * the
     * Event as such has not different identities, while the causal analysis
     * generates situations with different identities, according to what
     * Description is taken for interpreting the Event. On the other hand, if
     * the
     * possible actions of an Agent causing the starting of an avalanche are
     * taken as parts of the Event, then this makes its identity change, because
     * we are adding a part to it. Therefore, if intentionality is a criterion
     * to
     * classify events or not depends on if an ontology designer wants to
     * consider causality as a relevant dimension for events' identity.(3)
     * Alternative participant viewsA slightly different case is when we
     * consider
     * the basic participants to an Event. In this case, the identity of the
     * Event is affected by the participating objects, because it depends on
     * them. For example, if snow, mountain slopes, wind, waves, etc. are
     * considered as an avalanche basic participants, or if we also want to add
     * water, human agents, etc., makes the identity of an avalanche
     * change.Anyway, this approach to event classification is based on the
     * designer's choices, and more accurately mirrors lexical or commonsense
     * classifications (see. e.g. WordNet 'supersenses' for verb
     * synsets).Ultimately, this discussion has no end, because realists will
     * keep defending the idea that events in reality are not changed by the way
     * we describe them, while constructivists will keep defending the idea
     * that,
     * whatever 'true reality' is about, it can't be modelled without the
     * theoretical burden of how we observe and describe it. Both positions are
     * in principle valid, but, if taken too radically, they focus on issues
     * that
     * are only partly relevant to the aim of computational ontologies, which
     * only attempt to assist domain experts in representing what they want to
     * conceptualize about a certain portion of reality according to their own
     * ideas. For this reason, in this ontology both events and situations are
     * allowed, together with descriptions, in order to encode the modelling
     * needs independently from the position (if any) chosen by the designer.
     */
    public static final Resource Event = DUL.m_model.createResource(DUL.NS + "Event");
    /**
     * Method: A method is a Description that defines or uses concepts in order
     * to guide carrying out actions aimed at a solution with respect to a
     * problem. It is different from a Plan, because plans could be carried out
     * in order to follow a method, but a method can be followed by executing
     * alternative plans.
     */
    public static final Resource Method = DUL.m_model.createResource(DUL.NS + "Method");
    /** Object: Any physical, social, or mental object, or a substance */
    public static final Resource Object = DUL.m_model.createResource(DUL.NS + "Object");
    /**
     * Designed artifact: A PhysicalArtifact that is also described by a Design.
     * This excludes simple recycling or refunctionalization of natural objects.
     * Most common sense 'artifacts' can be included in this class: cars, lamps,
     * houses, chips, etc.
     */
    public static final Resource DesignedArtifact = DUL.m_model.createResource(DUL.NS + "DesignedArtifact");
    /**
     * Process: This is a placeholder for events that are considered in their
     * evolution, or anyway not strictly dependent on agents, tasks, and plans.
     * See Event class for some thoughts on classifying events. See also
     * 'Transition'.
     */
    public static final Resource Process = DUL.m_model.createResource(DUL.NS + "Process");
    /** Functional substance. */
    public static final Resource FunctionalSubstance = DUL.m_model.createResource(DUL.NS + "FunctionalSubstance");
    /**
     * Physical attribute: Physical value of a physical object, e.g. density,
     * color, etc.
     */
    public static final Resource PhysicalAttribute = DUL.m_model.createResource(DUL.NS + "PhysicalAttribute");
    /**
     * Unit of measure: Units of measure are conceptualized here as parameters
     * on regions, which can be valued as datatype values.
     */
    public static final Resource UnitOfMeasure = DUL.m_model.createResource(DUL.NS + "UnitOfMeasure");
    /**
     * Organization: An internally structured, conventionally created
     * SocialAgent, needing a specific Role and Agent that plays it, in order to
     * act.
     */
    public static final Resource Organization = DUL.m_model.createResource(DUL.NS + "Organization");
    /**
     * Collezione: Any container for entities that share one or more common
     * properties. E.g. "stone objects", "the nurses",
     * "the Louvre Aegyptian collection", all the elections for the Italian
     * President of the Republic. A collection is not a logical class: a
     * collection is a first-order entity, while a class is second-order.
     */
    public static final Resource Collection = DUL.m_model.createResource(DUL.NS + "Collection");
    /**
     * Informazione concreta: A concrete realization of an InformationObject,
     * e.g. the written document containing the text of a law.
     */
    public static final Resource InformationRealization = DUL.m_model.createResource(DUL.NS + "InformationRealization");
    /**
     * Person: Persons in commonsense intuition, which does not apparently
     * distinguish between either natural or social persons.
     */
    public static final Resource Person = DUL.m_model.createResource(DUL.NS + "Person");
    /**
     * Entity: Anything: real, possible, or imaginary, which some modeller wants
     * to talk about for some purpose.
     */
    public static final Resource Entity = DUL.m_model.createResource(DUL.NS + "Entity");
    /**
     * Place: A location, in a very generic sense: a political geographic entity
     * (Roma, Lesotho), a non-material location determined by the presence of
     * other entities ("the area close to Roma"), pivot events or signs
     * ("the area where the helicopter fell"), complements of other entities
     * ("the area under the table"), etc. In this generic sense, a Place is an
     * "approximate" location. For an "absolute" location, see the class
     * SpaceRegion
     */
    public static final Resource Place = DUL.m_model.createResource(DUL.NS + "Place");
    /**
     * Personification: A social entity with agentive features, but whose status
     * is the result of a cultural transformation from e.g. a PhysicalObject, an
     * Event, an Abstract, another SocialObject, etc. For example: the holy
     * grail, deus ex machina, gods, magic wands, etc.
     */
    public static final Resource Personification = DUL.m_model.createResource(DUL.NS + "Personification");
    /** Chemical object. */
    public static final Resource ChemicalObject = DUL.m_model.createResource(DUL.NS + "ChemicalObject");
    /**
     * Parameter: A Concept that classifies a Region; the difference between a
     * Region and a Parameter is that regions represent sets of observable
     * values, e.g. the height of a given building, while parameters represent
     * constraints or selections on observable values, e.g. 'VeryHigh'.
     * Therefore, parameters can also be used to constrain regions, e.g.
     * VeryHigh
     * on a subset of values of the Region Height applied to buildings, or to
     * add
     * an external selection criterion , such as measurement units, to regions,
     * e.g. Meter on a subset of values from the Region Length applied to the
     * Region Length applied to roads.
     */
    public static final Resource Parameter = DUL.m_model.createResource(DUL.NS + "Parameter");
    /**
     * Agente fisico: A PhysicalObject that is capable of self-representing
     * (conceptualizing) a Description in order to plan an Action. A
     * PhysicalAgent is a substrate for (actsFor) a Social Agent
     */
    public static final Resource PhysicalAgent = DUL.m_model.createResource(DUL.NS + "PhysicalAgent");
    /**
     * Abstract: Any Entity that cannot be located in space-time. E.g.
     * mathematical entities: formal semantics elements, regions within
     * dimensional spaces, etc.
     */
    public static final Resource Abstract = DUL.m_model.createResource(DUL.NS + "Abstract");
    /** null. */
    public static final Resource DesignedSubstance = DUL.m_model.createResource(DUL.NS + "DesignedSubstance");
    /**
     * Configuration: A collection whose members are 'unified', i.e. organized
     * according to a certain schema that can be represented by a
     * Description.Typically, a configuration is the collection that emerges out
     * of a composed entity: an industrial artifact, a plan, a discourse, etc.
     * E.g. a physical book has a configuration provided by the part-whole
     * schema
     * that holds together its cover, pages, ink. That schema, based on the
     * individual relations between the book and its parts, can be represented
     * in
     * a reified way by means of a (structural) description, which is said to
     * 'unify' the book configuration.
     */
    public static final Resource Configuration = DUL.m_model.createResource(DUL.NS + "Configuration");
    /**
     * Concept: A Concept is a SocialObject, and isDefinedIn some Description;
     * once defined, a Concept can be used in other Description(s). If a Concept
     * isDefinedIn exactly one Description, see the LocalConcept class.The
     * classifies relation relates Concept(s) to Entity(s) at some TimeInterval
     */
    public static final Resource Concept = DUL.m_model.createResource(DUL.NS + "Concept");
    /**
     * Agente: Additional comment: a computational agent can be considered as a
     * PhysicalAgent that realizes a certain class of algorithms (that can be
     * considered as instances of InformationObject) that allow to obtain some
     * behaviors that are considered typical of agents in general. For an
     * ontology of computational objects based on DOLCE see e.g.
     * http://www.loa-cnr.it/COS/COS.owl, and http://www.loa-cnr.it/KCO/KCO.owl.
     */
    public static final Resource Agent = DUL.m_model.createResource(DUL.NS + "Agent");
    /**
     * Social person: A SocialAgent that needs the existence of a specific
     * NaturalPerson in order to act (but the lifetime of the NaturalPerson has
     * only to overlap that of the SocialPerson).
     */
    public static final Resource SocialPerson = DUL.m_model.createResource(DUL.NS + "SocialPerson");
    /**
     * Collettivo: A Collection whose members are agents, e.g. "the nurses",
     * "the Italian rockabilly fans".Collectives, facon de parler, can act as
     * agents, although they are not assumed here to be agents (they are even
     * disjoint from the class SocialAgent). This is represented by admitting
     * collectives in the range of the relations having Agent in their domain or
     * range.
     */
    public static final Resource Collective = DUL.m_model.createResource(DUL.NS + "Collective");
    /**
     * Sostanza: Any PhysicalBody that has not necessarily specified (designed)
     * boundaries, e.g. a pile of trash, some sand, etc. In this sense, an
     * artistic object made of trash or a dose of medicine in the form of a pill
     * would be a FunctionalSubstance, and a DesignedArtifact, since its
     * boundaries are specified by a Design; aleatoric objects that are outcomes
     * of an artistic process might be still considered DesignedArtifact(s), and
     * Substance(s).
     */
    public static final Resource Substance = DUL.m_model.createResource(DUL.NS + "Substance");
    /**
     * Physical artifact: Any PhysicalObject that isDescribedBy a Plan .This
     * axiomatization is weak, but allows to talk of artifacts in a very general
     * sense, i.e. including recycled objects, objects with an intentional
     * functional change, natural objects that are given a certain function,
     * even
     * though they are not modified or structurally designed, etc.
     * PhysicalArtifact(s) are not considered disjoint from PhysicalBody(s), in
     * order to allow a dual classification when needed.
     * E.g.,FunctionalSubstance(s) are included here as well.Immaterial
     * (non-physical) artifacts (e.g. texts, ideas, cultural movements,
     * corporations, communities, etc. can be modelled as social objects (see
     * SocialObject), which are all 'artifactual' in the weak sense assumed
     * here.
     */
    public static final Resource PhysicalArtifact = DUL.m_model.createResource(DUL.NS + "PhysicalArtifact");
    /**
     * Type collection: A Collection whose members are the maximal set of
     * individuals that share the same (named) type, e.g. "the gem stones",
     * "the Italians".This class is very useful to apply a variety of the
     * so-called "ClassesAsValues" design pattern, when it is used to talk about
     * the extensional aspect of a class. An alternative variety of the pattern
     * applies to the intensional aspect of a class, and the class Concept
     * should
     * be used instead.
     */
    public static final Resource TypeCollection = DUL.m_model.createResource(DUL.NS + "TypeCollection");
    /**
     * Time interval: Any Region in a dimensional space that aims at
     * representing time.
     */
    public static final Resource TimeInterval = DUL.m_model.createResource(DUL.NS + "TimeInterval");
    /** Set. */
    public static final Resource Set = DUL.m_model.createResource(DUL.NS + "Set");
    /**
     * Diagnosis: A Description of the Situation of a system, usually applied in
     * order to control a normal behaviour, or to explain a notable behavior
     * (e.g. a functional breakdown).
     */
    public static final Resource Diagnosis = DUL.m_model.createResource(DUL.NS + "Diagnosis");
    /** Narrative. */
    public static final Resource Narrative = DUL.m_model.createResource(DUL.NS + "Narrative");
    /**
     * Design: A Description of the Situation, in terms of structure and
     * function, held by an Entity for some reason.A design is usually
     * accompanied by the rationales behind the construction of the designed
     * Entity (i.e. of the reasons why a design is claimed to be as such). For
     * example, the actual design (a Situation) of a car or of a law is based on
     * both the specification (a Description) of the structure and the
     * rationales
     * used to construct cars or a specific law.While designs typically describe
     * entities to be constructed, they can also be used to describe
     * 'refunctionalized' entities, or to hypothesize unknown functions.
     */
    public static final Resource Design = DUL.m_model.createResource(DUL.NS + "Design");
    /**
     * Workflow: A Plan that defines Role(s), Task(s), and a specific structure
     * for tasks to be executed, usually supporting the work of an Organization
     */
    public static final Resource Workflow = DUL.m_model.createResource(DUL.NS + "Workflow");
    /** Community. */
    public static final Resource Community = DUL.m_model.createResource(DUL.NS + "Community");
    /**
     * Theory: A Theory is a Description that represents a set of assumptions
     * for describing something, usually general. Scientific, philosophical, and
     * commonsense theories can be included here.This class can also be used to
     * act as 'naturalized reifications' of logical theories (of course, they
     * will be necessarily incomplete in this case, because second-order
     * entities
     * are represented as first-order ones).
     */
    public static final Resource Theory = DUL.m_model.createResource(DUL.NS + "Theory");
    /**
     * Organism: A physical objects with biological characteristics, typically
     * that organisms can self-reproduce.
     */
    public static final Resource Organism = DUL.m_model.createResource(DUL.NS + "Organism");
    /** null: A piece of information, be it concretely realized or not. */
    public static final Resource InformationEntity = DUL.m_model.createResource(DUL.NS + "InformationEntity");
    /**
     * Social object: Any Object that exists only within some communication
     * Event, in which at least one PhysicalObject participates in. In other
     * words, all objects that have been or are created in the process of social
     * communication: for the sake of communication (InformationObject), for
     * incorporating new individuals (SocialAgent, Place), for contextualizing
     * existing entities (Situation), for collecting existing entities
     * (Collection), or for describing existing entities (Description,
     * Concept).Being dependent on communication, all social objects need to be
     * expressed by some information object (information object are
     * self-expressing).
     */
    public static final Resource SocialObject = DUL.m_model.createResource(DUL.NS + "SocialObject");
    /**
     * Physical body: Physical bodies are PhysicalObject(s), for which we tend
     * to neutralize any possible artifactual character. They can have several
     * granularity levels: geological, chemical, physical, biological, etc.
     */
    public static final Resource PhysicalBody = DUL.m_model.createResource(DUL.NS + "PhysicalBody");
    /**
     * Luogo fisico: A physical object that is inherently located; for example,
     * a water area.
     */
    public static final Resource PhysicalPlace = DUL.m_model.createResource(DUL.NS + "PhysicalPlace");
    /**
     * Transition: A transition is a Situation that creates a context for three
     * TimeInterval(s), two additional different Situation(s), one Event, one
     * Process, and at least one Object: the Event is observed as the cause for
     * the transition, one Situation is the state before the transition, the
     * second Situation is the state after the transition, the Process is the
     * invariance under some different transitions (including the one
     * represented
     * here), in which at least one Object is situated. Finally, the time
     * intervals position the situations and the transitional event in time.This
     * class of situations partly encodes the ontology underlying typical
     * engineering algebras for processes, e.g. Petri Nets. A full
     * representation
     * of the transition ontology is outside the expressivity of OWL, because we
     * would need qualified cardinality restrictions, coreference, property
     * equivalence, and property composition.
     */
    public static final Resource Transition = DUL.m_model.createResource(DUL.NS + "Transition");
    /**
     * Local concept: A Concept that isDefinedIn exactly 1 Description. For
     * example, the Concept 'coffee' in a 'preparesCoffee' relation can be
     * defined in that relation, and for all other Description(s) that use it,
     * the isConceptUsedIn property should be applied. Notice therefore that not
     * necessarily all Concept(s) isDefinedIn exactly 1 Description.
     */
    public static final Resource LocalConcept = DUL.m_model.createResource(DUL.NS + "LocalConcept");
    /**
     * Natural person: A person in the physical commonsense intuition: 'have you
     * seen that person walking down the street?'
     */
    public static final Resource NaturalPerson = DUL.m_model.createResource(DUL.NS + "NaturalPerson");
    /** Workflow execution. */
    public static final Resource WorkflowExecution = DUL.m_model.createResource(DUL.NS + "WorkflowExecution");
    /**
     * Caratteristica sociale: Any Region in a dimensional space that is used to
     * represent some characteristic of a SocialObject, e.g. judgment values,
     * social scalars, statistical attributes over a collection of entities,
     * etc.
     */
    public static final Resource SocialObjectAttribute = DUL.m_model.createResource(DUL.NS + "SocialObjectAttribute");
    /** Ruolo: A Concept that classifies an Object */
    public static final Resource Role = DUL.m_model.createResource(DUL.NS + "Role");
    /**
     * Formal entity: Entities that are formally defined and are considered
     * independent from the social context in which they are used. They cannot
     * be localized in space or time. Also called 'Platonic
     * entities'.Mathematical and logical entities are included in this class:
     * sets, categories, tuples, costants, variables, etc.Abstract formal
     * entities are distinguished from information objects, which are supposed
     * to
     * be part of a social context, and are localized in space and time,
     * therefore being (social) objects.For example, the class 'Quark' is an
     * abstract formal entity from the purely set-theoretical perspective, but
     * it
     * is an InformationObject from the viewpoint of ontology design, when e.g.
     * implemented in a logical language like OWL.Abstract formal entities are
     * also distinguished from Concept(s), Collection(s), and Description(s),
     * which are part of a social context, therefore being SocialObject(s) as
     * well.For example, the class 'Quark' is an abstract FormalEntity from the
     * purely set-theoretical perspective, but it is a Concept within history of
     * science and cultural dynamics.These distinctions allow to represent two
     * different notions of 'semantics': the first one is abstract and formal
     * ('formal semantics'), and formallyInterprets symbols that are about
     * entities whatsoever; for example, the term 'Quark' isAbout the Collection
     * of all quarks, and that Collection isFormalGroundingFor the abstract
     * class
     * 'Quark' (in the extensional sense). The second notion is social,
     * localized
     * in space-time ('social semantics'), and can be used to interpret entities
     * in the intensional sense. For example, the Collection of all quarks
     * isCoveredBy the Concept 'Quark', which is also expressed by the term
     * 'Quark'.
     */
    public static final Resource FormalEntity = DUL.m_model.createResource(DUL.NS + "FormalEntity");
    /**
     * Pattern: Any invariance detected from a dataset, or from observation;
     * also, any invariance proposed based on top-down considerations.E.g.
     * patterns detected and abstracted by an organism, by pattern recognition
     * algorithms, by machine learning techniques, etc.An occurrence of a
     * pattern
     * is an 'observable', or detected Situation
     */
    public static final Resource Pattern = DUL.m_model.createResource(DUL.NS + "Pattern");
    /** Norm: A social norm. */
    public static final Resource Norm = DUL.m_model.createResource(DUL.NS + "Norm");
    /**
     * Azione: An Event with at least one Agent that isParticipantIn it, and
     * that executes a Task that typically isDefinedIn a Plan, Workflow,
     * Project, etc.
     */
    public static final Resource Action = DUL.m_model.createResource(DUL.NS + "Action");
    /**
     * Group: A CollectiveAgent whose acting agents conceptualize a same
     * SocialRelation .
     */
    public static final Resource Group = DUL.m_model.createResource(DUL.NS + "Group");
    /**
     * Description: A Description is a SocialObject that represents a
     * conceptualization. It can be thought also as a 'descriptive context' that
     * uses or defines concepts in order to create a view on a 'relational
     * context' (cf. Situation) out of a set of data or observations. For
     * example, a Plan is a Description of some actions to be executed by agents
     * in a certain way, with certain parameters; a Diagnosis is a Description
     * that provides an interpretation for a set of observed entities, etc.
     */
    public static final Resource Description = DUL.m_model.createResource(DUL.NS + "Description");
    /**
     * Collective agent: A SocialAgent that is actedBy agents that are (and act
     * as) members of a Collective. A collective agent can have roles that are
     * also roles of those agents.For example, in sociology, a 'group action' is
     * the situation in which a number of people (that result to be members of a
     * collective) in a given area behave in a coordinated way in order to
     * achieve a (often common) goal. The Agent in such a Situation is not
     * single, but a CollectiveAgent (a Group). This can be generalized to the
     * notion of social movement, which assumes a large Community or even the
     * entire Society as agents.The difference between a CollectiveAgent and an
     * Organization is that a Description that introduces a CollectiveAgent is
     * also one that unifies the corresponding Collective. In practice, this
     * difference makes collective agents 'less stable' than organizations,
     * because they have a dedicated, publicly recognizable Description that is
     * conceived to introduce them.
     */
    public static final Resource CollectiveAgent = DUL.m_model.createResource(DUL.NS + "CollectiveAgent");
    /**
     * Relazione: Relations are descriptions that can be considered as the
     * counterpart of formal relations (that are included in the FormalEntity
     * class).For example, 'givingGrantToInstitution(x,y,z)' with three argument
     * types: Provider(x),Grant(y),Recipient(z), can have a Relation
     * counterpart:
     * 'GivingGrantToInstitution', which defines three Concept instances:
     * Provider,Grant,Recipient.Since social objects are not formal entities,
     * Relation includes here any 'relation-like' entity in common sense,
     * including social relations.
     */
    public static final Resource Relation = DUL.m_model.createResource(DUL.NS + "Relation");
    /**
     * Plan: A Description having an explicit Goal, to be achieved by executing
     * the plan
     */
    public static final Resource Plan = DUL.m_model.createResource(DUL.NS + "Plan");
    /**
     * Event type: A Concept that classifies an Event . An event type describes
     * how an Event should be interpreted, executed, expected, seen, etc.,
     * according to the Description that the EventType isDefinedIn (or used in)
     */
    public static final Resource EventType = DUL.m_model.createResource(DUL.NS + "EventType");
    /** null. */
    public static final Resource SpatioTemporalRegion = DUL.m_model.createResource(DUL.NS + "SpatioTemporalRegion");
    /**
     * Plan execution: Plan executions are situations that proactively satisfy a
     * plan. Subplan executions are proper parts of the whole plan execution.
     */
    public static final Resource PlanExecution = DUL.m_model.createResource(DUL.NS + "PlanExecution");
    /**
     * Goal: The Description of a Situation that is desired by an Agent, and
     * usually associated to a Plan that describes how to actually achieve it
     */
    public static final Resource Goal = DUL.m_model.createResource(DUL.NS + "Goal");
    /**
     * Space region: Any Region in a dimensional space that is used to localize
     * an Entity ; i.e., it is not used to represent some characteristic (e.g.
     * it excludes time intervals, colors, size values, judgment values, etc.).
     * Differently from a Place , a space region has a specific dimensional
     * space.
     */
    public static final Resource SpaceRegion = DUL.m_model.createResource(DUL.NS + "SpaceRegion");
    /**
     * Amount: A quantity, independently from how it is measured, computed, etc.
     */
    public static final Resource Amount = DUL.m_model.createResource(DUL.NS + "Amount");
    /** Task: An EventStructure that classifies an Action to be executed */
    public static final Resource Task = DUL.m_model.createResource(DUL.NS + "Task");
    /**
     * Progetto: A Plan that defines Role(s), Task(s), and a specific structure
     * for tasks to be executed in relation to goals to be achieved, in order to
     * achieve the main goal of the project. In other words, a project is a plan
     * with a subgoal structure and multiple roles and tasks.
     */
    public static final Resource Project = DUL.m_model.createResource(DUL.NS + "Project");
    /**
     * Right: A legal position by which an Agent is entitled to obtain something
     * from another Agent , under specified circumstances, through an
     * enforcement explicited either in a Law, Contract , etc.
     */
    public static final Resource Right = DUL.m_model.createResource(DUL.NS + "Right");
    /**
     * Classificazione: A special kind of Situation that allows to include time
     * indexing for the classifies relation in situations. For example, if a
     * Situation s 'my old cradle is used in these days as a flower pot'
     * isSettingFor the entity 'my old cradle' and the TimeIntervals '8June2007'
     * and '10June2007', and we know that s satisfies a functional Description
     * for aesthetic objects, which defines the Concepts 'flower pot' and
     * 'flower', then we also need to know what concept classifies 'my old
     * cradle' at what time.In order to solve this issue, we need to create a
     * sub-situation s' for the classification time: 'my old cradle is a flower
     * pot in 8June2007'. Such sub-situation s' isPartOf s.
     */
    public static final Resource Classification = DUL.m_model.createResource(DUL.NS + "Classification");
    /** Biological object. */
    public static final Resource BiologicalObject = DUL.m_model.createResource(DUL.NS + "BiologicalObject");
    /**
     * Contract: (The content of) an agreement between at least two agents that
     * play a Party Role, about some contract object (a Task to be executed).
     */
    public static final Resource Contract = DUL.m_model.createResource(DUL.NS + "Contract");
    /** Social relation: Any social relationship */
    public static final Resource SocialRelation = DUL.m_model.createResource(DUL.NS + "SocialRelation");
    /**
     * Social agent: Any individual whose existence is granted simply by its
     * social communicability and capability of action (through some
     * PhysicalAgent).
     */
    public static final Resource SocialAgent = DUL.m_model.createResource(DUL.NS + "SocialAgent");

    /**
     * Class constructor.
     *
     */
    private DUL() {

    }
}
