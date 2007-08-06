package mg.dynaquest.history;

/**
 * @author  Marco Grawunder
 */
public class DataDeliveryBehaviour {

	public static int CONSTANT_DELIVERY = 1;

	public static int BURSTY_DELIVERY = 1;

	/**
	 * @uml.property  name="quellenURI"
	 */
	String quellenURI = null;

	/**
	 * @uml.property  name="tag"
	 */
	int tag = -1;

	/**
	 * @uml.property  name="monat"
	 */
	int monat = -1;

	/**
	 * @uml.property  name="wochentag"
	 */
	int wochentag = -1;

	/**
	 * @uml.property  name="stunde"
	 */
	int stunde = -1;

	/**
	 * @uml.property  name="minute"
	 */
	int minute = -1;

	/**
	 * @uml.property  name="kw"
	 */
	int kw = -1;

	/**
	 * @uml.property  name="deliveryType"
	 */
	int deliveryType = -1;

	/**
	 * @uml.property  name="datarate"
	 */
	float datarate = -1;

	public DataDeliveryBehaviour(String quellenURI, int tag, int monat,
			int wochentag, int stunde, int minute, int kw, int deliveryType,
			float datarate) {
		this.quellenURI = quellenURI;
		this.tag = tag;
		this.monat = monat;
		this.stunde = stunde;
		this.wochentag = wochentag;
		this.minute = minute;
		this.kw = kw;
		this.deliveryType = deliveryType;
		this.datarate = datarate;
	}

	/**
	 * @return  the quellenURI
	 * @uml.property  name="quellenURI"
	 */
	public String getQuellenURI() {
		return quellenURI;
	}

	/**
	 * @return  the tag
	 * @uml.property  name="tag"
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * @return  the monat
	 * @uml.property  name="monat"
	 */
	public int getMonat() {
		return monat;
	}

	/**
	 * @return  the wochentag
	 * @uml.property  name="wochentag"
	 */
	public int getWochentag() {
		return wochentag;
	}

	/**
	 * @return  the stunde
	 * @uml.property  name="stunde"
	 */
	public int getStunde() {
		return stunde;
	}

	/**
	 * @return  the minute
	 * @uml.property  name="minute"
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @return  the kw
	 * @uml.property  name="kw"
	 */
	public int getKw() {
		return kw;
	}

	/**
	 * @return  the deliveryType
	 * @uml.property  name="deliveryType"
	 */
	public int getDeliveryType() {
		return deliveryType;
	}

	/**
	 * @return  the datarate
	 * @uml.property  name="datarate"
	 */
	public float getDatarate() {
		return datarate;
	}

	public String toString() {
		String ret = this.getQuellenURI() + " Tag " + this.getTag() + " Monat "
				+ this.getMonat() + " Wochentag " + this.getWochentag()
				+ " Stunde " + this.getStunde() + " Minute " + this.getStunde()
				+ " Minute " + this.getMinute() + " KW " + this.getKw()
				+ " Type " + this.getDeliveryType() + " Rate "
				+ this.getDatarate();
		return ret;
	}
}