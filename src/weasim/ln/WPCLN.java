package weasim.ln;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;

import weasim.cdc.DPL;
import weasim.cdc.INC;
import weasim.cdc.ING;
import weasim.cdc.INS;
import weasim.cdc.LPL;
import weasim.cdc.SPC;
import weasim.cdc.SPS;
import weasim.wea.WEA;

@Entity
@Inheritance
public abstract class WPCLN extends LN implements Serializable {
    
	@OneToOne(cascade =  CascadeType.ALL)
    private LPL NamePlt;
	@OneToOne(cascade =  CascadeType.ALL)
    private INC Mod;
	@OneToOne(cascade =  CascadeType.ALL)
    private INS Beh;
	@OneToOne(cascade =  CascadeType.ALL)
    private INS Health;
	@OneToOne(cascade =  CascadeType.ALL)
    private SPS Loc;
	@OneToOne(cascade =  CascadeType.ALL)
    private INS EEHealth;
	@OneToOne(cascade =  CascadeType.ALL)
    private DPL EEName;
	@OneToOne(cascade =  CascadeType.ALL)
    private INC OpCntRs;
	@OneToOne(cascade =  CascadeType.ALL)
    private INS OpCnt;
	@OneToOne(cascade =  CascadeType.ALL)
    private INS OpTmh;
	@OneToOne(cascade =  CascadeType.ALL)
    private SPS CalcExp;
	@OneToOne(cascade =  CascadeType.ALL)
    private SPC CalcStr;
	@OneToOne(cascade =  CascadeType.ALL)
    private ING CalcMthd;
	@OneToOne(cascade =  CascadeType.ALL)
    private ING CalcPer;
	
    private String CalcSrc;

    public WPCLN() {
    }

    public abstract WEA getWea();

    public void setNamePlt(LPL namePlt) {
        this.NamePlt = namePlt;
    }

    public LPL getNamePlt() {
        return NamePlt;
    }

    public void setMod(INC mod) {
        this.Mod = mod;
    }

    public INC getMod() {
        return Mod;
    }

    public void setBeh(INS beh) {
        this.Beh = beh;
    }

    public INS getBeh() {
        return Beh;
    }

    public void setHealth(INS health) {
        this.Health = health;
    }

    public INS getHealth() {
        return Health;
    }

    public void setLoc(SPS loc) {
        this.Loc = loc;
    }

    public SPS getLoc() {
        return Loc;
    }

    public void setEEHealth(INS eEHealth) {
        this.EEHealth = eEHealth;
    }

    public INS getEEHealth() {
        return EEHealth;
    }

    public void setEEName(DPL eEName) {
        this.EEName = eEName;
    }

    public DPL getEEName() {
        return EEName;
    }

    public void setOpCntRs(INC opCntRs) {
        this.OpCntRs = opCntRs;
    }

    public INC getOpCntRs() {
        return OpCntRs;
    }

    public void setOpCnt(INS opCnt) {
        this.OpCnt = opCnt;
    }

    public INS getOpCnt() {
        return OpCnt;
    }

    public void setOpTmh(INS opTmh) {
        this.OpTmh = opTmh;
    }

    public INS getOpTmh() {
        return OpTmh;
    }

    public void setCalcExp(SPS calcExp) {
        this.CalcExp = calcExp;
    }

    public SPS getCalcExp() {
        return CalcExp;
    }

    public void setCalcStr(SPC calcStr) {
        this.CalcStr = calcStr;
    }

    public SPC getCalcStr() {
        return CalcStr;
    }

    public void setCalcMthd(ING calcMthd) {
        this.CalcMthd = calcMthd;
    }

    public ING getCalcMthd() {
        return CalcMthd;
    }

    public void setCalcPer(ING calcPer) {
        this.CalcPer = calcPer;
    }

    public ING getCalcPer() {
        return CalcPer;
    }

    public void setCalcSrc(String calcSrc) {
        this.CalcSrc = calcSrc;
    }

    public String getCalcSrc() {
        return CalcSrc;
    }
}
