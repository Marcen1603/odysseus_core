package cm.model;

/**
 * Created by Tobi on 03.04.2015.
 */
public class MachineEvent {

    Machine machine;
    String description;

    public MachineEvent(Machine machine, String description) {
        this.machine = machine;
        this.description = description;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
