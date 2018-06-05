package de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.substation.model;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import de.uniol.inf.is.odysseus.generator.conditionmonitoring.offshore.windpark.model.OffshoreWindparkManager;

public class OffshoreSubstationManager implements Observer {

	private Pump pump1;
	private Pump pump2;
	private Pump pump3;

	private Pipe bigPipeIn;
	private Pipe bigPipeOut;
	private Pipe smallPipeIn1;
	private Pipe smallPipeIn2;
	private Pipe smallPipeIn3;
	private Pipe smallPipeOut1;
	private Pipe smallPipeOut2;
	private Pipe smallPipeOut3;

	private Valve valve1;
	private Valve valve2;
	private Valve valve3;

	private Transformer transformer1;

	public OffshoreSubstationManager(OffshoreWindparkManager windparkManager) {

		// The northsea
		Ocean ocean = new Ocean();

		// Valves
		this.valve1 = new Valve();
		this.valve2 = new Valve();
		this.valve3 = new Valve();

		// Input (coming from the sea)
		this.bigPipeIn = new Pipe(ocean, null, null);
		this.smallPipeIn1 = new Pipe(this.bigPipeIn, null, this.valve1);
		this.smallPipeIn2 = new Pipe(this.bigPipeIn, null, this.valve2);
		this.smallPipeIn3 = new Pipe(this.bigPipeIn, null, this.valve3);
		this.bigPipeIn.addOutPipe(smallPipeIn1);
		this.bigPipeIn.addOutPipe(smallPipeIn2);
		this.bigPipeIn.addOutPipe(smallPipeIn3);

		// Output (Going to the sea)
		this.bigPipeOut = new Pipe(null, ocean, null);
		this.smallPipeOut1 = new Pipe(null, this.bigPipeOut, null);
		this.smallPipeOut2 = new Pipe(null, this.bigPipeOut, null);
		this.smallPipeOut3 = new Pipe(null, this.bigPipeOut, null);
		this.bigPipeOut.addInPipe(smallPipeOut1);
		this.bigPipeOut.addInPipe(smallPipeOut2);
		this.bigPipeOut.addInPipe(smallPipeOut3);

		// Pumps
		this.pump1 = new Pump(1000, smallPipeIn1, smallPipeOut1);
		this.pump2 = new Pump(1000, smallPipeIn2, smallPipeOut2);
		this.pump3 = new Pump(1000, smallPipeIn3, smallPipeOut3);
		this.pump1.addObserver(this);
		this.pump2.addObserver(this);
		this.pump3.addObserver(this);

		// Transformer to be cooled
		this.transformer1 = new Transformer(bigPipeOut, windparkManager.getMaximumEnergyOutput(),
				windparkManager.getWindturbines());
	}

	public void run() {
		this.valve1.open();
		this.valve2.open();
		this.valve3.close();
		this.pump1.startPump();
		this.pump2.startPump();
		this.pump3.shutDownPump();

		// Start a timer to update the values synchronously
		long switchDelay = 60 * 1000; // 30 seconds
		long switchPeriod = switchDelay;
		Timer switchTimer = new Timer();
		switchTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				switchPump();
			}
		}, switchDelay, switchPeriod);

		long stepDelay = 1 * 1000; // 1 second
		long stepPeriod = 1 * 1000;
		Timer stepTimer = new Timer();
		stepTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				doStep();
			}
		}, stepDelay, stepPeriod);
	}

	public void switchPump() {
		if (!this.pump3.isRunning() && !this.pump3.isFailed()) {
			// switch on pump 3
			this.valve3.open();
			this.pump3.startPump();
		} else if (!this.pump1.isRunning() && !this.pump1.isFailed()) {
			// switch on pump 1
			this.valve1.open();
			this.pump1.startPump();
		} else if (!this.pump2.isRunning() && !this.pump2.isFailed()) {
			// switch on pump 2
			this.valve2.open();
			this.pump2.startPump();
		}
	}

	public void doStep() {
		this.pump1.doStep();
		this.pump2.doStep();
		this.pump3.doStep();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == this.pump1) {
			if (this.pump1.isRunning() && this.pump3.isRunning()) {
				// Pump 1 was started successfully, we can now shut down pump 2
				// The check for pump 3 is needed on startup of the whole system
				this.pump2.shutDownPump();
			} else if (!this.pump1.isRunning()) {
				// Pump 1 is shut down
				this.valve1.close();
			}
		} else if (o == this.pump2) {
			if (this.pump2.isRunning() && this.pump1.isRunning()) {
				// Pump 2 was started successfully, we can now shut down pump 3
				this.pump3.shutDownPump();
			} else if (!this.pump2.isRunning()) {
				// Pump 2 is shut down
				this.valve2.close();
			}
		} else if (o == this.pump3 && this.pump2.isRunning()) {
			if (this.pump3.isRunning()) {
				// Pump 3 was started successfully, we can now shut down pump 1
				this.pump1.shutDownPump();
			} else if(!this.pump3.isRunning()) {
				// Pump 3 is shut down
				this.valve3.close();
			}
		}
	}

	public Pump getPump1() {
		return pump1;
	}

	public Pump getPump2() {
		return pump2;
	}

	public Pump getPump3() {
		return pump3;
	}

	public Pipe getBigPipeIn() {
		return bigPipeIn;
	}

	public Pipe getBigPipeOut() {
		return bigPipeOut;
	}

	public Pipe getSmallPipeIn1() {
		return smallPipeIn1;
	}

	public Pipe getSmallPipeIn2() {
		return smallPipeIn2;
	}

	public Pipe getSmallPipeIn3() {
		return smallPipeIn3;
	}

	public Pipe getSmallPipeOut1() {
		return smallPipeOut1;
	}

	public Pipe getSmallPipeOut2() {
		return smallPipeOut2;
	}

	public Pipe getSmallPipeOut3() {
		return smallPipeOut3;
	}

	public Valve getValve1() {
		return valve1;
	}

	public Valve getValve2() {
		return valve2;
	}

	public Valve getValve3() {
		return valve3;
	}

	public Transformer getTransformer1() {
		return transformer1;
	}

}
