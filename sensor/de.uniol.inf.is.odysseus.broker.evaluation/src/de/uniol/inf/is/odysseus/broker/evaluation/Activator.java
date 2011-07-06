/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.broker.evaluation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.broker.benchmarker.BenchmarkService;
import de.uniol.inf.is.odysseus.broker.evaluation.pql.BrokerAOBuilder;
import de.uniol.inf.is.odysseus.broker.evaluation.pql.CycleBuilder;
import de.uniol.inf.is.odysseus.broker.evaluation.pql.FreqCycleBuilder;
import de.uniol.inf.is.odysseus.broker.evaluation.pql.UpdateEvaluationAOBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private static BenchmarkService bs;
	
	public static BenchmarkService getBenchmarkService(){
		return bs;
	}

	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		//loading transform
		
		
		//loading PQL
		OperatorBuilderFactory.putOperatorBuilderType("BROKER", new BrokerAOBuilder());
		OperatorBuilderFactory.putOperatorBuilderType("UPEVAL", new UpdateEvaluationAOBuilder());
		OperatorBuilderFactory.putOperatorBuilderType("CYCLE", new CycleBuilder());
		OperatorBuilderFactory.putOperatorBuilderType("FREQCYCLE", new FreqCycleBuilder());
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		OperatorBuilderFactory.removeOperatorBuilderType("BROKER");
		OperatorBuilderFactory.removeOperatorBuilderType("UPEVAL");
		OperatorBuilderFactory.removeOperatorBuilderType("CYCLE");
		OperatorBuilderFactory.removeOperatorBuilderType("FREQCYCLE");
		Activator.context = null;		
	}
	


	public void bindBS(BenchmarkService service) {
		bs = service;
		System.out.println("benchmark bound!!");
	}

	public void unbindBS(BenchmarkService bs) {
		bs = null;
	}

}
