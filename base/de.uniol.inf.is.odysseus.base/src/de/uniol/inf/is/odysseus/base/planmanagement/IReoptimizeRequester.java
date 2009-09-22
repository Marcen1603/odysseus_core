package de.uniol.inf.is.odysseus.base.planmanagement;

/**
 * Describes an object which can request an reoptimization. Such an request
 * could be triggered by rules.
 * 
 * @author Wolf Bauer
 * 
 * @param <ReoptimizeRule>
 *            Type of the rules which could be used.
 */
public interface IReoptimizeRequester<ReoptimizeRule extends IReoptimizeRule<?>> {
	/**
	 * Send a reoptimize request.
	 */
	public void reoptimize();

	/**
	 * Adds an rule which which could trigger a reoptimization.
	 * 
	 * @param reoptimizeRule
	 *            Rule which will be added.
	 */
	public void addReoptimzeRule(ReoptimizeRule reoptimizeRule);

	/**
	 * Removes an rule which which could trigger a reoptimization.
	 * 
	 * @param reoptimizeRule
	 *            Rule which will be removed.
	 */
	public void removeReoptimzeRule(ReoptimizeRule reoptimizeRule);
}
