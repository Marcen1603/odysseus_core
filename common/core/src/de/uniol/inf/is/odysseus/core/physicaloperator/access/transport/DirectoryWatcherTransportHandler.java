/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class DirectoryWatcherTransportHandler extends AbstractPushTransportHandler {
    private static final String DIRECTORY = "directory";
    private static final String FILTER = "filter";
    Path directory;
    Pattern filter;
    WatchService watcher;

    /**
     * 
     * Class constructor.
     *
     */
    public DirectoryWatcherTransportHandler() {
        super();
    }

    /**
     * 
     * Class constructor.
     *
     * @param protocolHandler
     * @param options
     */
    public DirectoryWatcherTransportHandler(final IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        super(protocolHandler, options);
        if (options.containsKey(DirectoryWatcherTransportHandler.DIRECTORY)) {
            this.directory = Paths.get(options.get(DirectoryWatcherTransportHandler.DIRECTORY));
        }
        else {
            throw new IllegalArgumentException("No directory given!");
        }
        if (options.containsKey(DirectoryWatcherTransportHandler.FILTER)) {
            this.filter = Pattern.compile(options.get(DirectoryWatcherTransportHandler.FILTER));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final byte[] message) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final Map<String, String> options) {
        final DirectoryWatcherTransportHandler handler = new DirectoryWatcherTransportHandler(protocolHandler, options);
        return handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Directory";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInOpen() throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.directory.register(this.watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        this.fireOnConnect();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (DirectoryWatcherTransportHandler.this.watcher != null) {
                        WatchKey key;
                        try {
                            key = DirectoryWatcherTransportHandler.this.watcher.take();
                        }
                        catch (InterruptedException x) {
                            return;
                        }

                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();

                            if (kind == OVERFLOW) {
                                continue;
                            }
                            @SuppressWarnings("unchecked")
                            WatchEvent<Path> ev = (WatchEvent<Path>) event;
                            Path filename = ev.context();

                            try {
                                Path child = DirectoryWatcherTransportHandler.this.directory.resolve(filename);
                                if ((DirectoryWatcherTransportHandler.this.filter == null) || (DirectoryWatcherTransportHandler.this.filter.matcher(child.toString()).find())) {
                                    fireProcess(new FileInputStream(child.toFile()));
                                }
                            }
                            catch (IOException x) {
                            	x.printStackTrace();
                            	continue;
                            }
                        }
                        boolean valid = key.reset();
                        if (!valid) {
                            break;
                        }
                    }
                }
                catch (Exception x) {
                	x.printStackTrace();
                    return;
                }
            }
        }).start();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutOpen() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInClose() throws IOException {
        this.watcher.close();
        this.watcher = null;
        this.fireOnDisconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof DirectoryWatcherTransportHandler)) {
            return false;
        }
        final DirectoryWatcherTransportHandler other = (DirectoryWatcherTransportHandler) o;
        if (this.directory != other.directory) {
            return false;
        }

        return true;
    }

}
