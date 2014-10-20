package de.uniol.inf.is.odysseus.core.server.incubation.physicaloperator;

import java.util.ArrayDeque;
import java.util.Deque;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * Physical implementation of the combine operator
 * 
 * @author Dennis Nowak
 *
 * @param <M>
 */
public class CombinePO<M extends ITimeInterval> extends
		AbstractPipe<Tuple<M>, Tuple<M>> {

	private final int ports;
	private final int tupleSize;
	private boolean waitForEachChanged;
	private boolean bufferIncoming;

	private Tuple<M>[] tuples;
	private boolean[] changed;
	private Deque<Tuple<M>>[] queues;

	/**
	 * Creates a new instance of CombinePO
	 * @param waitForEachChanged if true, output is only transfered after each input port received at least one tuple
	 * @param ports number of input ports
	 * @param tupleSize number of input tuple size
	 * @param bufferIncoming if true, input will be buffered
	 */
	public CombinePO(boolean waitForEachChanged, int ports, int tupleSize,
			boolean bufferIncoming) {
		this.waitForEachChanged = waitForEachChanged;
		this.ports = ports;
		this.tupleSize = tupleSize;
		this.bufferIncoming = bufferIncoming;
	}

	/**
	 * Copy constructor of Class CombinePO
	 * @param cpo the CombinePO to copy
	 */
	public CombinePO(CombinePO<M> cpo) {
		this.waitForEachChanged = cpo.waitForEachChanged;
		this.ports = cpo.ports;
		this.tupleSize = cpo.tupleSize;
		this.bufferIncoming = cpo.bufferIncoming;
	}

	@Override
	protected CombinePO<M> clone() {
		return new CombinePO<M>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<M> tuple, int port) {
		synchronized (this.changed) {
			if (this.bufferIncoming && this.changed[port]) {
				this.queues[port].add(tuple);
			} else {
				for (int i = 0; i < this.ports; i++) {
					this.tuples[i].setAttribute(port, tuple.getAttribute(i));
				}
				this.changed[port] = true;
				if (!this.waitForEachChanged) {
					for (int i = 0; i < this.tuples.length; i++) {
						Tuple<M> out = new Tuple<M>(this.tuples[i]);
						out.setMetadata(tuple.getMetadata());
						transfer(out, i);
					}
				} else if (allTrue(this.changed)) {
					for (int i = 0; i < this.tuples.length; i++) {
						Tuple<M> out = new Tuple<M>(this.tuples[i]);
						out.setMetadata(tuple.getMetadata());
						transfer(out, i);
					}
					for (int i = 0; i < this.tupleSize; i++) {
						if(this.bufferIncoming && this.queues[i].peek() != null) {
							for (int j = 0; j < this.ports; j++) {
								this.tuples[j].setAttribute(port, tuple.getAttribute(j));
								this.changed[i] = true;
							}
						} else {
							this.changed[i] = false;
						}
					}
				}
			}
		}
	}

	/**
	 * Returns true if all parts of the given array equal true
	 * @param array the array to check
	 * @return true, if all elemets of the array equal true
	 */
	private boolean allTrue(boolean[] array) {
		for (boolean bool : array) {
			if (bool == false)
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_open()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.changed = new boolean[this.tupleSize];
		this.tuples = new Tuple[this.ports];
		for (int i = 0; i < this.ports; i++) {
			this.tuples[i] = new Tuple<M>(tupleSize, true);
		}
		if (bufferIncoming) {
			this.queues = new ArrayDeque[this.tupleSize];
			for (int i = 0; i < this.tupleSize; i++) {
				this.queues[i] = new ArrayDeque<Tuple<M>>();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_close()
	 */
	@Override
	protected void process_close() {
		super.process_close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * processPunctuation
	 * (de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation, int)
	 */
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO clean up queues
		super.processPunctuation(punctuation, port);
	}

}
