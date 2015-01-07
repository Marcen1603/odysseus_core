package de.offis.mosaik.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * This class allows you to communicate with simpy.io sockets.
 *
 * It provides methods at two abstraction levels. <code>send()</code> and
 * <code>recv()</code> work at a rather low level. They de-/encode and transmit
 * JSON message objects. <code>makeRequest()</code> and
 * <code>recvRequest()</code> provide a higher level abstraction and keep
 * track of message IDs and that stuff that you don't want to care about.
 */
public class SimpyIoSocket {
    public class MsgType {
        static final int REQ = 0;
        static final int SUCCESS = 1;
        static final int ERROR = 2;
    }

    /**
     * Returned by {@link SimpyIoSocket#recvRequest()}.
     */
    public class Request {
        private final SimpyIoSocket sock;
        private final int msgId;

        /**
         * The method name the sender wants to call on your side.
         */
        public final String method;

        /**
         * The arguments for that method call.
         */
        public final JSONArray args;

        /**
		 * The keyword arguments for that method call.
         */
        public final JSONObject kwargs;

        /**
         * @param sock is the socket instance that received the request.
         * @throws Exception
         */
        private Request(SimpyIoSocket sock) throws Exception {
            this.sock = sock;
            final JSONArray payload = sock.recv();

            // Expand payload
            final int msgType = ((Number) payload.get(0)).intValue();
            if (msgType != SimpyIoSocket.MsgType.REQ) {
                throw new IOException("Expected message type 0, got " + msgType);
            }
            this.msgId = ((Number) payload.get(1)).intValue();
            final JSONArray call = (JSONArray) payload.get(2);

            // Set request data
            this.method = (String) call.get(0);
            this.args = (JSONArray) call.get(1);
            this.kwargs = (JSONObject) call.get(2);
        }

        /**
         * Send a reply to this request passing a <em>result</em>.
         *
         * @param result the return value of the method call.
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
        public void reply(Object result) throws Exception {
            final JSONArray reply = new JSONArray();
            reply.add(SimpyIoSocket.MsgType.SUCCESS);
            reply.add(this.msgId);
            reply.add(result);
            this.sock.send(reply);
        }
    }

//    private final ServerSocket sock;
    private final Socket sock;
    private final JSONParser parser;
    private final int outMsgId;

    private final ByteBuffer header;
    private final BufferedInputStream input;
    private final BufferedOutputStream output;

    /**
     * @param addr the address to connect to (<em>host:port</em>).
     * @throws Exception
     */
    public SimpyIoSocket(String addr) throws Exception {
        final String[] parts = addr.split(":");
        final String host = parts[0];
        final int port = Integer.parseInt(parts[1]);
        this.sock = new Socket(host, port);
//        this.sock = new ServerSocket(port);
        this.parser = new JSONParser();
        this.outMsgId = 0;

        this.header = ByteBuffer.allocate(4);
        this.header.order(ByteOrder.BIG_ENDIAN);
//        Socket client = null;
//        try {
//        	System.out.println("wait for accept");
//            client = this.sock.accept();
//        	System.out.println("accept");
//        } catch (IOException e) {
//        	System.out.println("Read failed");
//        	System.exit(-1);
//        }
//        if(client != null) {
//        	this.input = new BufferedInputStream(client.getInputStream());
//        	this.output = new BufferedOutputStream(client.getOutputStream());
//        } else {
//        	this.input = null;
//        	this.output = null;
//        }
        this.input = new BufferedInputStream(this.sock.getInputStream());
        this.output = new BufferedOutputStream(this.sock.getOutputStream());
    }

    public SimpyIoSocket(Socket sock) throws Exception {
        this.sock = sock;
//        this.sock = new ServerSocket(port);
        this.parser = new JSONParser();
        this.outMsgId = 0;

        this.header = ByteBuffer.allocate(4);
        this.header.order(ByteOrder.BIG_ENDIAN);
        this.input = new BufferedInputStream(this.sock.getInputStream());
        this.output = new BufferedOutputStream(this.sock.getOutputStream());
	}

	/**
     * Close the socket.
     *
     * @throws Exception
     */
    public void close() throws Exception {
        this.sock.close();
    }

    /**
     * Request to call <em>method</em> on the remote side.
     *
     * @param method is the name of the method to be called.
     * @return the remote method's return value.
     * @throws Exception
     */
    public Object makeRequest(String method) throws Exception {
        return this.makeRequest(method, new JSONArray(), new JSONObject());
    }

    /**
     * Request to call <em>method(*args)</em> on the remote side.
     *
     * @param method is the name of the method to be called.
     * @param args is a list of arguments for the remote method call.
     * @return the remote method's return value.
     * @throws Exception
     */
    public Object makeRequest(String method, JSONArray args) throws Exception {
        return this.makeRequest(method, args, new JSONObject());
    }

    /**
     * Request to call <em>method(*args, **kwargs)</em> on the remote side.
     *
     * @param method is the name of the method to be called.
     * @param args is a list of arguments of the remote method call.
     * @param kwargs is a list of keyword arguments of the remote method call.
     * @return the remote method's return value.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Object makeRequest(String method, JSONArray args, JSONObject kwargs)
            throws Exception {
        // Construct content list
        final JSONArray call = new JSONArray();
        call.add(method);
        call.add(args);
        call.add(kwargs);

        // Construct payload
        final JSONArray req = new JSONArray();
        req.add(SimpyIoSocket.MsgType.REQ);
        req.add(this.outMsgId);
        req.add(call);

        // Make request, wait for reply and return the payload's content
        this.send(req);
        final JSONArray reply = this.recv();
        return reply.get(2);
    }

    /**
     * Wait for remote method call requests from the other side.
     *
     * @return the request object.
     * @throws Exception
     */
    public Request recvRequest() throws Exception {
        return new SimpyIoSocket.Request(this);
    }

    /**
     * Send a message to the other side.
     *
     * @param message is the payload for the message (see {@link
     *                https://mosaik.readthedocs.org/en/latest/mosaik-api/low-level.html#the-low-level-api}).
     * @throws Exception
     */
    public void send(JSONArray message) throws Exception {
        byte[] data;
        data = message.toString().getBytes();
        this.header.putInt(0, data.length);
        this.output.write(this.header.array(), 0, 4);
        this.output.write(data, 0, data.length);
        this.output.flush();
    }

    /**
     * Receive a message.
     *
     * @return the message's payload (see {@link
     *         https://mosaik.readthedocs.org/en/latest/mosaik-api/low-level.html#the-low-level-api}).
     * @throws Exception
     */
    public JSONArray recv() throws Exception {
        int size;
        int read;
        int res;

        // Read header
        read = 0;
        size = 4;  
        while (read < size) {
            res = this.input.read(this.header.array(), read, size - read);
            if (res < 0) {
                throw new IOException("Unexpected end of stream");
            }
            read += res;
        }

        // Read content
        read = 0;
        size = this.header.getInt(0);
        final byte[] data = new byte[size];
        while (read < size) {
            res = this.input.read(data, read, size - read);
            if (res < 0) {
                throw new IOException("Unexpected end of stream");
            }
            read += res;
        }

        final String message = new String(data, "UTF-8");
        final JSONArray payload = (JSONArray) this.parser.parse(message);
        return payload;
    }
}
