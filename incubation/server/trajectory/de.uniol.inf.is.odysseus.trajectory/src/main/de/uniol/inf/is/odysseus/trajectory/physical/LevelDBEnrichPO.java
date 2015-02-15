package de.uniol.inf.is.odysseus.trajectory.physical;

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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;

public class LevelDBEnrichPO<T extends IMetaAttribute> extends AbstractEnrichPO<Tuple<T>, T> {

	private final static Logger LOGGER = LoggerFactory.getLogger(LevelDBEnrichPO.class);
	
	private final static Options OPTIONS = new Options();
	
	static {
		OPTIONS.createIfMissing(false);
	}
	
	private final String key;
	
	private final File levelDBPath;
	
	private DB levelDB;
	
	
	public LevelDBEnrichPO(LevelDBEnrichPO<T> abstractEnrichPO) {
		super(abstractEnrichPO);
		
		this.key = abstractEnrichPO.key;
		this.levelDBPath = abstractEnrichPO.levelDBPath;
	}

	public LevelDBEnrichPO(ICache cache,
			IDataMergeFunction<Tuple<T>, T> dataMergeFunction,
			IMetadataMergeFunction<T> metaMergeFunction, int[] uniqueKeys,
			File levelDBPath) {
		super(cache, dataMergeFunction, metaMergeFunction, uniqueKeys);
			
		this.key = null;
		this.levelDBPath = levelDBPath;
	}

	@Override
	protected void internal_process_open() {
		try {
			this.levelDB = factory.open(this.levelDBPath, OPTIONS);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected List<IStreamObject<?>> internal_process(Tuple<T> input) {
		final ByteArrayInputStream arrayInputStream = 
				new ByteArrayInputStream(this.levelDB.get(bytes((String)input.getAttribute(0))));
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
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			if(objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		
		LOGGER.info(input.toString());
		
		input.setAttribute(3, object);
		
		final List<IStreamObject<?>> l = new ArrayList<>();
		l.add(input);
		return l;
	}

	@Override
	protected void internal_process_close() {
		try {
			if(this.levelDB != null) {
				this.levelDB.close();
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
