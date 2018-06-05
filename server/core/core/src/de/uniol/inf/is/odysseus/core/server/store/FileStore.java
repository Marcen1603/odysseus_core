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
package de.uniol.inf.is.odysseus.core.server.store;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.util.FileUtils;
import de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream;

public class FileStore<IDType extends Serializable & Comparable<? extends IDType>, STORETYPE extends Serializable>
		extends AbstractStore<IDType, STORETYPE> {

	private static final long serialVersionUID = -1840600055097437910L;
	public static final String FILENAME = "filename";
	public static final String TYPE = "filestore";

	Logger logger = LoggerFactory.getLogger(FileStore.class);

	private String path;
	private MemoryStore<IDType, STORETYPE> cache = new MemoryStore<IDType, STORETYPE>();
	private Map<IDType, Boolean> serializableTestPassed = new HashMap<IDType, Boolean>();

	private boolean initialzed;

	@Override
	public IStore<IDType, STORETYPE> newInstance(OptionMap options) throws StoreException {
		options.checkRequiredException(FILENAME);

		FileStore<IDType, STORETYPE> store;
		try {
			store = new FileStore<IDType, STORETYPE>(options.get(FILENAME));
		} catch (IOException e) {
			throw new StoreException(e);
		}
		return store;
	}

	public FileStore(){
	}

	public FileStore(String path) throws IOException {
		this.path = path;
		initialzed = false;
		//loadCache();
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@SuppressWarnings("unchecked")
	private void loadCache() throws IOException {
		File f = FileUtils.openOrCreateFile(path);
		OsgiObjectInputStream in = null;

		try {
			FileInputStream fis = new FileInputStream(f);
			in = new OsgiObjectInputStream(fis);
			IDType key = null;
			try {
				while ((key = (IDType) in.readObject()) != null) {
					// Could be unreadable input
					try {
						STORETYPE element = (STORETYPE) in.readObject();
						cache.put(key, element);
						// Object that have been written must be serializable
						// ;-)
						serializableTestPassed.put(key, Boolean.TRUE);
					} catch (Exception e) {
						logger.error("Error reading from " + path + " "
								+ e.getMessage() + " for key " + key);
						e.printStackTrace();
					}
				}
			} catch (EOFException e) {
				// Ignore ;
			}
			in.close();
		} catch (Exception e) {
		}
		initialzed = true;
		logger.trace("Loaded from " + path + " " + cache.entrySet().size()
				+ " values");

	}

	private void saveCache() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				new File(path)));
		for (Entry<IDType, STORETYPE> e : cache.entrySet()) {

			if (serializableTestPassed.get(e.getKey())) {
				try {
					out.writeObject(e.getKey());
					out.writeObject(e.getValue());
				} catch (NotSerializableException ex) {
					logger.error("Object " + e.getKey()
							+ " could not be saved.");
				}
			} else {
				logger.error("Object " + e.getKey() + " could not be saved.");
			}

		}
		out.close();
	}

	@Override
	public STORETYPE get(IDType id) {
		if (!initialzed) {
			try {
				loadCache();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cache.get(id);
	}

	@Override
	public List<Entry<IDType, STORETYPE>> getOrderedByKey(long limit) {
		if (!initialzed) {
			try {
				loadCache();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cache.getOrderedByKey(limit);
	}

	@Override
	public void put(IDType id, STORETYPE elem) throws StoreException {
		if (!initialzed) {
			try {
				loadCache();
			} catch (IOException e) {
				throw new StoreException(e);
			}
		}
		cache.put(id, elem);
		// Do serializable test for this new object
		try {
			ObjectOutputStream test = new ObjectOutputStream(
					new ByteArrayOutputStream());
			test.writeObject(elem);
			// If no exception is thrown, object is serializable
			serializableTestPassed.put(id, Boolean.TRUE);
			saveCache();
		} catch (Exception e) {
			logger.warn(e.getMessage()
					+ " Tried to store non serializable object " + elem
					+ " in " + path);
			e.printStackTrace();
			serializableTestPassed.put(id, Boolean.FALSE);
		}
	}

	@Override
	public void clear() throws StoreException {
		cache.clear();
		try {
			saveCache();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public boolean containsKey(IDType key) {
		return cache.containsKey(key);
	}

	@Override
	public STORETYPE remove(IDType id) throws StoreException {
		STORETYPE ret = cache.remove(id);
		try {
			saveCache();
		} catch (IOException e) {
			throw new StoreException(e);
		}
		return ret;
	}

	@Override
	public Set<Entry<IDType, STORETYPE>> entrySet() {
		return cache.entrySet();
	}

	@Override
	public Set<IDType> keySet() {
		return cache.keySet();
	}

	@Override
	public Collection<STORETYPE> values() {
		return cache.values();
	}

	@Override
	public void commit() {
		try {
			saveCache();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int size() {
		return cache.size();
	}

}
