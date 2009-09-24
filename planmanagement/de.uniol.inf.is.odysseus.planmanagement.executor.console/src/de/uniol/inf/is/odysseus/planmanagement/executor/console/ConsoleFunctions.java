package de.uniol.inf.is.odysseus.planmanagement.executor.console;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;

import de.uniol.inf.is.odysseus.base.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.Subscription;

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
		if (mySink instanceof IIterableSource<?>) {
			b.append(" active: ");
			b.append(((IIterableSource<?>)mySink).isActive());
		}
		b.append("(");
		b.append(mySink.getSubscribedTo());
		b.append(")\n");

		for (Subscription<? extends ISource<?>> source : mySink
				.getSubscribedTo()) {
			dumpPlan(source.target, depth + 1, b);
		}
	}

	@SuppressWarnings("unchecked")
	public void dumpPlan(ISource<?> source, int depth, StringBuffer b) {
		if(source == null) {
			return;
		}
		if (source instanceof ISink) {
			dumpPlan((ISink<?>) source, depth, b);
		} else {
			for (int i = 0; i < depth; ++i) {
				b.append("  ");
			}
			b.append(source);
			if (source instanceof IIterableSource<?>) {
				b.append(" active: ");
				b.append(((IIterableSource<?>)source).isActive());
			}
			b.append("(");
			b.append(source.getSubscribtions());
			b.append(")\n");
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void printPlanMetadata(ISink sink) {
		System.out.println("Metadata for Operator: " + sink);
		for (String type : sink.getProvidedMonitoringData()) {
			System.out.println(type + ": "
					+ sink.getMonitoringData(type).getValue());
		}

		List<Subscription<ISource>> subscriptions = sink.getSubscribedTo();
		for (int i = 0; i < subscriptions.size(); i++) {
			if (subscriptions.get(i).target instanceof ISink) {
				printPlanMetadata((ISink) subscriptions.get(i).target);
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
