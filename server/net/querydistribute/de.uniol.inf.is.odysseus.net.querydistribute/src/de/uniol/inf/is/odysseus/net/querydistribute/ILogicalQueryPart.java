package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface ILogicalQueryPart {

	public void addOperator( ILogicalOperator operator );
	public void addOperators( Collection<ILogicalOperator> operators );
	
	public void removeOperator( ILogicalOperator operator );
	public void removeOperators( Collection<ILogicalOperator> operators );
	
	public ImmutableCollection<ILogicalOperator> getOperators();
	public boolean contains( ILogicalOperator operator );
	public void removeAllOperators();
	
	public void addAvoidingQueryPart( ILogicalQueryPart queryPartToAvoid );
	public void addAvoidingQueryParts( Collection<ILogicalQueryPart> queryPartsToAvoid );
	public void removeAvoidingQueryPart( ILogicalQueryPart queryPartToAvoid );
	public void removeAvoidingQueryParts( Collection<ILogicalQueryPart> queryPartsToAvoid );
	
	public ImmutableCollection<ILogicalQueryPart> getAvoidingQueryParts();
	public boolean contains( ILogicalQueryPart queryPart );
	public void removeAllAvoidingQueryParts();
}
