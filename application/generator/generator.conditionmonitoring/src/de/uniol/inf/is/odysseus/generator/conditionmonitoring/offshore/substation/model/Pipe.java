package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Pipe extends Observable implements IPushable, ISuckable {

	private List<ISuckable> inPipes;
	private List<IPushable> outPipes;
	private Valve valve;
	private double lastThroughput;
	public static final double WATER_TEMPERATURE = 20;

	public Pipe() {
		this.inPipes = new ArrayList<>();
		this.outPipes = new ArrayList<>();
		startReportTimer();
	}

	public Pipe(ISuckable inPipe, IPushable outPipe, Valve valve) {
		this.inPipes = new ArrayList<>();
		this.outPipes = new ArrayList<>();
		this.inPipes.add(inPipe);
		this.outPipes.add(outPipe);
		this.valve = valve;
		startReportTimer();
	}

	public Pipe(List<ISuckable> inPipes, List<IPushable> outPipes, Valve valve) {
		this.inPipes = new ArrayList<>();
		this.outPipes = new ArrayList<>();
		this.inPipes.addAll(inPipes);
		this.outPipes.addAll(outPipes);
		this.valve = valve;
		startReportTimer();
	}
	
	private void startReportTimer() {
		long startUpDelay = 0; // 0 seconds
		long stepDelay = 1 * 1000; // 1 seconds
		final Timer reportTimerTimer = new Timer();
		reportTimerTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// Notify observers about this flow (maybe a transformer is cooled by
				// this pipe)
				setChanged();
				notifyObservers(lastThroughput);
				lastThroughput = 0;
			}
		}, startUpDelay, stepDelay);
	}

	public void addInPipe(Pipe inPipe) {
		inPipes.add(inPipe);
	}

	public void addOutPipe(Pipe outPipe) {
		outPipes.add(outPipe);
	}

	public void setValve(Valve valve) {
		this.valve = valve;
	}

	public boolean isValveOpen() {
		if (valve != null)
			return this.valve.isOpen();
		return true;
	}

	@Override
	public boolean isInPipeFlowOpen() {
		for (ISuckable inPipe : inPipes) {
			if (!inPipe.isInPipeFlowOpen()) {
				return false;
			}
		}
		return this.isValveOpen();
	}

	@Override
	public boolean isOutPipeFlowOpen() {
		for (IPushable outPipe : outPipes) {
			if (!outPipe.isOutPipeFlowOpen()) {
				return false;
			}
		}
		return this.isValveOpen();
	}

	@Override
	public double suck(double liters) {
		double suckedLiters = 0;
		if (this.isInPipeFlowOpen()) {
			// Count open input pipes
			int counter = 0;
			for (ISuckable inPipe : inPipes) {
				if (inPipe.isInPipeFlowOpen())
					counter++;
			}

			if (counter > 0) {
				for (ISuckable inPipe : inPipes) {
					suckedLiters += inPipe.suck(liters / counter);
				}
			}
		}
		this.lastThroughput += suckedLiters;

		return suckedLiters;
	}

	@Override
	public double push(double liters) {
		double pushedLiters = 0;
		if (this.isOutPipeFlowOpen()) {
			// Count open output pipes
			int counter = 0;
			for (IPushable outPipe : outPipes) {
				if (outPipe.isOutPipeFlowOpen())
					counter++;
			}

			if (counter > 0) {
				for (IPushable outPipe : outPipes) {
					pushedLiters += outPipe.push(liters / counter);
				}
			}
		}
		this.lastThroughput += pushedLiters;

		return pushedLiters;
	}

}
