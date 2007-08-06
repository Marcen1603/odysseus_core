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
import weasim.cdc.MV;
import weasim.cdc.STV;
import weasim.wea.WEA;

@Entity
@NamedQuery(name = "WROT.findAll", query = "select o from WROT o")
@Inheritance
public class WROT extends WPCLN implements Serializable {

	@OneToOne(mappedBy = "wrot", cascade = CascadeType.ALL)
	private WEA wea;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_RotSt")
	private List<STV> RotSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_BlStBl1")
	private List<STV> BlStBl1;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_BlStBl2")
	private List<STV> BlStBl2;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_BlStBl3")
	private List<STV> BlStBl3;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtCtlSt")
	private List<STV> PtCtlSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_RotSpd")
	private List<MV> RotSpd;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_RotPos")
	private List<MV> RotPos;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_HubTmp")
	private List<MV> HubTmp;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtHyPresBl1")
	private List<MV> PtHyPresBl1;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtHyPresBl2")
	private List<MV> PtHyPresBl2;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtHyPresBl3")
	private List<MV> PtHyPresBl3;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtAngSpBl1")
	private List<MV> PtAngSpBl1;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtAngSpBl2")
	private List<MV> PtAngSpBl2;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtAngSpBl3")
	private List<MV> PtAngSpBl3;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtAngValBl1")
	private List<MV> PtAngValBl1;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtAngValBl2")
	private List<MV> PtAngValBl2;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtAngValBl3")
	private List<MV> PtAngValBl3;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_BlkRot")
	private List<CMD> BlkRot;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name="WROT_PtEmgChk")
	private List<CMD> PtEmgChk;

	public WROT() {
	}

	public List<CMD> getBlkRot() {
		return BlkRot;
	}

	public void setBlkRot(List<CMD> blkRot) {
		BlkRot = blkRot;
	}

	public List<STV> getBlStBl1() {
		return BlStBl1;
	}

	public void setBlStBl1(List<STV> blStBl1) {
		BlStBl1 = blStBl1;
	}

	public List<STV> getBlStBl2() {
		return BlStBl2;
	}

	public void setBlStBl2(List<STV> blStBl2) {
		BlStBl2 = blStBl2;
	}

	public List<STV> getBlStBl3() {
		return BlStBl3;
	}

	public void setBlStBl3(List<STV> blStBl3) {
		BlStBl3 = blStBl3;
	}

	public List<MV> getHubTmp() {
		return HubTmp;
	}

	public void setHubTmp(List<MV> hubTmp) {
		HubTmp = hubTmp;
	}

	public List<MV> getPtAngSpBl1() {
		return PtAngSpBl1;
	}

	public void setPtAngSpBl1(List<MV> ptAngSpBl1) {
		PtAngSpBl1 = ptAngSpBl1;
	}

	public List<MV> getPtAngSpBl2() {
		return PtAngSpBl2;
	}

	public void setPtAngSpBl2(List<MV> ptAngSpBl2) {
		PtAngSpBl2 = ptAngSpBl2;
	}

	public List<MV> getPtAngSpBl3() {
		return PtAngSpBl3;
	}

	public void setPtAngSpBl3(List<MV> ptAngSpBl3) {
		PtAngSpBl3 = ptAngSpBl3;
	}

	public List<MV> getPtAngValBl1() {
		return PtAngValBl1;
	}

	public void setPtAngValBl1(List<MV> ptAngValBl1) {
		PtAngValBl1 = ptAngValBl1;
	}

	public List<MV> getPtAngValBl2() {
		return PtAngValBl2;
	}

	public void setPtAngValBl2(List<MV> ptAngValBl2) {
		PtAngValBl2 = ptAngValBl2;
	}

	public List<MV> getPtAngValBl3() {
		return PtAngValBl3;
	}

	public void setPtAngValBl3(List<MV> ptAngValBl3) {
		PtAngValBl3 = ptAngValBl3;
	}

	public List<STV> getPtCtlSt() {
		return PtCtlSt;
	}

	public void setPtCtlSt(List<STV> ptCtlSt) {
		PtCtlSt = ptCtlSt;
	}

	public List<CMD> getPtEmgChk() {
		return PtEmgChk;
	}

	public void setPtEmgChk(List<CMD> ptEmgChk) {
		PtEmgChk = ptEmgChk;
	}

	public List<MV> getPtHyPresBl1() {
		return PtHyPresBl1;
	}

	public void setPtHyPresBl1(List<MV> ptHyPresBl1) {
		PtHyPresBl1 = ptHyPresBl1;
	}

	public List<MV> getPtHyPresBl2() {
		return PtHyPresBl2;
	}

	public void setPtHyPresBl2(List<MV> ptHyPresBl2) {
		PtHyPresBl2 = ptHyPresBl2;
	}

	public List<MV> getPtHyPresBl3() {
		return PtHyPresBl3;
	}

	public void setPtHyPresBl3(List<MV> ptHyPresBl3) {
		PtHyPresBl3 = ptHyPresBl3;
	}

	public List<MV> getRotPos() {
		return RotPos;
	}

	public void setRotPos(List<MV> rotPos) {
		RotPos = rotPos;
	}

	public List<MV> getRotSpd() {
		return RotSpd;
	}

	public void setRotSpd(List<MV> rotSpd) {
		RotSpd = rotSpd;
	}

	public List<STV> getRotSt() {
		return RotSt;
	}

	public void setRotSt(List<STV> rotSt) {
		RotSt = rotSt;
	}

	public void setWea(WEA wea) {
		this.wea = wea;
	}

	public WEA getWea() {
		return wea;
	}
}
