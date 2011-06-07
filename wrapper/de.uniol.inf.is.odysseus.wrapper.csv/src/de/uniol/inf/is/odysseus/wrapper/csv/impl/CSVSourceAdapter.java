package de.uniol.inf.is.odysseus.wrapper.csv.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.Source;

public class CSVSourceAdapter extends AbstractPushingSourceAdapter implements SourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(CSVSourceAdapter.class);

    private Map<Source, Thread> serverThreads = new HashMap<Source, Thread>();

    @Override
    public String getName() {
        return "CSV";
    }

    @Override
    protected void doInit(Source source) {
        int port = 4444;
        if (source.getConfiguration().containsKey("port")) {
            port = Integer.parseInt(source.getConfiguration().get("port").toString());
        }
        CSVServer server = new CSVServer(source, this, port);
        Thread serverThread = new Thread(server);
        this.serverThreads.put(source, serverThread);
        serverThread.start();

    }

    @Override
    protected void doDestroy(Source source) {
        serverThreads.get(source).interrupt();
        serverThreads.remove(source);
    }

    class CSVServer implements Runnable {
        private ServerSocket serverSocket = null;
        private Source source;
        private CSVSourceAdapter adapter;

        public CSVServer(Source source, CSVSourceAdapter adapter, int port) {
            try {
                serverSocket = new ServerSocket(port);
            }
            catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        @Override
        public void run() {
            List<Thread> processingThreads = new ArrayList<Thread>();
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket;
                try {
                    socket = this.serverSocket.accept();
                    CSVProcessor processor = new CSVProcessor(this.source, socket, this.adapter);
                    Thread processingThread = new Thread(processor);
                    processingThreads.add(processingThread);
                    processingThread.start();
                }
                catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            for (Thread processingThread : processingThreads) {
                processingThread.interrupt();
            }
        }

        class CSVProcessor implements Runnable {
            private Socket server;
            private CSVSourceAdapter adapter;
            private Source source;

            public CSVProcessor(Source source, Socket server, CSVSourceAdapter adapter) {
                this.server = server;
                this.adapter = adapter;
            }

            @Override
            public void run() {
                LineNumberReader reader;
                try {
                    reader = new LineNumberReader(new InputStreamReader(server.getInputStream()));

                    String line = reader.readLine();
                    if (line != null) {
                        while (((line = reader.readLine()) != null)
                                && (!Thread.currentThread().isInterrupted())) {
                            String[] values = line.split(",");
                            adapter.transfer(source.getName(), System.currentTimeMillis(), values);
                        }
                    }
                }
                catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }

    }

}
