package cm.communication;

import cm.communication.dto.SocketInfo;
import cm.communication.rest.RestException;
import cm.communication.rest.RestService;
import cm.communication.socket.SocketReceiver;
import cm.configuration.ConnectionInformation;
import cm.data.ConnectionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tobias
 * @since 17.06.2015.
 */
public class CommunicationService {

    static Map<ConnectionInformation, List<SocketReceiver>> connectionInformationListMap = new HashMap<>();

    public static void establishConnection(ConnectionInformation connectionInformation) {
        try {
            List<SocketReceiver> receiverList = connectionInformationListMap.get(connectionInformation);
            if (receiverList == null) {
                receiverList = new ArrayList<>();
                connectionInformationListMap.put(connectionInformation, receiverList);
            }
            String token = RestService.login(connectionInformation.ip, "System", "manager");
            List<SocketInfo> socketInfos = null;
            if (connectionInformation.useName) {
                socketInfos = RestService.getResultsFromQuery(connectionInformation.ip, token, connectionInformation.queryName);
            } else {
                socketInfos = RestService.getResultsFromQuery(connectionInformation.ip, token, connectionInformation.queryId);
            }

            for (SocketInfo socketInfo : socketInfos) {
                SocketReceiver receiver = new SocketReceiver(socketInfo);
                receiverList.add(receiver);
                ConnectionHandler.getInstance().addConnection(receiver);
            }
        } catch (RestException e) {
            e.printStackTrace();
        }
    }

    public static List<SocketReceiver> getSocketReceivers(ConnectionInformation connectionInformation) {
        return connectionInformationListMap.get(connectionInformation);
    }

}
