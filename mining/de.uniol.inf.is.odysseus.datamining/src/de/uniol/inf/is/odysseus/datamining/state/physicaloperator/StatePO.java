package de.uniol.inf.is.odysseus.datamining.state.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class StatePO<T extends IMetaAttribute> extends AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	private boolean open = true;
	private List<RelationalTuple<T>> buffer = new ArrayList<RelationalTuple<T>>();
	private int count = 0;
	private int openAt = 10000;

	public StatePO() {		
	}

	public StatePO(boolean open) {
		this.open = open;
	}
	
	public StatePO(boolean open, int openAt){
		this.open = open;
		this.openAt = openAt;
	}

	public StatePO(StatePO<T> statePO) {
		this.open = statePO.open;
		this.openAt = statePO.openAt;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(RelationalTuple<T> object, int port) {
		if (port == 1) {
			count++;
			if(count%2500==0){
				System.out.println("aktueller count: "+count);
			}
			if (count == openAt) {
				System.out.println("OpenAt reached: "+count);
				switchState();
				// now open?
				if (this.open) {
					for (RelationalTuple<T> o : this.buffer) {
						transfer(o);
					}
				}
			}
		} else {
			if (this.open) {
				transfer(object, port);
			} else {
				this.buffer.add(object);
			}
		}

	}

	private synchronized void switchState() {
		this.open = !this.open;
		System.out.println("StatePO switched! Is open?: " + this.open);
	}

	@Override
	public StatePO<T> clone() {
		return new StatePO<T>(this);
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public int getOpenAt() {
		return openAt;
	}

	public void setOpenAt(int openAt) {
		this.openAt = openAt;
	}
}
