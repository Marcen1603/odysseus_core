package de.uniol.inf.is.odysseus.scars.testdata.provider.model;


public class CarModel {

	private int type;// Fahrzeugtyp: immer Wert 4
	private int id;// alles > 0
	private int laneid; // fahrspur: maximal 6
	private float posx; // relative koordinaten (in meter?)
	private float posy;
	private float posz;
	private float roll;// kann immer 0 sein
	private float pitch;// kann immer 0 sein
	private float heading;// kann immer 0 sein
	private float velocity;// absolute geschwindigkeit in ??
	private float length; // l�nge des autos in m
	private float width; // l�nge des autos in m

	private ICalculationModel calcModel;
	
	public CarModel(int id, ICalculationModel calcModel) {
		this.type = 4;
		this.id = id;
		this.roll = 0.0f;
		this.pitch = 0.0f;
		this.heading = 0.0f;
		this.length = 4.2f;
		this.width = 1.95f;

		this.calcModel = calcModel;
		this.calcModel.setModel(this);
		this.laneid = this.calcModel.initLaneid();
		this.posx = this.calcModel.initPosx();
		this.posy = this.calcModel.initPosy();
		this.posz = this.calcModel.initPosz();
		this.velocity = this.calcModel.initVelocity();
	}
	
	public CarModel(CarModel clone) {
		this.heading = clone.heading;
		this.id = clone.id;
		this.laneid = clone.laneid;
		this.length = clone.length;
		this.pitch = clone.pitch;
		this.posx = clone.posx;
		this.posy = clone.posy;
		this.posz = clone.posz;
		this.roll = clone.roll;
		this.type = clone.type;
		this.velocity = clone.velocity;
		this.width = clone.width;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLaneid() {
		return laneid;
	}

	public void setLaneid(int laneid) {
		this.laneid = laneid;
	}

	public float getPosx() {
		return posx;
	}

	public void setPosx(float posx) {
		this.posx = posx;
	}

	public float getPosy() {
		return posy;
	}

	public void setPosy(float posy) {
		this.posy = posy;
	}

	public float getPosz() {
		return posz;
	}

	public void setPosz(float posz) {
		this.posz = posz;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public ICalculationModel getCalcModel() {
		return calcModel;
	}

	public void setCalcModel(ICalculationModel calcModel) {
		this.calcModel = calcModel;
	}
	
	/**
	 * setzt die Werte eines Autos auf die Werte eines anderen Autos
	 */
	public void setValues(CarModel other) {
		this.setType(other.getType());
		this.setId(other.getId());
		this.setLaneid(other.getLaneid());
		this.setPosx(other.getPosx());
		this.setPosy(other.getPosy());
		this.setPosz(other.getPosz());
		this.setRoll(other.getRoll());
		this.setPitch(other.getPitch());
		this.setHeading(other.getHeading());
		this.setVelocity(other.getVelocity());
		this.setWidth(other.getWidth());
		this.setLength(other.getLength());
	}
	
	public String toString() {
		String str = "Car\n";
		str += "  type:     " + this.type +"\n";
		str += "  id:       " + this.id +"\n";
		str += "  laneid:   " + this.laneid +"\n";
		str += "  x:        " + this.posx +"\n";
		str += "  y:        " + this.posy +"\n";
		str += "  z:        " + this.posz +"\n";
		str += "  roll:     " + this.roll +"\n";
		str += "  pitch:    " + this.pitch +"\n";
		str += "  heading:  " + this.heading +"\n";
		str += "  velocity: " + this.velocity +"\n";
		str += "  length:   " + this.length +"\n";
		str += "  width:    " + this.width +"\n";
		
		return str;
	}

	@Override
	protected Object clone() {
		return new CarModel(this);
	}
	
	

}
