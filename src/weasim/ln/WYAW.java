package weasim.ln;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import weasim.cdc.CMD;
import weasim.cdc.MV;
import weasim.cdc.STV;
import weasim.cdc.TMS;
import weasim.wea.WEA;

@Entity
@Inheritance
public class WYAW extends WPCLN implements Serializable {
	@OneToOne(mappedBy = "wyaw", cascade = CascadeType.ALL)
	private WEA wea;
	
	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_CwTm")
	private List<TMS> CwTm;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_CcwTm")
	private List<TMS> CcwTm;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_YwSt")
	private List<STV> YwSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_YwBrakeSt")
	private List<STV> YwBrakeSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_YwSpd")
	private List<MV> YwSpd;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_Tmp")
	private List<MV> Tmp;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_YawAng")
	private List<MV> YawAng;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_CabWup")
	private List<MV> CabWup;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_SysGsLev")
	private List<MV> SysGsLev;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_BrkPres")
	private List<MV> BrkPres;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WYAW_AtvYw")
	private List<CMD> AtvYw;

	public List<CMD> getAtvYw() {
		return AtvYw;
	}

	public void setAtvYw(List<CMD> atvYw) {
		AtvYw = atvYw;
	}

	public List<MV> getBrkPres() {
		return BrkPres;
	}

	public void setBrkPres(List<MV> brkPres) {
		BrkPres = brkPres;
	}

	public List<MV> getCabWup() {
		return CabWup;
	}

	public void setCabWup(List<MV> cabWup) {
		CabWup = cabWup;
	}

	public List<TMS> getCcwTm() {
		return CcwTm;
	}

	public void setCcwTm(List<TMS> ccwTm) {
		CcwTm = ccwTm;
	}

	public List<TMS> getCwTm() {
		return CwTm;
	}

	public void setCwTm(List<TMS> cwTm) {
		CwTm = cwTm;
	}

	public List<MV> getSysGsLev() {
		return SysGsLev;
	}

	public void setSysGsLev(List<MV> sysGsLev) {
		SysGsLev = sysGsLev;
	}

	public List<MV> getTmp() {
		return Tmp;
	}

	public void setTmp(List<MV> tmp) {
		Tmp = tmp;
	}

	public List<MV> getYawAng() {
		return YawAng;
	}

	public void setYawAng(List<MV> yawAng) {
		YawAng = yawAng;
	}

	public List<STV> getYwBrakeSt() {
		return YwBrakeSt;
	}

	public void setYwBrakeSt(List<STV> ywBrakeSt) {
		YwBrakeSt = ywBrakeSt;
	}

	public List<MV> getYwSpd() {
		return YwSpd;
	}

	public void setYwSpd(List<MV> ywSpd) {
		YwSpd = ywSpd;
	}

	public List<STV> getYwSt() {
		return YwSt;
	}

	public void setYwSt(List<STV> ywSt) {
		YwSt = ywSt;
	}

	public WEA getWea() {
		return wea;
	}
}
