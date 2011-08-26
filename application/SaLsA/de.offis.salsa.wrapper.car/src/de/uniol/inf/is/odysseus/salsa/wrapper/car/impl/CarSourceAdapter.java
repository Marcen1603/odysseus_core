package de.uniol.inf.is.odysseus.salsa.wrapper.car.impl;

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
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class CarSourceAdapter extends AbstractPushingSourceAdapter implements SourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(CarSourceAdapter.class);

    private final Map<SourceSpec, Thread> serverThreads = new HashMap<SourceSpec, Thread>();

    @Override
    public String getName() {
        return "SALSA-Car";
    }

    @Override
    protected void doDestroy(SourceSpec source) {
        this.serverThreads.get(source).interrupt();
        this.serverThreads.remove(source);
    }

    @Override
    protected void doInit(SourceSpec source) {
        int port = 4444;
        if (source.getConfiguration().containsKey("port")) {
            port = Integer.parseInt(source.getConfiguration().get("port").toString());
        }
        final CarServer server = new CarServer(source, this, port);
        final Thread serverThread = new Thread(server);
        this.serverThreads.put(source, serverThread);
        serverThread.start();
    }

    class CarServer implements Runnable {
        private ServerSocket serverSocket = null;
        private SourceSpec source;
        private CarSourceAdapter adapter;

        public CarServer(final SourceSpec source, final CarSourceAdapter adapter, final int port) {
            try {
                this.serverSocket = new ServerSocket(port);
            }
            catch (final IOException e) {
                CarSourceAdapter.LOG.error(e.getMessage(), e);
            }
        }

        @Override
        public void run() {
            final List<Thread> processingThreads = new ArrayList<Thread>();
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket;
                try {
                    socket = this.serverSocket.accept();
                    final CarProcessor processor = new CarProcessor(this.source, socket,
                            this.adapter);
                    final Thread processingThread = new Thread(processor);
                    processingThreads.add(processingThread);
                    processingThread.start();
                }
                catch (final IOException e) {
                    CarSourceAdapter.LOG.error(e.getMessage(), e);
                }
            }
            for (final Thread processingThread : processingThreads) {
                processingThread.interrupt();
            }
        }

        class CarProcessor implements Runnable {
            private final Socket server;
            private final CarSourceAdapter adapter;
            private SourceSpec source;

            public CarProcessor(final SourceSpec source, final Socket server,
                    final CarSourceAdapter adapter) {
                this.server = server;
                this.adapter = adapter;
            }

            @Override
            public void run() {
                LineNumberReader reader;
                try {
                    reader = new LineNumberReader(new InputStreamReader(
                            this.server.getInputStream()));

                    String line = reader.readLine();
                    if (line != null) {
                        while (((line = reader.readLine()) != null)
                                && (!Thread.currentThread().isInterrupted())) {
                            final String[] values = line.split(",");
                            this.adapter.transfer(this.source, System.currentTimeMillis(), values);
                        }
                    }
                }
                catch (final IOException e) {
                    CarSourceAdapter.LOG.error(e.getMessage(), e);
                }
            }
        }

    }
}
