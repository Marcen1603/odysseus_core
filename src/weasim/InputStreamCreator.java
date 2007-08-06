package weasim;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class InputStreamCreator {
	private EntityManagerFactory ef;

	private EntityManager em;

	private static InputStreamCreator instance = new InputStreamCreator();

	private InputStreamCreator() {
		ef = Persistence.createEntityManagerFactory("weasim");
		em = ef.createEntityManager();
	}

	public static InputStreamCreator getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public synchronized ObjectInputStream createStream(String queryStr,
			TimePeriod period) throws IOException {
		Query query = em.createQuery(queryStr);
		List<Object> results = query.getResultList();
		if (results.isEmpty()) {
			throw new IllegalArgumentException(
					"query did not return any results");
		}

		Simulation s = new Simulation(period, results);
		try {
			PipedOutputStream pOut = new PipedOutputStream();
			PipedInputStream pIn = new PipedInputStream(pOut);
			ObjectOutputStream oOut = new ObjectOutputStream(pOut);
			ObjectInputStream oIn = new ObjectInputStream(pIn);
			
			s.addOutput(oOut);
			s.start();
			
			return oIn;
		} catch (IOException e) {
			s.interrupt();
			throw e;
		}
	}

	public synchronized ObjectInputStream createStream(String queryStr) throws IOException {
		return createStream(queryStr, new PeriodicalTimePeriod(1000));
	}
}
