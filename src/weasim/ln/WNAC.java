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
import weasim.cdc.SPV;
import weasim.cdc.STV;
import weasim.cdc.TMS;
import weasim.wea.WEA;

@Entity
@Inheritance
public class WNAC extends WPCLN implements Serializable {
	@OneToOne(mappedBy = "wnac", cascade = CascadeType.ALL)
	private WEA wea;
	
	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_BecTmRs")
	private List<TMS> BecTmRs;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_BecBulbSt")
	private List<STV> BecBulbSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_WdHtSt")
	private List<STV> WdHtSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_IceSt")
	private List<STV> IceSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_AneSt")
	private List<STV> AneSt;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_Dir")
	private List<MV> Dir;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_WdSpd")
	private List<MV> WdSpd;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_WdDir")
	private List<MV> WdDir;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_ExTmp")
	private List<MV> ExTmp;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_IntlTmp")
	private List<MV> IntlTmp;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_IntlHum")
	private List<MV> IntlHum;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_BecLumLev")
	private List<MV> BecLumLev;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_Vis")
	private List<MV> Vis;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_Ice")
	private List<MV> Ice;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_DispXdir")
	private List<MV> DispXdir;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_DispYdir")
	private List<MV> DispYdir;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_SetBecMod")
	private List<CMD> SetBecMod;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_SetBecLev")
	private List<SPV> SetBecLev;

	@OneToMany(cascade =  CascadeType.ALL)
	@JoinTable(name = "WNAC_SetFlsh")
	private List<SPV> SetFlsh;

	public List<STV> getAneSt() {
		return AneSt;
	}

	public void setAneSt(List<STV> aneSt) {
		AneSt = aneSt;
	}

	public List<STV> getBecBulbSt() {
		return BecBulbSt;
	}

	public void setBecBulbSt(List<STV> becBulbSt) {
		BecBulbSt = becBulbSt;
	}

	public List<MV> getBecLumLev() {
		return BecLumLev;
	}

	public void setBecLumLev(List<MV> becLumLev) {
		BecLumLev = becLumLev;
	}

	public List<TMS> getBecTmRs() {
		return BecTmRs;
	}

	public void setBecTmRs(List<TMS> becTmRs) {
		BecTmRs = becTmRs;
	}

	public List<MV> getDir() {
		return Dir;
	}

	public void setDir(List<MV> dir) {
		Dir = dir;
	}

	public List<MV> getDispXdir() {
		return DispXdir;
	}

	public void setDispXdir(List<MV> dispXdir) {
		DispXdir = dispXdir;
	}

	public List<MV> getDispYdir() {
		return DispYdir;
	}

	public void setDispYdir(List<MV> dispYdir) {
		DispYdir = dispYdir;
	}

	public List<MV> getExTmp() {
		return ExTmp;
	}

	public void setExTmp(List<MV> exTmp) {
		ExTmp = exTmp;
	}

	public List<MV> getIce() {
		return Ice;
	}

	public void setIce(List<MV> ice) {
		Ice = ice;
	}

	public List<STV> getIceSt() {
		return IceSt;
	}

	public void setIceSt(List<STV> iceSt) {
		IceSt = iceSt;
	}

	public List<MV> getIntlHum() {
		return IntlHum;
	}

	public void setIntlHum(List<MV> intlHum) {
		IntlHum = intlHum;
	}

	public List<MV> getIntlTmp() {
		return IntlTmp;
	}

	public void setIntlTmp(List<MV> intlTmp) {
		IntlTmp = intlTmp;
	}

	public List<SPV> getSetBecLev() {
		return SetBecLev;
	}

	public void setSetBecLev(List<SPV> setBecLev) {
		SetBecLev = setBecLev;
	}

	public List<CMD> getSetBecMod() {
		return SetBecMod;
	}

	public void setSetBecMod(List<CMD> setBecMod) {
		SetBecMod = setBecMod;
	}

	public List<SPV> getSetFlsh() {
		return SetFlsh;
	}

	public void setSetFlsh(List<SPV> setFlsh) {
		SetFlsh = setFlsh;
	}

	public List<MV> getVis() {
		return Vis;
	}

	public void setVis(List<MV> vis) {
		Vis = vis;
	}

	public List<MV> getWdDir() {
		return WdDir;
	}

	public void setWdDir(List<MV> wdDir) {
		WdDir = wdDir;
	}

	public List<STV> getWdHtSt() {
		return WdHtSt;
	}

	public void setWdHtSt(List<STV> wdHtSt) {
		WdHtSt = wdHtSt;
	}

	public List<MV> getWdSpd() {
		return WdSpd;
	}

	public void setWdSpd(List<MV> wdSpd) {
		WdSpd = wdSpd;
	}

	public WEA getWea() {
		return wea;
	}
}
