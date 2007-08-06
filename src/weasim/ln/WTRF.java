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

import weasim.cdc.CMD;
import weasim.cdc.DEL;
import weasim.cdc.MV;
import weasim.cdc.STV;
import weasim.cdc.TMS;
import weasim.cdc.WYE;
import weasim.wea.WEA;

@Entity
@NamedQuery(name = "WTRF.findAll", query = "select o from WTRF o")
@Inheritance
public class WTRF extends WPCLN implements Serializable{
	@OneToOne(mappedBy="wtrf", cascade = CascadeType.ALL)
	private WEA wea;
	
	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfOpTmRs")
	private List<TMS> TrfOpTmRs;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfClSt")
	private List<STV> TrfClSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_OilLevSt")
	private List<STV> OilLevSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_MTPresSt")
	private List<STV> MTPresSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfTurPPV")
	private List<DEL> TrfTurPPV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfTurPhV")
	private List<WYE> TrfTurPhV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfTurA")
	private List<WYE> TrfTurA;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfGriPPV")
	private List<DEL> TrfGriPPV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfGriPhV")
	private List<WYE> TrfGriPhV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfGriA")
	private List<WYE> TrfGriA;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfTmpTrfTur")
	private List<MV> TrfTmpTrfTur;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_TrfTmpTrfGri")
	private List<MV> TrfTmpTrfGri;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WTRF_AtvGriSw")
	private List<CMD> AtvGriSw;

	public WTRF() {
	}
	
	@Override
	public WEA getWea() {
		return this.wea;
	}
	
	public List<CMD> getAtvGriSw() {
		return AtvGriSw;
	}

	public void setAtvGriSw(List<CMD> atvGriSw) {
		AtvGriSw = atvGriSw;
	}

	public List<STV> getMTPresSt() {
		return MTPresSt;
	}

	public void setMTPresSt(List<STV> presSt) {
		MTPresSt = presSt;
	}

	public List<STV> getOilLevSt() {
		return OilLevSt;
	}

	public void setOilLevSt(List<STV> oilLevSt) {
		OilLevSt = oilLevSt;
	}

	public List<STV> getTrfClSt() {
		return TrfClSt;
	}

	public void setTrfClSt(List<STV> trfClSt) {
		TrfClSt = trfClSt;
	}

	public List<WYE> getTrfGriA() {
		return TrfGriA;
	}

	public void setTrfGriA(List<WYE> trfGriA) {
		TrfGriA = trfGriA;
	}

	public List<WYE> getTrfGriPhV() {
		return TrfGriPhV;
	}

	public void setTrfGriPhV(List<WYE> trfGriPhV) {
		TrfGriPhV = trfGriPhV;
	}

	public List<DEL> getTrfGriPPV() {
		return TrfGriPPV;
	}

	public void setTrfGriPPV(List<DEL> trfGriPPV) {
		TrfGriPPV = trfGriPPV;
	}

	public List<TMS> getTrfOpTmRs() {
		return TrfOpTmRs;
	}

	public void setTrfOpTmRs(List<TMS> trfOpTmRs) {
		TrfOpTmRs = trfOpTmRs;
	}

	public List<MV> getTrfTmpTrfGri() {
		return TrfTmpTrfGri;
	}

	public void setTrfTmpTrfGri(List<MV> trfTmpTrfGri) {
		TrfTmpTrfGri = trfTmpTrfGri;
	}

	public List<MV> getTrfTmpTrfTur() {
		return TrfTmpTrfTur;
	}

	public void setTrfTmpTrfTur(List<MV> trfTmpTrfTur) {
		TrfTmpTrfTur = trfTmpTrfTur;
	}

	public List<WYE> getTrfTurA() {
		return TrfTurA;
	}

	public void setTrfTurA(List<WYE> trfTurA) {
		TrfTurA = trfTurA;
	}

	public List<WYE> getTrfTurPhV() {
		return TrfTurPhV;
	}

	public void setTrfTurPhV(List<WYE> trfTurPhV) {
		TrfTurPhV = trfTurPhV;
	}

	public List<DEL> getTrfTurPPV() {
		return TrfTurPPV;
	}

	public void setTrfTurPPV(List<DEL> trfTurPPV) {
		TrfTurPPV = trfTurPPV;
	}
}
