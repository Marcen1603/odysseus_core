package weasim.wea;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import weasim.ln.WCNV;
import weasim.ln.WGEN;
import weasim.ln.WMET;
import weasim.ln.WNAC;
import weasim.ln.WROT;
import weasim.ln.WTOW;
import weasim.ln.WTRF;
import weasim.ln.WTRM;
import weasim.ln.WTUR;
import weasim.ln.WYAW;

@Entity
@NamedQuery(name = "WEA.findAll", query = "select o from WEA o")
@SequenceGenerator(name = "WEASEQ", sequenceName = "WEASEQ", allocationSize = 1, initialValue = 1)
public class WEA implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WEASEQ")
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	private WCNV wcnv;

	@OneToOne(cascade = CascadeType.ALL)
	private WGEN wgen;

	@OneToOne(cascade = CascadeType.ALL)
	private WMET wmet;

	@OneToOne(cascade = CascadeType.ALL)
	private WNAC wnac;

	@OneToOne(cascade = CascadeType.ALL)
	private WROT wrot;

	@OneToOne(cascade = CascadeType.ALL)
	private WTOW wtow;

	@OneToOne(cascade = CascadeType.ALL)
	private WTRF wtrf;

	@OneToOne(cascade = CascadeType.ALL)
	private WTRM wtrm;

	@OneToOne(cascade = CascadeType.ALL)
	private WTUR wtur;

	@OneToOne(cascade = CascadeType.ALL)
	private WYAW wyaw;

	@ManyToOne
	private WindPark windPark;

	public WEA() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setWgen(WGEN param) {
		this.wgen = param;
	}

	public WGEN getWgen() {
		return wgen;
	}

	public void setWrot(WROT param) {
		this.wrot = param;
	}

	public WROT getWrot() {
		return wrot;
	}

	public void setWtow(WTOW param) {
		this.wtow = param;
	}

	public WTOW getWtow() {
		return wtow;
	}

	public void setWtrm(WTRM param) {
		this.wtrm = param;
	}

	public WTRM getWtrm() {
		return wtrm;
	}

	public void setWtur(WTUR param) {
		this.wtur = param;
	}

	public WTUR getWtur() {
		return wtur;
	}

	public void setWindPark(WindPark param) {
		this.windPark = param;
	}

	public WindPark getWindPark() {
		return windPark;
	}

	public WCNV getWcnv() {
		return wcnv;
	}

	public void setWcnv(WCNV wcnv) {
		this.wcnv = wcnv;
	}

	public WMET getWmet() {
		return wmet;
	}

	public void setWmet(WMET wmet) {
		this.wmet = wmet;
	}

	public WNAC getWnac() {
		return wnac;
	}

	public void setWnac(WNAC wnac) {
		this.wnac = wnac;
	}

	public WTRF getWtrf() {
		return wtrf;
	}

	public void setWtrf(WTRF wtrf) {
		this.wtrf = wtrf;
	}

	public WYAW getWyaw() {
		return wyaw;
	}

	public void setWyaw(WYAW wyaw) {
		this.wyaw = wyaw;
	}
}
