package de.uniol.inf.is.odysseus.base.planmanagement;

public interface IReoptimizeRequester<ReoptimizeRule extends IReoptimizeRule<?>> {
	public void reoptimize();

	public void addReoptimzeRule(ReoptimizeRule reoptimizeRule);

	public void removeReoptimzeRule(ReoptimizeRule reoptimizeRule);
}
