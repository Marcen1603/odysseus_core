package de.uniol.inf.is.odysseus.base.planmanagement;

/**
 * Describes a rule which defines when a reoptimization should be started.
 * 
 * @author Wolf Bauer
 * 
 * @param <RuleOwnerType>
 *            Type of the objects which are called if the rule is valid.
 */
public interface IReoptimizeRule<RuleOwnerType extends IReoptimizeRequester<?>> {
	/**
	 * Adds an object which is called if the rule is valid.
	 * 
	 * @param reoptimieRequester Object which will be added.
	 */
	public void addReoptimieRequester(RuleOwnerType reoptimieRequester);


	/**
	 * Removes an object which is called if the rule is valid.
	 * 
	 * @param reoptimieRequester Object which will be removed.
	 */
	public void removeReoptimieRequester(RuleOwnerType reoptimieRequester);
}
