/**
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
package de.uniol.inf.is.odysseus.mining.physicaloperator;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <R>
 *            The type of the objects
 */
public class SOMPO<R extends IStreamObject<?>> extends AbstractPipe<R, R> {
	private static final int ITERATIONS = 0;
	private static final double LEARNING_RATE = 0;
	private Trainer trainer=null;
	/** The height of the SOM. */
	private int height = 1;
	/** The width of the SOM. */
	private int width;
	/** The depth of the SOM. */
	private int depth;

	private long iteration;

	/**
	 * 
	 * Class constructor.
	 * 
	 * @param height
	 *            The height of the SOM
	 * @param width
	 *            The width of the SOM
	 * @param depth
	 *            The depth of the SOM
	 */
	public SOMPO(final int height, final int width, final int depth) {
		this.height = height;
		this.width = width;
		this.depth = depth;
	}

	/**
	 * 
	 * Class constructor.
	 * 
	 * @param height
	 *            The height of the SOM
	 * @param width
	 *            The width of the SOM
	 */
	public SOMPO(final int height, final int width) {
		this(height, width, 1);
	}

	/**
	 * Clone constructor.
	 * 
	 * @param somPO
	 *            The object to copy from
	 */
	public SOMPO(final SOMPO<R> somPO) {
		super(somPO);
		this.height = somPO.height;
		this.width = somPO.width;
		this.depth = somPO.depth;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void process_next(final R object, final int port) {
		this.iteration++;
		// TODO Auto-generated method stub
		final double[] input = new double[] {};
		final Node bmu = this.getBestMatchingUnit(input);
		final double neighbourhoodRadius = this.getNeighbourhoodRadius(this.iteration);

		final int xstart = (int) Math.max(bmu.getX() - neighbourhoodRadius - 1, 0);
		final int ystart = (int) Math.max(bmu.getY() - neighbourhoodRadius - 1, 0);
		final int zstart = (int) Math.max(0 - neighbourhoodRadius - 1, 0);
		final int xend = (int) Math.min(xstart + (neighbourhoodRadius * 2) + 1, this.getWidth());
		final int yend = (int) Math.min(ystart + (neighbourhoodRadius * 2) + 1, this.getDepth());
		final int zend = (int) Math.min(zstart + (neighbourhoodRadius * 2) + 1, this.getHeight());
		for (int x = xstart; x < xend; x++) {
			for (int y = ystart; y < yend; y++) {
				for (int z = zstart; z < zend; z++) {
					final Node temp = this.getNode(x, y, z);
					final double distance = bmu.distanceTo(temp);
					if (distance <= neighbourhoodRadius) {
						final double influence = this.distanceFalloff(distance, neighbourhoodRadius);
						temp.adjustWeights(input, this.getLearningRate(this.iteration), influence);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SOMPO<R> clone() {
		return new SOMPO<>(this);
	}

	private Node getNode(final int x, final int y, final int z) {
		return null;
	}

	private int getWidth() {
		return this.width;
	}

	private int getHeight() {
		return this.height;
	}

	private int getDepth() {
		return this.depth;
	}

	private Node getBestMatchingUnit(final double[] input) {
		Node bmu = this.getNode(0, 0, 0);
		double minimalDistance = bmu.getWeightDistance(input);

		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				for (int z = 0; z < this.getDepth(); z++) {
					final Node node = this.getNode(x, y, z);
					final double distance = node.getWeightDistance(input);
					if (distance < minimalDistance) {
						bmu = node;
						minimalDistance = distance;
					}
				}
			}
		}
		return bmu;

	}

	private Double distanceFalloff(final Double distance, final Double radius) {
		return Math.exp(-(distance * distance) / (2 * radius * radius));
	}

	private double getMapRadius() {
		return FastMath.max(this.getHeight(), FastMath.max(this.getWidth(), this.getDepth())) / 2.0;
	}

	private double getLearningRate(final long iteration) {
		return SOMPO.LEARNING_RATE * Math.exp(-iteration / SOMPO.ITERATIONS);
	}

	private double getNeighbourhoodRadius(final long iteration) {
		final double radius = this.getMapRadius();
		final double time = SOMPO.ITERATIONS / FastMath.log(radius);
		return radius * Math.exp(-iteration / time);
	}

	@SuppressWarnings("unused")
	private void startTrainer() {
		if (this.trainer != null) {
			this.trainer.terminate();
			this.trainer = null;
		}
		// trainer = new Trainer(refInputs);
		this.trainer.start();

	}

	public class Trainer extends Thread {

		private final List<List<Double>> inputs;
		private boolean terminate;
		private boolean isTerminated = true;

		public Trainer(final List<List<Double>> inputs) {
			super();
			this.inputs = inputs;
			this.terminate = false;
			this.isTerminated = true;
			this.setPriority(Thread.MIN_PRIORITY);
		}

		public void terminate() {
			this.terminate = true;
		}

		public boolean isTerminated() {
			return this.isTerminated;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			this.isTerminated = false;
			this.terminate = false;
			@SuppressWarnings("unused")
			double learningRate = SOMPO.LEARNING_RATE;
			final double radius = Math.max(SOMPO.this.getWidth() / 2, SOMPO.this.getHeight() / 2);
			final double time = SOMPO.ITERATIONS / Math.log(radius);
			for (int i = 0; (i < SOMPO.ITERATIONS) && !this.terminate; ++i) {
				final double neighborhoodRadius = radius * Math.exp(-i / time);
				learningRate = SOMPO.this.getLearningRate(i);
				for (final List<Double> curInput : this.inputs) {
					final Node bmu = this.getBestMatchingUnit(curInput);
					final int xstart = (int) Math.max(bmu.getX() - neighborhoodRadius - 1, 0);
					final int ystart = (int) Math.max(bmu.getY() - neighborhoodRadius - 1, 0);
					final int zstart = (int) Math.max(0 - neighborhoodRadius - 1, 0);
					final int xend = (int) Math.min(xstart + (neighborhoodRadius * 2) + 1, SOMPO.this.getWidth());
					final int yend = (int) Math.min(ystart + (neighborhoodRadius * 2) + 1, SOMPO.this.getDepth());
					final int zend = (int) Math.min(zstart + (neighborhoodRadius * 2) + 1, SOMPO.this.getHeight());
					for (int x = xstart; x < xend; x++) {
						for (int y = ystart; y < yend; y++) {
							for (int z = zstart; z < zend; z++) {
								final Node temp = SOMPO.this.getNode(x, y, z);
								final double distance = bmu.distanceTo(temp);
								if (distance <= neighborhoodRadius) {
									// temp.adjustWeights(curInput, learningRate, distance);
								}
							}
						}
					}
				}
			}
			this.isTerminated = true;
		}

		/**
		 * @param curInput
		 * @return
		 */
		private Node getBestMatchingUnit(final List<Double> curInput) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
