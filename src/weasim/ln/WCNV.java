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
@NamedQuery(name = "WCNV.findAll", query = "select o from WCNV o")
@Inheritance
public class WCNV extends WPCLN implements Serializable {
	@OneToOne(mappedBy = "wcnv", cascade = CascadeType.ALL)
	private WEA wea;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_OpTmRs")
	private List<TMS> OpTmRs;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_CnvOpMod")
	private List<STV> CnvOpMod;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_ClSt")
	private List<STV> ClSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_Hz")
	private List<MV> Hz;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_Torq")
	private List<MV> Torq;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_GnPPV")
	private List<DEL> GnPPV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_GnPhV")
	private List<WYE> GnPhV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_GnA")
	private List<WYE> GnA;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_GnPF")
	private List<WYE> GnPF;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_GriPPV")
	private List<DEL> GriPPV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_GriPhV")
	private List<WYE> GriPhV;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_GriA")
	private List<WYE> GriA;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_GriPF")
	private List<WYE> GriPF;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_CnvTmpGn")
	private List<MV> CnvTmpGn;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_CnvTmpDclink")
	private List<MV> CnvTmpDclink;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_CnvTmpGri")
	private List<MV> CnvTmpGri;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_DclVol")
	private List<MV> DclVol;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WCNV_DclAmp")
	private List<MV> DclAmp;

	public WCNV() {
	}
	
	@Override
	public WEA getWea() {
		return this.wea;
	}

	public List<STV> getClSt() {
		return ClSt;
	}

	public void setClSt(List<STV> clSt) {
		ClSt = clSt;
	}

	public List<STV> getCnvOpMod() {
		return CnvOpMod;
	}

	public void setCnvOpMod(List<STV> cnvOpMod) {
		CnvOpMod = cnvOpMod;
	}

	public List<MV> getCnvTmpDclink() {
		return CnvTmpDclink;
	}

	public void setCnvTmpDclink(List<MV> cnvTmpDclink) {
		CnvTmpDclink = cnvTmpDclink;
	}

	public List<MV> getCnvTmpGn() {
		return CnvTmpGn;
	}

	public void setCnvTmpGn(List<MV> cnvTmpGn) {
		CnvTmpGn = cnvTmpGn;
	}

	public List<MV> getCnvTmpGri() {
		return CnvTmpGri;
	}

	public void setCnvTmpGri(List<MV> cnvTmpGri) {
		CnvTmpGri = cnvTmpGri;
	}

	public List<MV> getDclAmp() {
		return DclAmp;
	}

	public void setDclAmp(List<MV> dclAmp) {
		DclAmp = dclAmp;
	}

	public List<MV> getDclVol() {
		return DclVol;
	}

	public void setDclVol(List<MV> dclVol) {
		DclVol = dclVol;
	}

	public List<WYE> getGnA() {
		return GnA;
	}

	public void setGnA(List<WYE> gnA) {
		GnA = gnA;
	}

	public List<WYE> getGnPF() {
		return GnPF;
	}

	public void setGnPF(List<WYE> gnPF) {
		GnPF = gnPF;
	}

	public List<WYE> getGnPhV() {
		return GnPhV;
	}

	public void setGnPhV(List<WYE> gnPhV) {
		GnPhV = gnPhV;
	}

	public List<DEL> getGnPPV() {
		return GnPPV;
	}

	public void setGnPPV(List<DEL> gnPPV) {
		GnPPV = gnPPV;
	}

	public List<WYE> getGriA() {
		return GriA;
	}

	public void setGriA(List<WYE> griA) {
		GriA = griA;
	}

	public List<WYE> getGriPF() {
		return GriPF;
	}

	public void setGriPF(List<WYE> griPF) {
		GriPF = griPF;
	}

	public List<WYE> getGriPhV() {
		return GriPhV;
	}

	public void setGriPhV(List<WYE> griPhV) {
		GriPhV = griPhV;
	}

	public List<DEL> getGriPPV() {
		return GriPPV;
	}

	public void setGriPPV(List<DEL> griPPV) {
		GriPPV = griPPV;
	}

	public List<MV> getHz() {
		return Hz;
	}

	public void setHz(List<MV> hz) {
		Hz = hz;
	}

	public List<TMS> getOpTmRs() {
		return OpTmRs;
	}

	public void setOpTmRs(List<TMS> opTmRs) {
		OpTmRs = opTmRs;
	}

	public List<MV> getTorq() {
		return Torq;
	}

	public void setTorq(List<MV> torq) {
		Torq = torq;
	}

}
