package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = Integer.MAX_VALUE, minInputPorts = 1, name = "Sender")
public class SenderAO extends AbstractLogicalOperator {
    /**
     * 
     */
    private static final long   serialVersionUID = -6830784739913623456L;
    private String              dataHandler      = "Tuple";

    private String              protocolHandler;
    private String              transportHandler;
    private Map<String, String> optionsMap;
    private String              wrapper;

    public SenderAO(AbstractLogicalOperator po) {
        super(po);
    }

    public SenderAO() {
        super();
    }

    public SenderAO(String wrapper, Map<String, String> optionsMap) {
        this.wrapper = wrapper;
        this.optionsMap = optionsMap;
    }

    public SenderAO(String wrapper, String dataHandler, Map<String, String> optionsMap) {
        this.wrapper = wrapper;
        this.dataHandler = dataHandler;
        this.optionsMap = optionsMap;
    }

    public SenderAO(SenderAO senderAO) {
        this.wrapper = senderAO.wrapper;
        this.dataHandler = senderAO.dataHandler;
        this.optionsMap = senderAO.optionsMap;
        this.protocolHandler = senderAO.protocolHandler;
        this.transportHandler = senderAO.transportHandler;
    }

    // @Parameter(name = "options", type = MapParameter.class, optional = true,)
    public void setOptions(Map<String, String> value) {
        this.optionsMap = value;
    }

    public Map<String, String> getOptionsMap() {
        return optionsMap;
    }

    @Parameter(name = "wrapper", type = StringParameter.class, optional = false)
    public void setWrapper(String wrapper) {
        this.wrapper = wrapper;
    }

    public String getWrapper() {
        return wrapper;
    }

    public String getDataHandler() {
        return dataHandler;
    }

    @Parameter(name = "dataHandler", type = StringParameter.class, optional = false)
    public void setDataHandler(String dataHandler) {
        this.dataHandler = dataHandler;
    }

    public String getProtocolHandler() {
        return protocolHandler;
    }

    @Parameter(name = "protocol", type = StringParameter.class, optional = false)
    public void setProtocolHandler(String protocolHandler) {
        this.protocolHandler = protocolHandler;
    }

    public String getTransportHandler() {
        return transportHandler;
    }

    @Parameter(name = "transport", type = StringParameter.class, optional = false)
    public void setTransportHandler(String transportHandler) {
        this.transportHandler = transportHandler;
    }

    @Override
    public AbstractLogicalOperator clone() {
        return new SenderAO(this);
    }
}
