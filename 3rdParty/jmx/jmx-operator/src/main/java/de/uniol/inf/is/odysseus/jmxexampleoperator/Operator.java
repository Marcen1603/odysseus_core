package de.uniol.inf.is.odysseus.jmxexampleoperator;

/**
 * Example of using JMX for operators or other components
 * of Odysseus.
 * Also an example of using Felix SCR annotations
 */

import de.uniol.inf.is.odysseus.jmx.services.OdysseusManagedBean;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

/**
 * @author Christian Kuka
 * @version 1.0
 */
@Component(immediate = true)
@Service(value = OdysseusManagedBean.class)
public class Operator implements OdysseusManagedBean {

    private static final String REF_NAME = "de.uniol.inf.is.odyssues:type=ExampleOperator,name=Configuration";
    private final String ALIAS = "operator";

    /**
     * The configuration for this operator
     */
    private Configuration config;

    @Activate
    protected void activate(ComponentContext context) {
        try {
            config = new Configuration();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Object getMBean() {
        return config;
    }

    @Override
    public String getObjectName() {
        return REF_NAME;
    }

    @Override
    public String getObjectNameAlias() {
        return ALIAS;
    }

    /**
     * Just an example how to use JMX for operators
     * @param event The event
     * @param port The port
     */
    protected void process_next(Object event, int port) {
        if (config.getState()) {
            config.incProcessTuple();
            // transfer(event);
        }
    }
}
