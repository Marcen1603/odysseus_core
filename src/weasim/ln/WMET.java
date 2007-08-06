package weasim.ln;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import weasim.cdc.MV;
import weasim.wea.WEA;

@Entity
@Inheritance
public class WMET extends WPCLN implements Serializable {
	@OneToOne(mappedBy = "wmet", cascade = CascadeType.ALL)
	private WEA wea;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WMET_MetAlt1Alt")
	private List<MV> MetAlt1Alt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WMET_MetAlt1HorWdSpd")
	private List<MV> MetAlt1HorWdSpd;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WMET_MetAlt1VerWdSpd")
	private List<MV> MetAlt1VerWdSpd;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WMET_MetAlt1HorWdDir")
	private List<MV> MetAlt1HorWdDir;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WMET_MetAlt1VerWdDir")
	private List<MV> MetAlt1VerWdDir;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WMET_MetAlt1Tmp")
	private List<MV> MetAlt1Tmp;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WMET_MetAlt1Hum")
	private List<MV> MetAlt1Hum;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WMET_MetAlt1Pres")
	private List<MV> MetAlt1Pres;

	public List<MV> getMetAlt1Alt() {
		return MetAlt1Alt;
	}

	public void setMetAlt1Alt(List<MV> metAlt1Alt) {
		MetAlt1Alt = metAlt1Alt;
	}

	public List<MV> getMetAlt1HorWdDir() {
		return MetAlt1HorWdDir;
	}

	public void setMetAlt1HorWdDir(List<MV> metAlt1HorWdDir) {
		MetAlt1HorWdDir = metAlt1HorWdDir;
	}

	public List<MV> getMetAlt1HorWdSpd() {
		return MetAlt1HorWdSpd;
	}

	public void setMetAlt1HorWdSpd(List<MV> metAlt1HorWdSpd) {
		MetAlt1HorWdSpd = metAlt1HorWdSpd;
	}

	public List<MV> getMetAlt1Hum() {
		return MetAlt1Hum;
	}

	public void setMetAlt1Hum(List<MV> metAlt1Hum) {
		MetAlt1Hum = metAlt1Hum;
	}

	public List<MV> getMetAlt1Pres() {
		return MetAlt1Pres;
	}

	public void setMetAlt1Pres(List<MV> metAlt1Pres) {
		MetAlt1Pres = metAlt1Pres;
	}

	public List<MV> getMetAlt1Tmp() {
		return MetAlt1Tmp;
	}

	public void setMetAlt1Tmp(List<MV> metAlt1Tmp) {
		MetAlt1Tmp = metAlt1Tmp;
	}

	public List<MV> getMetAlt1VerWdDir() {
		return MetAlt1VerWdDir;
	}

	public void setMetAlt1VerWdDir(List<MV> metAlt1VerWdDir) {
		MetAlt1VerWdDir = metAlt1VerWdDir;
	}

	public List<MV> getMetAlt1VerWdSpd() {
		return MetAlt1VerWdSpd;
	}

	public void setMetAlt1VerWdSpd(List<MV> metAlt1VerWdSpd) {
		MetAlt1VerWdSpd = metAlt1VerWdSpd;
	}

	public WEA getWea() {
		return wea;
	}
}
