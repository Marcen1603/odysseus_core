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

    private static Map<ConnectionInformation, List<SocketReceiver>> connectionInformationListMap = new HashMap<>();
    private static String token;
    private static String loginIp;

    public static void establishConnection(String token, ConnectionInformation connectionInformation) {
        try {
            List<SocketReceiver> receiverList = connectionInformationListMap.get(connectionInformation);
            if (receiverList == null) {
                receiverList = new ArrayList<>();
                connectionInformationListMap.put(connectionInformation, receiverList);
            }
            List<SocketInfo> socketInfos = null;
            if (connectionInformation.isUseName()) {
                socketInfos = RestService.getResultsFromQuery(connectionInformation.getIp(), token, connectionInformation.getQueryName());
            } else {
                socketInfos = RestService.getResultsFromQuery(connectionInformation.getIp(), token, connectionInformation.getQueryId(),connectionInformation.getQueryName());
            }

            for (SocketInfo socketInfo : socketInfos) {
                SocketReceiver receiver = new SocketReceiver(socketInfo);
                receiverList.add(receiver);
                ConnectionHandler.getInstance().addConnection(receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<SocketReceiver> getSocketReceivers(ConnectionInformation connectionInformation) {
        return connectionInformationListMap.get(connectionInformation);
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        CommunicationService.token = token;
    }

    public static String getLoginIp() {
        return loginIp;
    }

    public static void setLoginIp(String loginIp) {
        CommunicationService.loginIp = loginIp;
    }
}
