package de.uniol.inf.is.odysseus.client.communication.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.client.communication.transformStrategies.ITransformStrategy;
import de.uniol.inf.is.odysseus.client.communication.transformStrategies.QueryResults;
import de.uniol.inf.is.odysseus.client.communication.transformStrategies.TransformStrategyFactory;
import de.uniol.inf.is.odysseus.client.data.DataHandler;
import de.uniol.inf.is.odysseus.client.model.JsonEvent;
import de.uniol.inf.is.odysseus.client.model.SchemaEvent;
import de.uniol.inf.is.odysseus.rest.dto.response.AttributeInformation;
import de.uniol.inf.is.odysseus.rest.dto.response.SocketInfo;

/**
 * @author Tobias Brandt
 * @since 26.04.2015.
 */
public class SocketReceiver {

    private SocketInfo socketInfo;
    private Thread socketReceiverThread;

    public SocketReceiver(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
        this.socketReceiverThread = new SocketReceiveThread(socketInfo);
        this.socketReceiverThread.start();
    }

    public SocketInfo getSocketInfo() {
        return socketInfo;
    }

    public void stopReceiver() {
        this.socketReceiverThread.interrupt();
    }

    class SocketReceiveThread extends Thread {

        private SocketInfo socketInfo;

        private SocketReceiveThread(SocketInfo socketInfo) {
            this.socketInfo = socketInfo;
        }

        public void run() {
            try {
                Socket socket = new Socket(socketInfo.getIp(), socketInfo.getPort());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                while (!isInterrupted()) {
                    // We need to read the first integer (size of the buffer I guess)
                    int bufferSize = dataInputStream.readInt();
                    if (socketInfo.getSchema().size() == 0) {
                        // This stream is schema-less, probably KeyValue.
                        String json = readKeyValueStreamElement(dataInputStream, bufferSize);
                        JsonEvent jsonEvent = new JsonEvent(SocketReceiver.this, json);
                        DataHandler.getInstance().addEvent(jsonEvent);
                    } else {
                        // This stream comes with a schema, probably a tuple.
                        List<Object> attributeValues = readAttributesFromObjectWithSchema(dataInputStream);

                        // At this point, no data should be left in the input-stream

                        Map<String, String> attributes = putValuesInMap(attributeValues);
                        SchemaEvent schemaEvent = new SchemaEvent(SocketReceiver.this, attributes);
                        DataHandler.getInstance().addEvent(schemaEvent);
                    }
                }
                dataInputStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private List<Object> readAttributesFromObjectWithSchema(DataInputStream dataInputStream) throws IOException {
            List<Object> attributeValues = new ArrayList<>();

            for (AttributeInformation info : socketInfo.getSchema()) {
                byte b = dataInputStream.readByte();
                if (b == 1) {
                    ITransformStrategy transformStrategy = TransformStrategyFactory.getInstance()
                            .getTransformStrategy(info.getDataType());
                    if (transformStrategy != null) {
                        attributeValues.add(transformStrategy.transformBytesToObject(dataInputStream));
                    } else {
                        throw new RuntimeException(
                                "Transform strategy not available for datatype " + info.getDataType());
                    }
                } else {
                    attributeValues.add(null);
                }
            }
            return attributeValues;
        }

        private Map<String, String> putValuesInMap(List<Object> attributeValues) {
            QueryResults results = new QueryResults(attributeValues);

            Map<String, String> attributes = new HashMap<>(socketInfo.getSchema().size());
            for (int i = 0; i < results.getValues().size(); i++) {
                Object o = results.getValues().get(i);
                if (o != null) {
                    String name = socketInfo.getSchema().get(i).getName();
                    attributes.put(name, o.toString());
                }
            }

            return attributes;
        }

        private String readKeyValueStreamElement(DataInputStream dataInputStream, int size) {
            try {
                byte[] buffer = new byte[size];
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) dataInputStream.read();
                }

                CharBuffer decoded = Charset.forName("UTF-8").newDecoder().decode(ByteBuffer.wrap(buffer));

                return decoded.toString();
            } catch (Exception e) {
                throw new RuntimeException("Could not decode data.", e);
            }
        }
    }
}
