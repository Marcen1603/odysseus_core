package de.uniol.inf.is.odysseus.p2p_new.logicaloperator;

import java.net.URI;
import java.net.URISyntaxException;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "DISTRIBUTEDQUERYREPRESENTATIONAO", doc = "Represents a fully distributed query", minInputPorts = 0, maxInputPorts = 0, category = { LogicalOperatorCategory.TEST }, hidden = true)
public class DistributedQueryRepresentationAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	private ID sharedQueryID;

	public DistributedQueryRepresentationAO(ID sharedQueryID) {
		this.sharedQueryID = sharedQueryID;
	}
	
	public DistributedQueryRepresentationAO() {
	}

	public DistributedQueryRepresentationAO(DistributedQueryRepresentationAO copy) {
		this.sharedQueryID = copy.sharedQueryID;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DistributedQueryRepresentationAO(this);
	}

	@Parameter(name = "SHAREDQUERYIDSTRING", doc = "Shared query id which is fully distributed", type = StringParameter.class, optional = false)
	public void setSharedQueryIDString(String idString) {
		sharedQueryID = toID(idString);
	}

	public String getSharedQueryIDString() {
		return sharedQueryID.toString();
	}

	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			return null;
		}
	}

	public ID getSharedQueryID() {
		return sharedQueryID;
	}
}
