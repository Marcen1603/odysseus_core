/********************************************************************************** 
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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Dennis Geesen
 * 
 */
public class ConvolutionFilterPO<M extends IMetaAttribute> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private List<SDFAttribute> attributes;
	private int[] positions;
	private double[] weights;
	private SDFExpression expression;
	private int size = 0;
	private Map<Integer, LinkedList<Tuple<M>>> list = new HashMap<>();
	private IGroupProcessor<Tuple<M>, Tuple<M>> groupProcessor = null;
	private int totalSize;

	public ConvolutionFilterPO(SDFExpression expression, List<SDFAttribute> attributes, int size) {
		this.expression = expression;
		this.attributes = attributes;
		this.size = size;
		this.totalSize = (size * 2) + 1;
	}

	public ConvolutionFilterPO(ConvolutionFilterPO<M> old) {
		this.expression = old.expression;
		this.attributes = old.attributes;
		this.size = old.size;
		this.totalSize = old.totalSize;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		initWeights();

		positions = new int[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			SDFAttribute a = attributes.get(i);
			positions[i] = this.getOutputSchema().indexOf(a);
		}
	}

	/**
	 * 
	 */
	private void initWeights() {
		this.weights = new double[(this.size * 2) + 1];
		// left part
		for (int i = 0; i < this.size; i++) {
			double x = i - this.size;
			this.weights[i] = calculateWeight(x);
		}
		// middle
		this.weights[this.size] = calculateWeight(0);
		// right part
		for (int i = this.size + 1; i < (this.size * 2) + 1; i++) {
			double x = i - this.size;
			this.weights[i] = calculateWeight(x);
		}

	}

	/**
	 * @param x
	 * @return
	 */
	private double calculateWeight(double x) {
		this.expression.bindVariables(x);
		return this.expression.getValue();
	}

	@Override
	protected synchronized void process_next(Tuple<M> o, int port) {
		Integer groupID = 0;
		if (groupProcessor != null) {
			groupID = groupProcessor.getGroupID(o);
		}
		if (this.list.get(groupID) == null) {
			this.list.put(groupID, new LinkedList<Tuple<M>>());
		}
		this.list.get(groupID).addLast(o);
		if (this.list.get(groupID).size() >= totalSize) {
			Tuple<M> weightedTuple = this.list.get(groupID).get(this.size).clone();
			for (int pos : this.positions) {
				weightedTuple.setAttribute(pos, getWeightedValue(pos, groupID));
			}
			transfer(weightedTuple);
			this.list.get(groupID).removeFirst();
		}

	}

	/**
	 * @param pos
	 * @return
	 */
	private double getWeightedValue(int pos, int groupID) {
		double weighted = 0.0d;
		for (int i = 0; i < totalSize; i++) {
			double old = ((Number) this.list.get(groupID).get(i).getAttribute(pos)).doubleValue();
			weighted = weighted + (old * this.weights[i]);
		}
		return weighted;
	}

	@Override
	public ConvolutionFilterPO<M> clone() {
		return new ConvolutionFilterPO<M>(this);
	}

	/**
	 * @param relationalGroupProcessor
	 */
	public void setGroupProcessor(IGroupProcessor<Tuple<M>, Tuple<M>> groupProcessor) {
		this.groupProcessor = groupProcessor;		
	}

}
