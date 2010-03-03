package de.uniol.inf.is.odysseus.new_transformation.stream_characteristics;

import java.io.File;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;

public class StreamCharacteristicDatabase {
	private static StreamCharacteristicDatabase instance = null;

	public static StreamCharacteristicDatabase getInstance() {
		if (instance == null) {
			try {
				instance = new StreamCharacteristicDatabase();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	private final Repository repository;

	public StreamCharacteristicDatabase() throws RepositoryException {
		File dataDir = new File("./sesame/");
		repository = new SailRepository(new NativeStore(dataDir));
		repository.initialize();
	}

	public Repository getRepository() {
		return repository;
	}
}
