package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class PartialPlan implements IPartialPlan {
	private ArrayList<IIterableSource<?>> iterableSource;
	private List<ISink<?>> roots;
	private int priority;

	public PartialPlan(List<IIterableSource<?>> iterableSource,
			List<ISink<?>> roots, int priority) {
		this.iterableSource = new ArrayList<IIterableSource<?>>(iterableSource);
		this.roots = roots;
		this.priority = priority;
	}
	
	private String getOwnerIDs(ArrayList<IOperatorOwner> owner) {
		String result = "";
		for (IOperatorOwner iOperatorOwner : owner) {
			if(result != "") {
				result += ", ";
			}
			result += iOperatorOwner.getID();
		}
		return result;
	} 

	@Override
	public List<IIterableSource<?>> getIterableSource() {
		return iterableSource;
	}

	@Override
	public List<ISink<?>> getRoots() {
		return roots;
	}

	@Override
	public int getPriority() {
		return this.priority;
	}

	@Override
	public int hashCode() {
		return iterableSource.hashCode();
	}

	@Override
	public String toString() {
		String result = "Roots:";
		
		for (ISink<?> root : this.roots) {
			if(result != "") {
				result += AppEnv.LINE_SEPERATOR;
			}
			result += root.toString() + ", Owner: " + getOwnerIDs(root.getOwner());
		}
		
		result += AppEnv.LINE_SEPERATOR + "Sources:";
		
		for (IIterableSource<?> source : iterableSource) {
			if(result != "") {
				result += AppEnv.LINE_SEPERATOR;
			}
			result += source.toString() + ", Owner: " + getOwnerIDs(source.getOwner());
		}
		return result;
	}
}
