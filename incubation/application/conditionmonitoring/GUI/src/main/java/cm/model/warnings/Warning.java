package cm.model.warnings;

/**
 * @author Tobias
 * @since 26.04.2015.
 */
public class Warning {

    private int machineId;
    private int sensorId;
    private WarningLevel level;
    private String description;

    public Warning(int machineId, int sensorId, WarningLevel level, String description) {
        this.machineId = machineId;
        this.sensorId = sensorId;
        this.level = level;
        this.description = description;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public WarningLevel getLevel() {
        return level;
    }

    public void setLevel(WarningLevel level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
