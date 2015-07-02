package de.uniol.inf.is.odysseus.query.transformation.java.model.osgi;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.osgi.xmlns.scr.v1_1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.osgi.xmlns.scr.v1_1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Component }
     * 
     */
    public Component createComponent() {
        return new Component();
    }

    /**
     * Create an instance of {@link NewDataSet }
     * 
     */
    public NewDataSet createNewDataSet() {
        return new NewDataSet();
    }

    /**
     * Create an instance of {@link Component.Implementation }
     * 
     */
    public Component.Implementation createComponentImplementation() {
        return new Component.Implementation();
    }

    /**
     * Create an instance of {@link Component.Reference }
     * 
     */
    public Component.Reference createComponentReference() {
        return new Component.Reference();
    }

}
