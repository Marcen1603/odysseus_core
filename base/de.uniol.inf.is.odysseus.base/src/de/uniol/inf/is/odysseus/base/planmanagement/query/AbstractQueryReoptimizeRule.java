package de.uniol.inf.is.odysseus.base.planmanagement.query;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRule;

public abstract class AbstractQueryReoptimizeRule implements
		IReoptimizeRule<IQuery> {

	protected ArrayList<IQuery> reoptimizable;

	@Override
	public void addReoptimieRequester(IQuery reoptimizable) {
		this.reoptimizable.add(reoptimizable);
	}

	@Override
	public void removeReoptimieRequester(IQuery reoptimizable) {
		this.reoptimizable.remove(reoptimizable);
	}

	protected void fireReoptimizeEvent() {
		for (IQuery reoptimizableType : this.reoptimizable) {
			reoptimizableType.reoptimize();
		}
	}
}
