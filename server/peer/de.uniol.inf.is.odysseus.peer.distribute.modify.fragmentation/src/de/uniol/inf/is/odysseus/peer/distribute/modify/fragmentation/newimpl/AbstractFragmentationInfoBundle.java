package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

public abstract class AbstractFragmentationInfoBundle {

	private int degreeOfFragmentation;

	private ILogicalOperator originStartOperator;

	private Optional<ILogicalOperator> originEndOperator;
	
	private Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap;

	public void setDegreeOfFragmentation(int degree) {

		this.degreeOfFragmentation = degree;

	}

	public int getDegreeOfFragmentation() {

		return this.degreeOfFragmentation;

	}

	public void setOriginStartOperator(ILogicalOperator operator) {

		this.originStartOperator = operator;

	}

	public ILogicalOperator getOriginStartOperator() {

		return this.originStartOperator;

	}

	public void setOriginEndOperator(Optional<ILogicalOperator> operator) {

		this.originEndOperator = operator;

	}

	public Optional<ILogicalOperator> getOriginEndOperator() {

		return this.originEndOperator;

	}
	
	public void setCopyMap(Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap) {
		
		this.copyMap = copyMap;
		
	}
	
	public Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> getCopyMap() {
		
		return this.copyMap;
		
	}

}