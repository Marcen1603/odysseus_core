package cm.model.warnings;

import cm.communication.dto.SocketInfo;
import cm.communication.socket.SocketReceiver;
import cm.data.DataHandler;
import cm.model.Collection;
import cm.model.MachineEvent;
import cm.model.MessageHandler;

import java.util.Map;

/**
 * @author Tobias
 * @since 26.04.2015.
 */
public class WarningHandler extends MessageHandler {

    public static void handleMessage(Map<String, String> message, SocketReceiver connection) {
        // TODO Look for correct collection
        MachineEvent machineEvent = new MachineEvent(connection, message);
        DataHandler.getInstance().addMachineEvent(machineEvent);
    }

}
