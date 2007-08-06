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

import weasim.cdc.MV;
import weasim.cdc.STV;
import weasim.wea.WEA;

@Entity
@NamedQuery(name = "WTRM.findAll", query = "select o from WTRM o")
@Inheritance
public class WTRM extends WPCLN implements Serializable {

	@OneToOne(mappedBy = "wtrm", cascade = CascadeType.ALL)
	private WEA wea;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_BrkOpMod")
	private List<STV> BrkOpMod;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_LuSt")
	private List<STV> LuSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_ClSt")
	private List<STV> ClSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_HtSt")
	private List<STV> HtSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_OilLevSt")
	private List<STV> OilLevSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_OfFltSt")
	private List<STV> OfFltSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_InlFltSt")
	private List<STV> InlFltSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_TrmTmpShfBrg1")
	private List<MV> TrmTmpShfBrg1;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_TrmTmpShfBrg2")
	private List<MV> TrmTmpShfBrg2;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_TrmTmpGbxOil")
	private List<MV> TrmTmpGbxOil;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_TrmTmpShfBrk")
	private List<MV> TrmTmpShfBrk;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_VibGbx1")
	private List<MV> VibGbx1;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_VibGbx2")
	private List<MV> VibGbx2;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_GsLev")
	private List<MV> GsLev;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_GbxOilLev")
	private List<MV> GbxOilLev;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_GbxOilPres")
	private List<MV> GbxOilPres;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_BrkHyPres")
	private List<MV> BrkHyPres;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_OfFlt")
	private List<MV> OfFlt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTRM_InlFlt")
	private List<MV> InlFlt;

	public WTRM() {
	}

	public List<MV> getBrkHyPres() {
		return BrkHyPres;
	}

	public void setBrkHyPres(List<MV> brkHyPres) {
		BrkHyPres = brkHyPres;
	}

	public List<STV> getBrkOpMod() {
		return BrkOpMod;
	}

	public void setBrkOpMod(List<STV> brkOpMod) {
		BrkOpMod = brkOpMod;
	}

	public List<STV> getClSt() {
		return ClSt;
	}

	public void setClSt(List<STV> clSt) {
		ClSt = clSt;
	}

	public List<MV> getGbxOilLev() {
		return GbxOilLev;
	}

	public void setGbxOilLev(List<MV> gbxOilLev) {
		GbxOilLev = gbxOilLev;
	}

	public List<MV> getGbxOilPres() {
		return GbxOilPres;
	}

	public void setGbxOilPres(List<MV> gbxOilPres) {
		GbxOilPres = gbxOilPres;
	}

	public List<MV> getGsLev() {
		return GsLev;
	}

	public void setGsLev(List<MV> gsLev) {
		GsLev = gsLev;
	}

	public List<STV> getHtSt() {
		return HtSt;
	}

	public void setHtSt(List<STV> htSt) {
		HtSt = htSt;
	}

	public List<MV> getInlFlt() {
		return InlFlt;
	}

	public void setInlFlt(List<MV> inlFlt) {
		InlFlt = inlFlt;
	}

	public List<STV> getInlFltSt() {
		return InlFltSt;
	}

	public void setInlFltSt(List<STV> inlFltSt) {
		InlFltSt = inlFltSt;
	}

	public List<STV> getLuSt() {
		return LuSt;
	}

	public void setLuSt(List<STV> luSt) {
		LuSt = luSt;
	}

	public List<MV> getOfFlt() {
		return OfFlt;
	}

	public void setOfFlt(List<MV> ofFlt) {
		OfFlt = ofFlt;
	}

	public List<STV> getOfFltSt() {
		return OfFltSt;
	}

	public void setOfFltSt(List<STV> ofFltSt) {
		OfFltSt = ofFltSt;
	}

	public List<STV> getOilLevSt() {
		return OilLevSt;
	}

	public void setOilLevSt(List<STV> oilLevSt) {
		OilLevSt = oilLevSt;
	}

	public List<MV> getTrmTmpGbxOil() {
		return TrmTmpGbxOil;
	}

	public void setTrmTmpGbxOil(List<MV> trmTmpGbxOil) {
		TrmTmpGbxOil = trmTmpGbxOil;
	}

	public List<MV> getTrmTmpShfBrg1() {
		return TrmTmpShfBrg1;
	}

	public void setTrmTmpShfBrg1(List<MV> trmTmpShfBrg1) {
		TrmTmpShfBrg1 = trmTmpShfBrg1;
	}

	public List<MV> getTrmTmpShfBrg2() {
		return TrmTmpShfBrg2;
	}

	public void setTrmTmpShfBrg2(List<MV> trmTmpShfBrg2) {
		TrmTmpShfBrg2 = trmTmpShfBrg2;
	}

	public List<MV> getTrmTmpShfBrk() {
		return TrmTmpShfBrk;
	}

	public void setTrmTmpShfBrk(List<MV> trmTmpShfBrk) {
		TrmTmpShfBrk = trmTmpShfBrk;
	}

	public List<MV> getVibGbx1() {
		return VibGbx1;
	}

	public void setVibGbx1(List<MV> vibGbx1) {
		VibGbx1 = vibGbx1;
	}

	public List<MV> getVibGbx2() {
		return VibGbx2;
	}

	public void setVibGbx2(List<MV> vibGbx2) {
		VibGbx2 = vibGbx2;
	}

	public WEA getWea() {
		return wea;
	}

}
