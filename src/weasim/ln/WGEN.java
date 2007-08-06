package weasim.ln;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinTable;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import weasim.cdc.DEL;
import weasim.cdc.MV;
import weasim.cdc.STV;
import weasim.cdc.TMS;
import weasim.cdc.WYE;
import weasim.wea.WEA;

@Entity
@NamedQuery(name = "WGEN.findAll", query = "select o from WGEN o")
@Inheritance
public class WGEN extends WPCLN implements Serializable {

	@OneToOne(mappedBy = "wgen", cascade = CascadeType.ALL)
	private WEA wea;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_OptmRs")
	private List<TMS> OptmRs;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_GnOpMod")
	private List<STV> GnOpMod;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_ClSt")
	private List<STV> ClSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_Spd")
	private List<MV> Spd;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_W")
	private List<WYE> W;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_VAr")
	private List<WYE> VAr;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_GnTmpSta")
	private List<MV> GnTmpSta;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_GnTmpRtr")
	private List<MV> GnTmpRtr;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_GnTmpInlet")
	private List<MV> GnTmpInlet;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_StaPPV")
	private List<DEL> StaPPV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_StaPhV")
	private List<WYE> StaPhV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_StaA")
	private List<WYE> StaA;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_RtrPPV")
	private List<DEL> RtrPPV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_RtrPhV")
	private List<WYE> RtrPhV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_RtrA")
	private List<WYE> RtrA;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_RtrExtDC")
	private List<MV> RtrExtDC;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WGEN_RtrExtAC")
	private List<MV> RtrExtAC;

	public WGEN() {
	}

	public List<STV> getClSt() {
		return ClSt;
	}

	public void setClSt(List<STV> clSt) {
		ClSt = clSt;
	}

	public List<STV> getGnOpMod() {
		return GnOpMod;
	}

	public void setGnOpMod(List<STV> gnOpMod) {
		GnOpMod = gnOpMod;
	}

	public List<MV> getGnTmpInlet() {
		return GnTmpInlet;
	}

	public void setGnTmpInlet(List<MV> gnTmpInlet) {
		GnTmpInlet = gnTmpInlet;
	}

	public List<MV> getGnTmpRtr() {
		return GnTmpRtr;
	}

	public void setGnTmpRtr(List<MV> gnTmpRtr) {
		GnTmpRtr = gnTmpRtr;
	}

	public List<MV> getGnTmpSta() {
		return GnTmpSta;
	}

	public void setGnTmpSta(List<MV> gnTmpSta) {
		GnTmpSta = gnTmpSta;
	}

	public List<TMS> getOptmRs() {
		return OptmRs;
	}

	public void setOptmRs(List<TMS> optmRs) {
		OptmRs = optmRs;
	}

	public List<WYE> getRtrA() {
		return RtrA;
	}

	public void setRtrA(List<WYE> rtrA) {
		RtrA = rtrA;
	}

	public List<MV> getRtrExtAC() {
		return RtrExtAC;
	}

	public void setRtrExtAC(List<MV> rtrExtAC) {
		RtrExtAC = rtrExtAC;
	}

	public List<MV> getRtrExtDC() {
		return RtrExtDC;
	}

	public void setRtrExtDC(List<MV> rtrExtDC) {
		RtrExtDC = rtrExtDC;
	}

	public List<WYE> getRtrPhV() {
		return RtrPhV;
	}

	public void setRtrPhV(List<WYE> rtrPhV) {
		RtrPhV = rtrPhV;
	}

	public List<DEL> getRtrPPV() {
		return RtrPPV;
	}

	public void setRtrPPV(List<DEL> rtrPPV) {
		RtrPPV = rtrPPV;
	}

	public List<MV> getSpd() {
		return Spd;
	}

	public void setSpd(List<MV> spd) {
		Spd = spd;
	}

	public List<WYE> getStaA() {
		return StaA;
	}

	public void setStaA(List<WYE> staA) {
		StaA = staA;
	}

	public List<WYE> getStaPhV() {
		return StaPhV;
	}

	public void setStaPhV(List<WYE> staPhV) {
		StaPhV = staPhV;
	}

	public List<DEL> getStaPPV() {
		return StaPPV;
	}

	public void setStaPPV(List<DEL> staPPV) {
		StaPPV = staPPV;
	}

	public List<WYE> getVAr() {
		return VAr;
	}

	public void setVAr(List<WYE> ar) {
		VAr = ar;
	}

	public List<WYE> getW() {
		return W;
	}

	public void setW(List<WYE> w) {
		W = w;
	}

	public WEA getWea() {
		return wea;
	}
}
