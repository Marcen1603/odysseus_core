package de.uniol.inf.is.odysseus.base.planmanagement;

public interface IReoptimizeRule<RuleOwnerType extends IReoptimizeRequester<?>> {
	public void addReoptimieRequester(RuleOwnerType reoptimieRequester);
	
	public void removeReoptimieRequester(RuleOwnerType reoptimieRequester);
}
