/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package de.uniol.inf.is.odysseus.scars.operator.sink.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author boomer
 * @author tommy
 */
public class NIOServer extends Thread implements IServer {

    private static final boolean DEBUG = false;

    Queue<ByteBuffer> data = new ConcurrentLinkedQueue<ByteBuffer>();
    ServerSocket socket = null;
    int port;
    SocketChannel socketChannel;
    ServerSocketChannel channel;

    public NIOServer( int port ) {
        this.port = port;
    }

    @Override
    public void run() {
        while( true ) {
            channel = null;
            try {
                channel = ServerSocketChannel.open();
                socket = channel.socket();
                socket.bind(new InetSocketAddress(port));
                channel.configureBlocking(true);

                // auf Verbindung warten
                System.out.println("NIOServer for " + port + " is waiting for connection");
                Socket clientSocket = socket.accept();
                System.out.println("NIOServer for " + port + " has connection established");
                socketChannel = clientSocket.getChannel();

//                while( clientSocket.isConnected() ) {
//                    if( !data.isEmpty()) {
//
//                        ByteBuffer b = data.remove();
//                        socketChannel.write(b);
//                    }
//                }

            } catch( IOException ex ) {
                if( DEBUG )
                    ex.printStackTrace();
            } finally {
                if( channel != null ) {
                    try {channel.close();} catch( Exception ex ) {}
                    System.out.println("Connection for " + port + " closed");
                }
            }
        }
    }

    @Override
	public void sendData(ByteBuffer buffer) {
    	try {
    		if (this.isConnected()) {
    			this.socketChannel.write(buffer);
    		}
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    /**
     * 
     * @return true if a socket connection is established
     */
    public boolean isConnected() {
    	if (this.socketChannel != null) {
    		if (this.socketChannel.isConnected()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    @Override
	public void close() {
    	try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if( channel != null ) {
                try {channel.close();} catch( Exception ex ) {}
                System.out.println("Connection for " + port + " closed");
            }
        }
    }
}
