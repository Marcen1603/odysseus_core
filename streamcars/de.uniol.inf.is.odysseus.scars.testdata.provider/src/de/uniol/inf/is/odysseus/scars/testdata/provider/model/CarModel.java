package de.uniol.inf.is.odysseus.scars.testdata.provider.model;

public class CarModel {

	private Integer type;//Fahrzeugtyp: immer Wert 4
	private Integer id;// alles > 0 
	private Integer laneid; //fahrspur: maximal 6
	private Float posx; // relative koordinaten (in meter?)
	private Float posy;
	private Float posz;
	private Float roll;// kann immer 0 sein
	private Float pitch;// kann immer 0 sein
	private Float heading;// kann immer 0 sein
	private Float velocity;// absolute geschwindigkeit in ?? 
	private Float length; //länge des autos in m
	private Float width; //länge des autos in m
	
	private ICalculationModel calcModel;

	public CarModel(int id, ICalculationModel calcModel) {
		this.type = 4;
		this.id = id;
		this.laneid = 0;
		this.roll = 0.0f;
		this.pitch = 0.0f;
		this.heading = 0.0f;
		this.length = 4.2f;
		this.width = 1.95f;
		
		this.calcModel = calcModel;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLaneid() {
		return laneid;
	}

	public void setLaneid(Integer laneid) {
		this.laneid = laneid;
	}

	public Float getPosx() {
		return posx;
	}

	public void setPosx(Float posx) {
		this.posx = posx;
	}

	public Float getPosy() {
		return posy;
	}

	public void setPosy(Float posy) {
		this.posy = posy;
	}

	public Float getPosz() {
		return posz;
	}

	public void setPosz(Float posz) {
		this.posz = posz;
	}

	public Float getRoll() {
		return roll;
	}

	public void setRoll(Float roll) {
		this.roll = roll;
	}

	public Float getPitch() {
		return pitch;
	}

	public void setPitch(Float pitch) {
		this.pitch = pitch;
	}

	public Float getHeading() {
		return heading;
	}

	public void setHeading(Float heading) {
		this.heading = heading;
	}

	public Float getVelocity() {
		return velocity;
	}

	public void setVelocity(Float velocity) {
		this.velocity = velocity;
	}

	public Float getLength() {
		return length;
	}

	public void setLength(Float length) {
		this.length = length;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

}
