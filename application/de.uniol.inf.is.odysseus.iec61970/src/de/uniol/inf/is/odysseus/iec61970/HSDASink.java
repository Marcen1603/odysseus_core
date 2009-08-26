package de.uniol.inf.is.odysseus.iec61970;

import java.rmi.RemoteException;

import javax.swing.table.TableModel;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.HSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IFacade;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

/**
 * Nimmt die Daten des Operators an und propagiert diese geeignet an eine Tabelle in der GUI
 * @author Mart Köhler
 *
 * @param <T>
 */
public class HSDASink<T extends IClone>  extends AbstractSink<T> {
	private TableModel tableModel;
	private int tablerowCounter = -1;
	private IFacade facade = null;

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		HSDAObject temp = (HSDAObject) object;
		tablerowCounter++;
		if(tablerowCounter==20) {
			tablerowCounter=0;
		}
		try {
			tableModel.setValueAt(facade.getItem().getPathname(temp.getId()), tablerowCounter, 0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		tableModel.setValueAt(temp.getValue(), tablerowCounter, 1);
		tableModel.setValueAt(temp.getTimestamp(), tablerowCounter, 2);
		try {
			tableModel.setValueAt(facade.getItem().getDatatypeOf(temp.getId()), tablerowCounter, 3);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		tableModel.setValueAt(temp.getQuality(), tablerowCounter, 4);
	}
	public void setTableModel(TableModel model) {
		this.tableModel = model;
	}
	
	public void setFacade(IFacade facade) {
		this.facade = facade;
	}
}
