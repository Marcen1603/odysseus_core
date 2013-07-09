/*
 * Copyright 2013 The Odysseus Team
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
	private long timestamp = 0l;

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
			Thread.sleep(1000);
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
			tuple.addLong(this.timestamp);
			this.timestamp++;
			final List<String> distributions = new ArrayList<String>();
			final String[] values = line.split(",");
			for (final String value : values) {
				if (!value.isEmpty()) {
					if (value.contains("[")) {
						// Send continuous distribution
						distributions.add(value);
					} else {
						if (value.contains(";")) {
							// Send discrete probabilistic value
							this.generateDiscreteAttribute(tuple, value);
						} else {
							if (value.contains(".")) {
								// Send double value
								tuple.addDouble(value);
							} else {
								// Send continuous probabilistic value (Index to
								// distribution)
								tuple.addInteger(value);
							}
						}
					}
				}
			}

			for (final String value : distributions) {
				this.generateContinuousAttribute(tuple, value);
			}
			return tuple;
		}
		return null;
	}

	private void generateDiscreteAttribute(final DataTuple tuple,
			final String string) {
		final String[] probabilisticValues = string.split(";");
		tuple.addInteger(probabilisticValues.length);
		for (final String probabilisticValue : probabilisticValues) {
			final String[] probabilisticParameter = probabilisticValue
					.split(":");
			// The value
			tuple.addDouble(probabilisticParameter[0]);
			// The probability
			tuple.addDouble(probabilisticParameter[1]);
		}
	}

	private void generateContinuousAttribute(final DataTuple tuple,
			final String string) {
		final String[] components = string.split("<");
		final String[] mixtures = components[0].split("\\|");
		tuple.addInteger(mixtures.length);
		final int dimension = mixtures[0].split(";")[1].split(":").length;
		tuple.addInteger(dimension);
		for (final String mixture : mixtures) {
			this.generateContinuousAttributeMixture(tuple, dimension, mixture);
		}
		if (components.length > 1) {
			final String[] parameter = components[1].split(";");
			if (parameter.length > 0) {
				// The scale factor
				tuple.addDouble(parameter[0]);
			} else {
				tuple.addDouble(1.0);
			}
			if (parameter.length > 1) {
				final String[] supportParameter = parameter[1].substring(1,
						parameter[1].length() - 1).split(":");
				for (int i = 0; i < (dimension * 2); i++) {
					// The support on each dimension
					tuple.addDouble(supportParameter[i]);
				}
			} else {
				for (int i = 0; i < (dimension * 2); i++) {
					// The support on each dimension
					if ((i % 2) == 0) {
						tuple.addDouble(Double.NEGATIVE_INFINITY);
					} else {
						tuple.addDouble(Double.POSITIVE_INFINITY);
					}
				}
			}
		} else {
			tuple.addDouble(1.0);
			for (int i = 0; i < (dimension * 2); i++) {
				// The support on each dimension
				if ((i % 2) == 0) {
					tuple.addDouble(Double.NEGATIVE_INFINITY);
				} else {
					tuple.addDouble(Double.POSITIVE_INFINITY);
				}
			}
		}
	}

	private void generateContinuousAttributeMixture(final DataTuple tuple,
			final int dimension, final String string) {
		final String[] probabilisticValues = string.split(";");
		// The weight
		final String weight = probabilisticValues[0];
		final String[] meanParameter = probabilisticValues[1].substring(1,
				probabilisticValues[1].length() - 1).split(":");

		tuple.addDouble(weight);
		for (final String element : meanParameter) {
			// The mean in dimension i
			tuple.addDouble(element);
		}
		final String[] covarianceParameter = probabilisticValues[2].substring(
				1, probabilisticValues[2].length() - 1).split(":");
		for (final String element : covarianceParameter) {
			// The Covariance Entry i
			tuple.addDouble(element);
		}
	}

	public static void main(final String[] args) {
		final ProbabilisticDataProvider provider = new ProbabilisticDataProvider();
		provider.init();
		provider.next();
		provider.next();

		provider.next();
	}
}
