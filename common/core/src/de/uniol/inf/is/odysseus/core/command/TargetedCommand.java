package de.uniol.inf.is.odysseus.core.command;

import java.util.List;

public abstract class TargetedCommand<TargetId> extends Command
{
	final private List<Object> targets;
	final private boolean resolveTargets;
	
	private List<Object> resolvedTargets;
	
	public TargetedCommand(List<Object> targetNames, boolean resolveTargets) {
		this.targets = targetNames;
		this.resolveTargets = resolveTargets;
	}
	
	public boolean needsTargetsResolved() {
		return resolveTargets;
	}
	
	public List<Object> getTargets() {
		return targets;
	}
	
	public void setResolvedTargets(List<Object> resolvedTargets) {
		this.resolvedTargets = resolvedTargets;
	}
	
	public List<Object> getResolvedTargets() {
		return resolvedTargets;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	final public boolean run()
	{
		boolean success = true;		
		
		if (resolveTargets) {			
			for (Object target : getResolvedTargets())
				if (!run((TargetId) target)) success = false;
		} else {
			for (Object target : getTargets())
				if (!run((TargetId) target)) success = false;
		}

		return success;
	}
	
	protected abstract boolean run(TargetId targetId);	
}
