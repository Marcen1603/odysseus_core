package de.uniol.inf.is.odysseus.sensormanagement.server.script;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
 
public enum InitSensorManagementKeywordParameter implements IKeywordParameter 
{
	INSTANCE_NAME("instanceName", 0, false),
    LOGGING_DIRECTORY("loggingDir", 1, false);
 
    private InitSensorManagementKeywordParameter(String name, int position, boolean isOptional){
        this.name = name;
        this.position = position;
        this.isOptional = isOptional;
    }
 
    private String name;
    private Integer position;
    private boolean isOptional;
 
    @Override
    public String getName() {
        return this.name;
    }
 
    @Override
    public int getPosition() {
        return this.position;
    }
 
    @Override
    public boolean isOptional() {
        return this.isOptional;
    }
}