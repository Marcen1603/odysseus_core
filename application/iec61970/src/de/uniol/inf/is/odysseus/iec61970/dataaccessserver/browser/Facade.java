package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.browser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication.Server;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IAggregate;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IFacade;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IItem;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IItemAttribute;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.INode;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IProperty;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IType;

/**
 * Eine Facade zu den einzelnen Zugriffsobjekten von HSDA/TSDA . Ein Client hat ausschließlich über diese Facade Zugriff auf das jeweilige Datenmodell
 * @author Mart Köhler
 *
 */
public class Facade extends UnicastRemoteObject implements IFacade{
	/**
	 * 
	 */
	private static final long serialVersionUID = -393806112254469669L;
	private String mode;
	private IItem item;
	private INode node;
	private IProperty property;
	private IType type;
	private IItemAttribute itemAttribute = null;
	private IAggregate aggregate = null;
	
	public Facade() throws RemoteException{
		super();
		if(Server.hMode) {
			this.mode = "hsda";
		}
		else {
			this.mode = "tsda";
		}
		this.item = new Item();
		this.node = new Node();
		this.property = new Property();
		this.type = new Type();
		if(!Server.hMode) {
			this.aggregate = new Aggregate();
			this.itemAttribute = new ItemAttribute();
		}
	}

	@Override
	public IItem getItem() throws RemoteException {
		return this.item;
	}

	@Override
	public INode getNode() throws RemoteException {
		return this.node;
	}

	@Override
	public IProperty getProperty() throws RemoteException {
		return this.property;
	}

	@Override
	public IType getType() throws RemoteException {
		return this.type;
	}

	@Override
	public IAggregate getAggregate() throws RemoteException {
		return aggregate;
	}

	@Override
	public IItemAttribute getItemAttribute() throws RemoteException {
		return itemAttribute;
	}

	@Override
	public String getMode() throws RemoteException {
		return this.mode;
	}
}
