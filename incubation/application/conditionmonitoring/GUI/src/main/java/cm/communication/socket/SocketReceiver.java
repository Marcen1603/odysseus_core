package cm.communication.socket;

import cm.communication.dto.AttributeInformation;
import cm.communication.dto.SocketInfo;
import cm.communication.transformStrategies.ITransformStrategy;
import cm.communication.transformStrategies.QueryResults;
import cm.communication.transformStrategies.TransformStrategyFactory;
import cm.model.Message;
import cm.model.warnings.WarningHandler;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias
 * @since 26.04.2015.
 */
public class SocketReceiver {

    private SocketInfo socketInfo;

    public SocketReceiver(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
        new SocketReceiveThread(socketInfo).start();
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
                    dataInputStream.readInt();
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
                    Gson gson = new Gson();
                    Message message = gson.fromJson(results.getValues().get(0).toString(), Message.class);
                    switch (message.getMessageType()) {
                        case WARNING:
                            WarningHandler.handleMessage(message.getMessageContent());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
