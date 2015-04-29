package cm.model.warnings;

import cm.data.DataHandler;
import cm.model.Machine;
import cm.model.MachineEvent;
import cm.model.MessageHandler;
import com.google.gson.Gson;

/**
 * @author Tobias
 * @since 26.04.2015.
 */
public class WarningHandler extends MessageHandler {

    public static void handleMessage(String message) {
        Gson gson = new Gson();
        Warning warning = gson.fromJson(message, Warning.class);
        Machine machine = DataHandler.getInstance().getMachine(warning.getMachineId());
        MachineEvent machineEvent = new MachineEvent(machine, warning.getDescription());
        DataHandler.getInstance().addMachineEvent(machineEvent);
        System.out.println("Warning(" + warning.getLevel() + ") " + warning.getMachineId() + ", " + warning.getSensorId() + ", " + warning.getDescription());
    }

}
