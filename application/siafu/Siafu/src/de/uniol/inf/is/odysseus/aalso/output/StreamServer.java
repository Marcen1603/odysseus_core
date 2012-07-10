/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.aalso.output;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

import de.uniol.inf.is.odysseus.aalso.model.World;

public class StreamServer extends Thread {

    private ServerSocket socket;
    private Class<StreamClientHandler> streamClientHandler;
    private World world;
    private Boolean stopThread = false;
    private String placeName;
    private int port;
    private String host = "";

    @SuppressWarnings("unchecked")
    public StreamServer(int port, String host, Class<?> streamClientHandler, World world, String placeName) throws Exception {
        this.setStreamClientHandler((Class<StreamClientHandler>) streamClientHandler);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        socket = serverChannel.socket();
        socket.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(true);
        this.setWorld(world);
        this.setPlaceName(placeName);
        this.setPort(port);
        this.setHost(host);
    }

    @Override
    public void run() {
        System.out.println("Starting new server on port " + this.socket.getLocalPort());
        while (!isClosing()) {
            Socket connection = null;
            StreamClientHandler streamClient = null;
            try {
                System.out.println("Waiting for connection...");
                streamClient = new AalsoDataProvider(this.getWorld(), this.getPlaceName(), port, host);
                System.out.println(((AalsoDataProvider) streamClient).getQuery());
                connection = socket.accept();
                System.out.println("New connection from " + connection.getInetAddress());
                streamClient.setConnection(connection);
                streamClient.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
    }

    public synchronized void close() {
        this.setStopThread(true);
    }

    private synchronized boolean isClosing() {
        return this.getStopThread();
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setStreamClientHandler(Class<StreamClientHandler> streamClientHandler) {
        this.streamClientHandler = streamClientHandler;
    }

    public Class<StreamClientHandler> getStreamClientHandler() {
        return streamClientHandler;
    }

    public void setStopThread(Boolean stopThread) {
        this.stopThread = stopThread;
    }

    public Boolean getStopThread() {
        return stopThread;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
