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
package de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;

public class P2PSinkPO<T extends IStreamObject<?>> extends AbstractSink<T> {

    static private Logger logger = LoggerFactory.getLogger(P2PSinkPO.class);

    private String queryId;
    private PipeAdvertisement adv;
    public ArrayList<StreamHandler> subscribe = new ArrayList<StreamHandler>();
    private PeerGroup peerGroup;

    class ConnectionListener extends Thread {

        @Override
        public void run() {
            JxtaServerSocket server = null;
            try {
                server = new JxtaServerSocket(getPeerGroup(), adv, 10);
                server.setSoTimeout(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                Socket socket = null;
                try {
                    logger.debug("Waiting for Server to connect");
                    socket = server.accept();
                    logger.debug("Connection from " + socket.getRemoteSocketAddress());
                    logger.debug("Adding Handler");
                    StreamHandler temp = new StreamHandler(socket);
                    temp.start();
                    synchronized (subscribe) {
                        subscribe.add(temp);
                    }
                    logger.debug("Adding Handler done");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class StreamHandler extends Thread {
        OutputStream outS;
        ObjectOutputStream dout;
        Socket socket;
        List<Object> linkedList = new LinkedList<Object>();

        public StreamHandler(Socket socket) {
            this.socket = socket;
            try {
                outS = socket.getOutputStream();
                dout = new ObjectOutputStream(outS);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (getSubscribedToSource().get(0) == null) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized void transfer(Object o) {
            synchronized (linkedList) {
                linkedList.add(o);
            }
            notifyAll();
        }

        public void done() {
            try {
                dout.writeObject(Integer.valueOf(0));
                dout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
                dout.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            while (!interrupted()) {
                synchronized (this) {
                    try {
                        wait(1000);
                        synchronized (linkedList) {
                            for (Object o : linkedList) {
                                try {
                                    dout.writeObject(o);
                                    dout.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            linkedList.clear();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public P2PSinkPO(String adv, PeerGroup peerGroup) {
        super();
        this.peerGroup = peerGroup;
        this.adv = MessageTool.createPipeAdvertisementFromXml(adv);
    }

    public void startListener() {
        ConnectionListener listener = new ConnectionListener();
        listener.start();
    }

    @Override
    protected void process_next(T object, int port) {
        synchronized (subscribe) {
            for (StreamHandler sh : subscribe) {
                sh.transfer(object);
            }
            if (subscribe.size() == 0) {
                logger.warn("No recever for object");
            }
        }

    }

    protected void process_done() {
        synchronized (subscribe) {
            for (StreamHandler sh : subscribe) {
                sh.done();
            }
        }
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public void setAdv(PipeAdvertisement adv) {
        this.adv = adv;
    }

    public PipeAdvertisement getAdv() {
        return adv;
    }

    public PeerGroup getPeerGroup() {
        return peerGroup;
    }

    public void setPeerGroup(PeerGroup peerGroup) {
        this.peerGroup = peerGroup;
    }

    @Override
    public P2PSinkPO<T> clone() {
        throw new RuntimeException("Clone Not implemented yet");
    }

    @Override
    public void processPunctuation(PointInTime timestamp, int port) {
        throw new RuntimeException("process punctuation not implemented");
    }

}
