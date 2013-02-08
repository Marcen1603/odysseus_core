package de.uniol.inf.is.odysseus.generator.probabilistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticDataProvider extends StreamClientHandler {
	private BufferedReader reader;

	public ProbabilisticDataProvider() {

	}

	@Override
	public synchronized List<DataTuple> next() {
		final List<DataTuple> tuples = new ArrayList<DataTuple>();
		DataTuple tuple = null;
		try {
			tuple = this.generateDataTuple();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		if (tuple != null) {
			tuples.add(tuple);
		} else {
			this.close();
			this.init();
		}
		try {
			Thread.sleep(500);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		return tuples;
	}

	@Override
	public void init() {
		final URL fileURL = Activator.getContext().getBundle()
				.getEntry("/data/data");
		try {
			final InputStream inputStream = fileURL.openConnection()
					.getInputStream();
			this.reader = new BufferedReader(new InputStreamReader(inputStream));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		if (this.reader != null) {
			try {
				this.reader.close();
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				this.reader = null;
			}
		}
	}

	@Override
	public ProbabilisticDataProvider clone() {
		return new ProbabilisticDataProvider();
	}

	private DataTuple generateDataTuple() throws IOException {
		final DataTuple tuple = new DataTuple();
		String line = null;
		while (line == null) {
			line = this.reader.readLine();
			if (line == null) {
				break;
			}
			if (line.startsWith("#")) {
				line = null;
			}
		}
		if (line != null) {
			tuple.addLong(System.currentTimeMillis());
			final String[] values = line.split(",");
			for (final String value : values) {
				if (!value.isEmpty()) {
					if (value.contains("[")) {
						// Send continuous distribution
						final String[] mixtures = value.split("\\|");

						tuple.addInteger(mixtures.length);
						final int dimension = mixtures[0]
								.split(";")[1].split(":").length;
						tuple.addInteger(dimension);
						for (final String mixture : mixtures) {
							final String[] probabilisticValues = mixture
									.split(";");
							// The weight
							String weight = probabilisticValues[0];
							final String[] meanParameter = probabilisticValues[1]
									.substring(1,
											probabilisticValues[1].length() - 1)
									.split(":");

							tuple.addDouble(weight);
							for (final String element : meanParameter) {
								// The mean in dimension i
								tuple.addDouble(element);
							}
							final String[] covarianceParameter = probabilisticValues[2]
									.substring(1,
											probabilisticValues[2].length() - 1)
									.split(":");
							for (final String element : covarianceParameter) {
								// The Covariance Entry i
								tuple.addDouble(element);
							}
							if (probabilisticValues.length > 3) {
								// The scale factor
								tuple.addDouble(probabilisticValues[3]);
							} else {
								tuple.addDouble(1.0);
							}
							if (probabilisticValues.length > 4) {
								final String[] supportParameter = probabilisticValues[4]
										.substring(
												1,
												probabilisticValues[4].length() - 1)
										.split(":");
								for (int i = 0; i < dimension * 2; i++) {
									// The support on each dimension
									tuple.addDouble(supportParameter[i]);
								}
							} else {
								for (int i = 0; i < dimension * 2; i++) {
									// The support on each dimension
									if (i % 2 == 0) {
										tuple.addDouble(Double.NEGATIVE_INFINITY);
									} else {
										tuple.addDouble(Double.POSITIVE_INFINITY);
									}
								}
							}
						}

					} else {
						if (value.contains(";")) {
							// Send discrete probabilistic value
							final String[] probabilisticValues = value
									.split(";");
							tuple.addInteger(probabilisticValues.length);
							for (final String probabilisticValue : probabilisticValues) {
								final String[] probabilisticParameter = probabilisticValue
										.split(":");
								// The value
								tuple.addDouble(probabilisticParameter[0]);
								// The probability
								tuple.addDouble(probabilisticParameter[1]);
							}
						} else {
							// Send continuous probabilistic value (Index to
							// distribution)
							tuple.addInteger(value);
						}
					}
				}
			}
			System.out.println(tuple);
			return tuple;
		}
		return null;
	}
}
