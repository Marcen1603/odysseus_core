package mining.generator.base.socket;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

public class StreamServer extends Thread {
    
    private ServerSocket socket;        
    private Class<StreamClientHandler> streamClientHandler;

    @SuppressWarnings("unchecked")
	public StreamServer(int port, Class<?> streamClientHandler) throws Exception {    	
    	this.streamClientHandler = (Class<StreamClientHandler>)streamClientHandler;
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        socket = serverChannel.socket();
        socket.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(true);        
    }

    @Override
    public void run() {
    	System.out.println("Starting new server on port "+this.socket.getLocalPort());
        while (true) {
            Socket connection = null;
            try {
            	System.out.println("Waiting for connection...");
                connection = socket.accept();
                System.out.println("New connection from "+connection.getInetAddress());
                StreamClientHandler streamClient = this.streamClientHandler.newInstance();
                streamClient.setConnection(connection);
                streamClient.start();
            } catch (Exception ex) {
                System.err.println("Error: "+ex.getStackTrace());
                continue;
            }
        }
    }   
}
