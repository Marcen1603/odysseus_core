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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class DirectoryWatcherTransportHandler extends AbstractPushTransportHandler {
	/** Logger. */
	static final Logger LOG = LoggerFactory.getLogger(DirectoryWatcherTransportHandler.class);
	protected static final String DIRECTORY = "directory";
	protected static final String FILTER = "filter";
	protected static final String CACHEFILES = "cachefiles";
	protected static final String CACHESIZE = "cachesize";
	protected static final int defaultCacheSize = 10;

	Path directory;
	Pattern filter;
	WatchService watcher;
	LoadingCache<File, FileInputStream> fileCache;

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
	public DirectoryWatcherTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		if (options.containsKey(DirectoryWatcherTransportHandler.DIRECTORY)) {
			this.directory = Paths.get(options.get(DirectoryWatcherTransportHandler.DIRECTORY));
		} else {
			throw new IllegalArgumentException("No directory given!");
		}
		if (options.containsKey(DirectoryWatcherTransportHandler.FILTER)) {
			this.filter = Pattern.compile(options.get(DirectoryWatcherTransportHandler.FILTER));
		}
		if (options.get(CACHEFILES, "false").equalsIgnoreCase("true")) {
			fileCache = CacheBuilder.newBuilder().maximumSize(options.getInt(CACHESIZE, defaultCacheSize))
					.build(new CacheLoader<File, FileInputStream>() {
						@Override
						public FileInputStream load(File file) throws Exception {
							return new FileInputStream(file);
						}
					});
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
	public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
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

	protected void onChangeDetected(File file) throws IOException {
		if (fileCache != null) {
			try {
				fireProcess(fileCache.get(file));
			} catch (ExecutionException e) {
				throw new IOException("Error while loading \"" + file + "\" into cache", e);
			}
		} else
			fireProcess(new FileInputStream(file));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processInOpen() throws IOException {
	}

	@Override
	public void processInStart() {
		try {
			this.watcher = FileSystems.getDefault().newWatchService();
			this.directory.register(this.watcher, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			this.fireOnConnect();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						try (DirectoryStream<Path> stream = Files
								.newDirectoryStream(DirectoryWatcherTransportHandler.this.directory)) {
							for (Path child : stream) {
								if ((DirectoryWatcherTransportHandler.this.filter == null)
										|| (DirectoryWatcherTransportHandler.this.filter.matcher(child.toString())
												.find())) {
									onChangeDetected(child.toFile());
								}
							}
						} catch (IOException | DirectoryIteratorException e) {
							LOG.error(e.getMessage(), e);
						}
						while (DirectoryWatcherTransportHandler.this.watcher != null) {
							WatchKey key;
							try {
								key = DirectoryWatcherTransportHandler.this.watcher.take();
							} catch (InterruptedException e) {
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
									if ((DirectoryWatcherTransportHandler.this.filter == null)
											|| (DirectoryWatcherTransportHandler.this.filter.matcher(child.toString())
													.find())) {
										onChangeDetected(child.toFile());
									}
								} catch (IOException e) {
									LOG.error(e.getMessage(), e);

									continue;
								}
							}
							boolean valid = key.reset();
							if (!valid) {
								break;
							}
						}
					} catch (Exception x) {
						return;
					}
				}
			}).start();
		} catch (IOException e1) {
			throw new StartFailedException(e1);
		}

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
