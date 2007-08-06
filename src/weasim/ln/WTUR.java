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

import weasim.cdc.BCR;
import weasim.cdc.CMD;
import weasim.cdc.CTE;
import weasim.cdc.MV;
import weasim.cdc.SPV;
import weasim.cdc.STV;
import weasim.cdc.TMS;
import weasim.wea.WEA;

@Entity
@NamedQuery(name = "WTUR.findAll", query = "select o from WTUR o")
@Inheritance
public class WTUR extends WPCLN implements Serializable {
	@OneToOne(mappedBy = "wtur", cascade = CascadeType.ALL)
	private WEA wea;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_AvlTmRs")
	private List<TMS> AvlTmRs;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_OpTmRs")
	private List<TMS> OpTmRs;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_StrCnt")
	private List<CTE> StrCnt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_StopCnt")
	private List<CTE> StopCnt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_TotWh")
	private List<CTE> TotWh;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_TotArh")
	private List<CTE> TotArh;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_DmdWh")
	private List<BCR> DmdWh;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_DmDVArh")
	private List<BCR> DmDVArh;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_SupWh")
	private List<BCR> SupWh;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_SupVArh")
	private List<BCR> SupVArh;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_TurSt")
	private List<STV> TurSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_W")
	private List<MV> W;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_VAr")
	private List<MV> VAr;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_SetTurOp")
	private List<CMD> SetTurOp;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_VArOvW")
	private List<CMD> VArOvW;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_VArRefPri")
	private List<CMD> VArRefPri;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_DmdW")
	private List<SPV> DmdW;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_DmdVAr")
	private List<SPV> DmdVAr;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WTUR_DmdPF")
	private List<SPV> DmdPF;

	public WTUR() {
	}

	public List<TMS> getAvlTmRs() {
		return AvlTmRs;
	}

	public void setAvlTmRs(List<TMS> avlTmRs) {
		AvlTmRs = avlTmRs;
	}

	public List<SPV> getDmdPF() {
		return DmdPF;
	}

	public void setDmdPF(List<SPV> dmdPF) {
		DmdPF = dmdPF;
	}

	public List<SPV> getDmdVAr() {
		return DmdVAr;
	}

	public void setDmdVAr(List<SPV> dmdVAr) {
		DmdVAr = dmdVAr;
	}

	public List<BCR> getDmDVArh() {
		return DmDVArh;
	}

	public void setDmDVArh(List<BCR> dmDVArh) {
		DmDVArh = dmDVArh;
	}

	public List<SPV> getDmdW() {
		return DmdW;
	}

	public void setDmdW(List<SPV> dmdW) {
		DmdW = dmdW;
	}

	public List<BCR> getDmdWh() {
		return DmdWh;
	}

	public void setDmdWh(List<BCR> dmdWh) {
		DmdWh = dmdWh;
	}

	public List<TMS> getOpTmRs() {
		return OpTmRs;
	}

	public void setOpTmRs(List<TMS> opTmRs) {
		OpTmRs = opTmRs;
	}

	public List<CMD> getSetTurOp() {
		return SetTurOp;
	}

	public void setSetTurOp(List<CMD> setTurOp) {
		SetTurOp = setTurOp;
	}

	public List<CTE> getStopCnt() {
		return StopCnt;
	}

	public void setStopCnt(List<CTE> stopCnt) {
		StopCnt = stopCnt;
	}

	public List<CTE> getStrCnt() {
		return StrCnt;
	}

	public void setStrCnt(List<CTE> strCnt) {
		StrCnt = strCnt;
	}

	public List<BCR> getSupVArh() {
		return SupVArh;
	}

	public void setSupVArh(List<BCR> supVArh) {
		SupVArh = supVArh;
	}

	public List<BCR> getSupWh() {
		return SupWh;
	}

	public void setSupWh(List<BCR> supWh) {
		SupWh = supWh;
	}

	public List<CTE> getTotArh() {
		return TotArh;
	}

	public void setTotArh(List<CTE> totArh) {
		TotArh = totArh;
	}

	public List<CTE> getTotWh() {
		return TotWh;
	}

	public void setTotWh(List<CTE> totWh) {
		TotWh = totWh;
	}

	public List<STV> getTurSt() {
		return TurSt;
	}

	public void setTurSt(List<STV> turSt) {
		TurSt = turSt;
	}

	public List<MV> getVAr() {
		return VAr;
	}

	public void setVAr(List<MV> ar) {
		VAr = ar;
	}

	public List<CMD> getVArOvW() {
		return VArOvW;
	}

	public void setVArOvW(List<CMD> arOvW) {
		VArOvW = arOvW;
	}

	public List<CMD> getVArRefPri() {
		return VArRefPri;
	}

	public void setVArRefPri(List<CMD> arRefPri) {
		VArRefPri = arRefPri;
	}

	public List<MV> getW() {
		return W;
	}

	public void setW(List<MV> w) {
		W = w;
	}

	public WEA getWea() {
		return wea;
	}

	public void setWea(WEA wea) {
		this.wea = wea;
	}

}
