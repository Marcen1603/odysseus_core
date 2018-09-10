/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;

/**
 * this class represents the result of the intialization of the current query in
 * benchmarker UI. COntains strategies for inter operator parallelization
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkInitializationResult {

	private IEditorPart part;
	private ISelection selection;

	// query data
	private IFile queryFile;
	private List<String> queryStringArray = new ArrayList<String>();
	private List<ILogicalQuery> logicalQueries = new ArrayList<ILogicalQuery>();

	private Map<ILogicalOperator, List<IParallelTransformationStrategy<ILogicalOperator>>> strategiesForOperator = new HashMap<ILogicalOperator, List<IParallelTransformationStrategy<ILogicalOperator>>>();

	public Map<ILogicalOperator, List<IParallelTransformationStrategy<ILogicalOperator>>> getStrategiesForOperator() {
		return strategiesForOperator;
	}

	public List<IParallelTransformationStrategy<ILogicalOperator>> getStrategiesForOperator(
			ILogicalOperator operator) {
		return strategiesForOperator.get(operator);
	}

	/**
	 * adds a list of strategies for an specific operator
	 * 
	 * @param operator
	 * @param strategies
	 */
	public void setStrategiesForOperator(ILogicalOperator operator,
			List<IParallelTransformationStrategy<ILogicalOperator>> strategies) {
		if (!strategiesForOperator.containsKey(operator)) {
			strategiesForOperator
					.put(operator,
							new ArrayList<IParallelTransformationStrategy<ILogicalOperator>>());
		}
		List<IParallelTransformationStrategy<ILogicalOperator>> list = strategiesForOperator
				.get(operator);
		list.addAll(strategies);
	}

	public List<ILogicalQuery> getLogicalQueries() {
		return logicalQueries;
	}

	public void setLogicalQueries(List<ILogicalQuery> logicalQueries) {
		this.logicalQueries = logicalQueries;
	}

	public void addLogicalQuery(ILogicalQuery logicalQuery) {
		this.logicalQueries.add(logicalQuery);
	}

	public IFile getQueryFile() {
		return queryFile;
	}

	public void setQueryFile(IFile queryFile) {
		this.queryFile = queryFile;
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	public IEditorPart getPart() {
		return part;
	}

	public void setPart(IEditorPart part) {
		this.part = part;
	}

	public void addQueryString(String queryString) {
		this.queryStringArray.add(queryString);
	}

	public List<String> getQueryStringArray() {
		return queryStringArray;
	}
}
