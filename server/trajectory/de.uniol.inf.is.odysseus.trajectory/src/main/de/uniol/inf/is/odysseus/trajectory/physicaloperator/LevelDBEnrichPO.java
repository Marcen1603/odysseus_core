/*
 * Copyright 2015 Marcus Behrendt
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
 *
**/

package de.uniol.inf.is.odysseus.trajectory.physicaloperator;

import static org.fusesource.leveldbjni.JniDBFactory.bytes;
import static org.fusesource.leveldbjni.JniDBFactory.factory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractEnrichPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;

/**
 * A physical operator for <tt>LevelDBEnrichAO</tt>.
 * 
 * @author marcus
 *
 * @param <T> an type of <tt>IMetaAttribute</tt>
 */
public class LevelDBEnrichPO<T extends IMetaAttribute> extends AbstractEnrichPO<Tuple<T>, T> {

	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(LevelDBEnrichPO.class);
	
	/** options for the LevelDB */
	private final static Options OPTIONS = new Options();
	
	static {
		OPTIONS.createIfMissing(false);
	}
	
	/** the file path to the LevelDB */
	private final File levelDBPath;
	
	/** the position of the key attribute in the attribute schema */
	private final int in;
	
	/** the position of the attribute in the attribute schema where to write result */
	private final int out;
	
	private DB levelDB;
	
	
	public LevelDBEnrichPO(final LevelDBEnrichPO<T> abstractEnrichPO) {
		super(abstractEnrichPO);
		
		this.levelDBPath = abstractEnrichPO.levelDBPath;
		this.in = abstractEnrichPO.in;
		this.out = abstractEnrichPO.out;
	}

	public LevelDBEnrichPO(final ICache cache,
			final IDataMergeFunction<Tuple<T>, T> dataMergeFunction,
			final IMetadataMergeFunction<T> metaMergeFunction, final int[] uniqueKeys,
			final File levelDBPath, final int in, final int out) {
		super(cache, dataMergeFunction, metaMergeFunction, uniqueKeys);
			
		this.levelDBPath = levelDBPath;
		this.in = in;
		this.out = out;
	}

	@Override
	protected void internal_process_open() {
		try {
			this.levelDB = factory.open(this.levelDBPath, OPTIONS);
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected List<IStreamObject<?>> internal_process(final Tuple<T> input) {
		
		final ByteArrayInputStream arrayInputStream = 
				new ByteArrayInputStream(this.levelDB.get(bytes((String)input.getAttribute(this.in))));
		ObjectInputStream objectInputStream = null;
		Object object = null;
		try {
			objectInputStream = new ObjectInputStream(arrayInputStream);
			object = objectInputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				arrayInputStream.close();
			} catch (final IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			if(objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (final IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		
		input.setAttribute(this.out, object);
		
		final List<IStreamObject<?>> tupleList = new ArrayList<>();
		tupleList.add(input);
		return tupleList;
	}

	@Override
	protected void internal_process_close() {
		try {
			if(this.levelDB != null) {
				this.levelDB.close();
			}
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
