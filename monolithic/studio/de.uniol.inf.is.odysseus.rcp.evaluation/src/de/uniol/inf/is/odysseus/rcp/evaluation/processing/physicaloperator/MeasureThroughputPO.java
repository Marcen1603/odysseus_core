package de.uniol.inf.is.odysseus.rcp.evaluation.processing.physicaloperator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.util.FileUtils;

public class MeasureThroughputPO<T extends IStreamObject<? extends IMetaAttribute>> extends AbstractPipe<T, T> {

	private int each;
	private String filename;
	private boolean active;
	private boolean dump;
	private ArrayList<Integer> counts = new ArrayList<>();
	private ArrayList<Long> times = new ArrayList<>();
	private int counter;
	// private File file;
	private long totalstart;
	private File file;

	public MeasureThroughputPO(int each, String filename, boolean active, boolean dump) {
		this.each = each;
		this.filename = filename;
		this.active = active;
		this.dump = dump;
	}

	public MeasureThroughputPO(MeasureThroughputPO<T> po) {
		this.each = po.each;
		this.filename = po.filename;
		this.active = po.active;
		this.dump = po.dump;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		totalstart = System.currentTimeMillis();
		try {
			this.file = FileUtils.openOrCreateFile(this.filename);
		} catch (IOException e) {
			throw new OpenFailedException(e);
		}
		this.counter = 0;
		this.times.clear();
		this.counts.clear();

	}

	@Override
	protected void process_close() {
		try {

//			System.out.println("Writing throughputs to " + this.file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.file));
//			int count = 0;
			for (int i = 0; i < counts.size(); i++) {
				Integer c = counts.get(i);
				Long time = times.get(i);
				String line = c + ";" + time + System.lineSeparator();				
				bw.write(line);
//				count++;
			}
//			System.out.println(count + " lines were written!");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void process_next(T object, int port) {
		if (active) {

			counter++;
			if (counter % each == 0) {
				times.add(System.currentTimeMillis() - totalstart);
				counts.add(counter);
			}
		}

		transfer(object);
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo instanceof MeasureThroughputPO) {
			return this.getSubscribedToSource().containsAll(((MeasureThroughputPO<?>) ipo).getSubscribedToSource());
		}
		return super.isSemanticallyEqual(ipo);
	}
}
