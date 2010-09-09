package de.uniol.inf.is.odysseus.iec61970;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.table.TableModel;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.HSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.TSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.server.service.IFacade;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class TSDASink<T extends IClone>  extends AbstractSink<T> {

	private TableModel tableModel;
	private int tablerowCounter = -1;
	private IFacade facade = null;

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		TSDAObject temp = (TSDAObject) object;
		ArrayList<HSDAObject> list = new ArrayList<HSDAObject>();
		for(int i=0; i<temp.getData().size(); i++) {
			list.add(new HSDAObject(temp.getId(),temp.getData().get(0).get(i), (String)temp.getData().get(2).get(i), (String)temp.getData().get(1).get(i)));
		}
		for(int j=0; j<list.size(); j++) {
			tablerowCounter++;
			if(tablerowCounter==20) {
				tablerowCounter=0;
			}
			try {
				tableModel.setValueAt(facade.getItem().getPathname(list.get(j).getId()), tablerowCounter, 0);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			tableModel.setValueAt(list.get(j).getValue(), tablerowCounter, 1);
			tableModel.setValueAt(list.get(j).getTimestamp(), tablerowCounter, 2);
			try {
				tableModel.setValueAt(facade.getItem().getDatatypeOf(temp.getId()), tablerowCounter, 3);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			tableModel.setValueAt(list.get(j).getQuality(), tablerowCounter, 4);
		}
	}
	public void setTableModel(TableModel model) {
		this.tableModel = model;
	}
	
	public void setFacade(IFacade facade) {
		this.facade = facade;
	}
	
	@Override
	public TSDASink<T> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}


}
