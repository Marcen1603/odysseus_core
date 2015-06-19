package cm.communication.socket;

import cm.communication.dto.AttributeInformation;
import cm.communication.dto.SocketInfo;
import cm.communication.transformStrategies.ITransformStrategy;
import cm.communication.transformStrategies.QueryResults;
import cm.communication.transformStrategies.TransformStrategyFactory;
import cm.data.DataHandler;
import cm.model.Event;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tobias
 * @since 26.04.2015.
 */
public class SocketReceiver {

    private SocketInfo socketInfo;
    private Thread socketReceiverThread;

    public SocketReceiver(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
        this.socketReceiverThread = new SocketReceiveThread(socketInfo);
        this.socketReceiverThread.setDaemon(true); // To stop the thread when the user closes the main window
        this.socketReceiverThread.start();
    }

    class SocketReceiveThread extends Thread {

        private SocketInfo socketInfo;

        protected SocketReceiveThread(SocketInfo socketInfo) {
            this.socketInfo = socketInfo;
        }

        public void run() {
            try {
                Socket socket = new Socket(socketInfo.getIp(), socketInfo.getPort());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                while (!isInterrupted()) {
                    int firstInt = dataInputStream.readInt();
                    List<Object> values = new ArrayList<Object>();

                    for (AttributeInformation info : socketInfo.getSchema()) {
                        byte b = dataInputStream.readByte();
                        if (b == 1) {
                            ITransformStrategy transformStrategy = TransformStrategyFactory.getInstance().getTransformStrategy(info.getDataType());
                            if (transformStrategy != null) {
                                values.add(transformStrategy.transformBytesToObject(dataInputStream));
                            } else {
                                throw new RuntimeException("Transform strategy not available for datatype " + info.getDataType());
                            }
                        } else {
                            values.add(null);
                        }
                    }
                    QueryResults results = new QueryResults(values);

                    Map<String, String> valueMap = new HashMap<>();
                    for (int i = 0; i < results.getValues().size(); i++) {
                        Object o = results.getValues().get(i);
                        String name = socketInfo.getSchema().get(i).getName();
                        valueMap.put(name, o.toString());
                    }
                    DataHandler.getInstance().addEvent(new Event(SocketReceiver.this, valueMap));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SocketInfo getSocketInfo() {
        return socketInfo;
    }

    public void stopReceiver() {
        this.socketReceiverThread.interrupt();
    }
}
