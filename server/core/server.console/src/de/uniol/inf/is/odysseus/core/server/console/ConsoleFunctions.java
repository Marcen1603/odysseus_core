package de.uniol.inf.is.odysseus.core.server.console;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;

public class ConsoleFunctions {

	public String[] getArgs(CommandInterpreter ci) {
		ArrayList<String> args = new ArrayList<String>();

		boolean running = true;
		String current = "";
		while (running) {
			current = ci.nextArgument();
			if (current != null && current.length() > 0) {
				args.add(current);
			} else {
				running = false;
			}
		}
		return args.toArray(new String[0]);
	}
	
	public void dumpPlan(ISink<?> mySink, int depth, StringBuffer b) {
		if(mySink == null) {
			return;
		}
		for (int i = 0; i < depth; ++i) {
			b.append("  ");
		}
		b.append(mySink);
		b.append("(");
		b.append(mySink.getSubscribedToSource());
		b.append(")\n");
		if(mySink instanceof IPhysicalOperatorKeyValueProvider) {
			for (int i = 0; i < depth; ++i) {
				b.append("  ");
			}
			b.append("[");
			for(Map.Entry<String, String> entry : ((IPhysicalOperatorKeyValueProvider) mySink).getKeyValues().entrySet()) {
				b.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
			}
			b.append("]\n");
			
		}

		for (AbstractPhysicalSubscription<? extends ISource<?>,?> source : mySink
				.getSubscribedToSource()) {
			dumpPlan(source.getSource(), depth + 1, b);
		}
	}

	public void dumpPlan(ISource<?> source, int depth, StringBuffer b) {
		if(source == null) {
			return;
		}
		if (source.isSink()) {
			dumpPlan((ISink<?>) source, depth, b);
		} else {
			for (int i = 0; i < depth; ++i) {
				b.append("  ");
			}
			b.append(source);
			b.append("(");
			b.append(source.getSubscriptions());
			b.append(")\n");
			if(source instanceof IPhysicalOperatorKeyValueProvider) {
				for (int i = 0; i < depth; ++i) {
					b.append("  ");
				}
				b.append("[");
				for(Map.Entry<String, String> entry : ((IPhysicalOperatorKeyValueProvider) source).getKeyValues().entrySet()) {
					b.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
				}
				b.append("]\n");
				
			}
		}		
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void printPlanMetadata(ISink sink) {
		System.out.println("Metadata for Operator: " + sink);
		for (String type : sink.getProvidedMonitoringData()) {
			System.out.println(type + ": "
					+ sink.getMonitoringData(type).getValue());
		}

		Collection<AbstractPhysicalSubscription<? extends ISource<?>,?>> subscriptions = sink.getSubscribedToSource();
		for (AbstractPhysicalSubscription<? extends ISource<?>,?> sub :  subscriptions) {
			if (sub.getSource().isSink()) {
				printPlanMetadata((ISink) sub.getSource());
			}
		}
	}

	//TODO die methode kommt auch in partial plan vor, sollte evtl in die operatoren rein
	public String getOwnerIDs(List<IOperatorOwner> owner) {
		String result = "";
		for (IOperatorOwner iOperatorOwner : owner) {
			if(result != "") {
				result += ", ";
			}
			result += iOperatorOwner.getID();
		}
		return result;
	} 

}
