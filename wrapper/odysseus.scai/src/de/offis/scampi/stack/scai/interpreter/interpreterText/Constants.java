/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.interpreter.interpreterText;

/**
 *
 * @author sbehrensen
 */
public class Constants {

    public static final String PROTOCOL_IDENTIFIER = "t01";
    public static final String SEPARATOR = ";";
    public static final String[] COMPLEX = {
        "accs", "cfgpm", "cv", "datel", "dstyp", "datyp", "er", "mod", "oprtr", "opgrp",
        "oplnk", "optyp", "perm", "prunt", "ref", "secat", "m", "sensr", "sedom", "sref",
        "setyp", "userd"
    };
    // <editor-fold defaultstate="collapsed" desc="Commands">
    public static final String[] REGISTRYCMDS = {
        "rc_datyp", "rr_datyp", "ru_datyp", "rg_datyp", "rc_datel", "rr_datel", "ru_datel",
        "rg_datel", "rc_dstyp", "rr_dstyp", "ru_dstyp", "rg_dstyp", "rc_cfgpm", "rr_cfgpm",
        "ru_cfgpm", "rg_cfgpm", "rc_setyp", "rr_setyp", "ru_setyp", "rg_setyp", "rc_sedom",
        "rr_sedom", "ru_sedom", "rg_sedom", "rc_secat", "rr_secat", "ru_secat", "rg_secat",
        "rc_sensr", "rr_sensr", "ru_sensr", "rg_sensr", "rc_opgrp", "rr_opgrp", "ru_opgrp",
        "rg_opgrp", "rc_optyp", "rr_optyp", "ru_optyp", "rg_optyp", "rc_oprtr", "rr_oprtr",
        "ru_oprtr", "rg_oprtr", "rl_datyp", "rl_cfgpm", "rl_dstyp", "rl_setyp", "rl_sedom",
        "rl_secat", "rl_sensr", "rl_oprtr", "rl_opgrp", "rl_optyp", "rl_datel", "rl_prunt",
        "rl_links", "rlink_op", "rulnk_op", "rreg_pru", "rurg_pru", "rde_opgp", "rud_opgp",
        "ra_cg2dm", "rr_cg2dm", "ra_tp2cg", "rr_tp2cg", "ra_tp2dm", "rr_tp2dm", "ra_de2ds",
        "rr_de2ds", "ra_cg2cg", "rr_cg2cg", "rg_prunt", "rg_ogpst"
    };
    public static final String[] ACCESSCMDS = {
        "ac_userd", "ar_userd", "au_userd", "aau_user", "ad_sessn", "ag_userd", "agr_accs",
        "awd_accs", "als_accs"
    };
    public static final String[] SENSORCMDS = {
        "sst_snsr", "ssp_snsr", "scf_snsr", "sg_confg", "ssubs_ds", "sunsb_ds", "sg_spmod",
        "sg_value"
    };
    public static final String[] MEASUREMENTS = {
        "mdstream"
    };
    public static final String[] IDENTIFICATION = {
        "idaccess"
    };
    public static final String[] ACKNOWLEDGMENTS = {
        "ack_excp", "ack_sucs", "ack_rply"
    };
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Primitive Datatypes">
    public static final String DATETIME_TYPE = "\\-?\\d\\d\\d\\d\\-\\d\\d\\-\\d\\dT\\d\\d\\:\\d\\d\\:\\d\\d(?:\\.\\d*)?(?:Z|(?:\\+|\\-)(?:\\d\\d\\:\\d\\d))?";
    //    public static final String STRING_TYPE = "\\\"[^\\\"]*\\\""; // TODO: looks dangerous
    //    public static final String STRING_TYPE = "\\\".*\\\""; // TODO: looks dangerous
    public static final String BLINDARRAY_TYPE = "\\((.*)\\)";
    public static final String STRING_TYPE = "\\\"(?:\\\"|[^\"])*?\\\""; // allows escaped quotation marks in strings
    public static final String VARIABLE_TYPE = "[a-zA-Z0-9+/\\-_\\.]*";
    public static final String DECIMAL_TYPE = "\\-?[0-9]+\\.?[0-9]*";
    public static final String BINARY_TYPE = "\\\'[a-zA-Z0-9+/]+={0,2}\\\'";
    public static final String UINT_TYPE = "[0-9]+";
    public static final String INT_TYPE = "\\-?[0-9]+";
    public static final String BOOLEAN_TYPE = "((true)|(false))";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Prefabs">
    public static final String NAME = "( n=" + VARIABLE_TYPE + ")";
    public static final String REFERENCEBYNAME = "(rn=" + VARIABLE_TYPE + ")";
    public static final String REFERENCEBYID = "(ri=" + VARIABLE_TYPE + ")";
    public static final String REFERENCEDOMAINNAME = "(dn=" + VARIABLE_TYPE + ")";
    public static final String REFERENCE = "(" + REFERENCEBYNAME + "|" + REFERENCEBYID + ")";
    public static final String SENSORREFERENCE = "(" + REFERENCEBYID + "|(?:" + REFERENCEBYNAME + " " + REFERENCEDOMAINNAME + "))";
    public static final String REFERENCEARRAY = "(" + REFERENCE + "?(?: " + REFERENCE + ")*)";
    public static final String ENUM_ITEM = "(?:a=\\((o=" + UINT_TYPE + ") (n=" + STRING_TYPE + ")\\))";
    public static final String DATATYPE_STRING_TYPE = "(( l=" + UINT_TYPE + ")?( h=" + UINT_TYPE + ")?( r=" + STRING_TYPE + ")?( d=" + STRING_TYPE + "))";
    public static final String DATATYPE_DECIMAL_TYPE = "(( l=" + DECIMAL_TYPE + ")?( h=" + DECIMAL_TYPE + ")?( s=" + DECIMAL_TYPE + ")?( d=" + DECIMAL_TYPE + "))";
    public static final String DATATYPE_BINARY_TYPE = "(( l=" + UINT_TYPE + ")?( h=" + UINT_TYPE + ")?( d=" + BINARY_TYPE + "))";
    public static final String DATATYPE_LIST_TYPE = "(( l=" + UINT_TYPE + ")?( h=" + UINT_TYPE + ")?((?: a=" + STRING_TYPE + ")+)((?: d=" + STRING_TYPE + ")*))";
    public static final String DATATYPE_ENUM_TYPE = "(((?: " + ENUM_ITEM + ")+)( d=" + STRING_TYPE + "))";
    public static final String ATOMICPARAMETER = "(( u=" + VARIABLE_TYPE + ")( i=" + BOOLEAN_TYPE + ") " + REFERENCE + ")";
    public static final String COMPLEXPARAMETER = "( " + REFERENCE + ")+";
    public static final String SEQUENCEPARAMETER = "(( l=" + UINT_TYPE + ")( h=" + UINT_TYPE + ") " + REFERENCE + ")";
    public static final String TIMESTAMP = "(ts=" + DATETIME_TYPE + ")";
    public static final String OLDVALUES = "( ov=" + UINT_TYPE + ")?";
    public static final String PROPERTY_TYPE = "(?: p=\\(k=" + VARIABLE_TYPE + " v=" + STRING_TYPE + "\\))";
    public static final String PROPERTY_TYPE_RW = "( p=\\((k=" + VARIABLE_TYPE + ") (v=" + STRING_TYPE + ") (w=" + BOOLEAN_TYPE + ")\\))";
    public static final String DATASTREAMELEMENT = "(?:(pa=" + STRING_TYPE + ")( q=" + DECIMAL_TYPE + ")?(?: (et=(?:s|n|b|u)) (em=" + STRING_TYPE + "))? (da=" + STRING_TYPE + "))";
    public static final String DATASTREAMELEMENTARRAY = "((?: (?:ds=\\(" + DATASTREAMELEMENT + "\\)))+)";
    public static final String DATASTREAM = TIMESTAMP + NAME + NAME + OLDVALUES + DATASTREAMELEMENTARRAY;
    public static final String LOGIN = "( (un=" + VARIABLE_TYPE + ") (pw=" + BINARY_TYPE + "))";
    public static final String AUTHENTICATION = "(( st=" + VARIABLE_TYPE + ")|" + LOGIN + ")";
    public static final String CONFIGURATIONVALUE = "(cv=\\((n=" + VARIABLE_TYPE + ")( ix=" + UINT_TYPE + ")?(?:( va=" + STRING_TYPE + ")|( cva=" + BLINDARRAY_TYPE + "))\\))"; // Let the parser do the rest
    public static final String CONFIGURATIONVALUEARRAY = "((?: " + CONFIGURATIONVALUE + ")+)";
    public static final String PERMISSION = "(pm=\\((((?:g)|(?:w))=" + STRING_TYPE + ")(" + PROPERTY_TYPE + "*)( ih=" + BOOLEAN_TYPE + ")?\\))";
    public static final String PERMISSIONARRAY = "((?: " + PERMISSION + ")*)";
    public static final String USERDATA = LOGIN + PERMISSIONARRAY + "((?: rl=" + VARIABLE_TYPE + ")*)";
    public static final String SESSION = "( se=" + STRING_TYPE + ")";
    public static final String ACCESSOR = "((?:u)|(?:d))=\\((" + REFERENCE + ")\\)";
    public static final String ACCESSORARRAY = "((?: " + ACCESSOR + ")+)";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    public static final String OPERATIONID = "( id=" + VARIABLE_TYPE + ")";
    public static final String TEXT = "( tx=" + STRING_TYPE + ")";
    public static final String ERRORCODE = "( ec=" + UINT_TYPE + ")";
    public static final String DATALENGTH = "( dl=" + UINT_TYPE + ")";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Complex Types">
    public static final String DATATYPE = NAME + "(" + DATATYPE_STRING_TYPE + "|" + DATATYPE_DECIMAL_TYPE + "|" + DATATYPE_BINARY_TYPE + "|" + DATATYPE_LIST_TYPE + "|" + DATATYPE_ENUM_TYPE + ")";
    public static final String DATAELEMENT = "( t=(a(tomic)?|c(omplex)?)" + NAME + ") (" + REFERENCEARRAY + ")";
    public static final String DATASTREAMTYPE = NAME + " (" + REFERENCEARRAY + ")";
    public static final String CONFIGURATIONPARAMETER = NAME + "( o=" + BOOLEAN_TYPE + ")(" + ATOMICPARAMETER + "|" + COMPLEXPARAMETER + "|" + SEQUENCEPARAMETER + ")";
    public static final String SENSORTYPE = NAME + "( a=" + VARIABLE_TYPE + ") (" + REFERENCE + ")( p=\\(" + REFERENCEARRAY + "\\))?( c=\\(" + REFERENCEARRAY + "\\))( d=\\(" + REFERENCEARRAY + "\\))";
    public static final String SENSORDOMAIN = NAME;
    public static final String SENSORCATEGORY = NAME + "(?: (" + REFERENCE + "))?( d=\\(" + REFERENCEARRAY + "\\))";
    public static final String SENSOR = NAME + " (" + REFERENCE + ") (" + REFERENCE + ") (vi=" + BOOLEAN_TYPE + ")";
    public static final String OPERATORGROUP = NAME;
    public static final String OPERATORTYPE = NAME + "( m=" + VARIABLE_TYPE + ")" + PROPERTY_TYPE_RW + "*( ds=" + STRING_TYPE + ")?";
    public static final String OPERATOR = NAME + "(" + PROPERTY_TYPE + "*) t=((?:(i(?:nput)?|o(?:utput)?) " + SENSORREFERENCE + ")|(s(?:ervice)? " + REFERENCE + "))";
    public static final String LINK = " (" + REFERENCE + ") ((io|so)=\\(" + REFERENCE + "\\)) ((so|oo)=\\(" + REFERENCE + "\\))";
    public static final String PROCESSINGUNIT = NAME + " (adr=" + VARIABLE_TYPE + ") (v=" + DECIMAL_TYPE + ")"; // TODO: address should be URL_TYPE
    public static final String REPLY = "((?: ((m=\\(" + DATASTREAM + "\\))|" + CONFIGURATIONVALUE + "))+)";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SensorRegistryControl">
    public static final String RC_DATYP = "(" + REGISTRYCMDS[0] + ")" + DATATYPE + OPERATIONID + "?";
    public static final String RR_DATYP = "(" + REGISTRYCMDS[1] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RU_DATYP = "(" + REGISTRYCMDS[2] + ") " + REFERENCE + DATATYPE + OPERATIONID + "?";
    public static final String RG_DATYP = "(" + REGISTRYCMDS[3] + ") " + REFERENCE + OPERATIONID;
    public static final String RC_DATEL = "(" + REGISTRYCMDS[4] + ")" + DATAELEMENT + OPERATIONID + "?";
    public static final String RR_DATEL = "(" + REGISTRYCMDS[5] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RU_DATEL = "(" + REGISTRYCMDS[6] + ") " + REFERENCE + DATAELEMENT + OPERATIONID + "?";
    public static final String RG_DATEL = "(" + REGISTRYCMDS[7] + ") " + REFERENCE + OPERATIONID;
    public static final String RC_DSTYP = "(" + REGISTRYCMDS[8] + ")" + DATASTREAMTYPE + OPERATIONID + "?";
    public static final String RR_DSTYP = "(" + REGISTRYCMDS[9] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RU_DSTYP = "(" + REGISTRYCMDS[10] + ") " + REFERENCE + DATASTREAMTYPE + OPERATIONID + "?";
    public static final String RG_DSTYP = "(" + REGISTRYCMDS[11] + ") " + REFERENCE + OPERATIONID;
    public static final String RC_CFGPM = "(" + REGISTRYCMDS[12] + ")" + CONFIGURATIONPARAMETER + OPERATIONID + "?";
    public static final String RR_CFGPM = "(" + REGISTRYCMDS[13] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RU_CFGPM = "(" + REGISTRYCMDS[14] + ") " + REFERENCE + CONFIGURATIONPARAMETER + OPERATIONID + "?";
    public static final String RG_CFGPM = "(" + REGISTRYCMDS[15] + ") " + REFERENCE + OPERATIONID;
    public static final String RC_SETYP = "(" + REGISTRYCMDS[16] + ")" + SENSORTYPE + OPERATIONID + "?";
    public static final String RR_SETYP = "(" + REGISTRYCMDS[17] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RU_SETYP = "(" + REGISTRYCMDS[18] + ") " + REFERENCE + SENSORTYPE + OPERATIONID + "?";
    public static final String RG_SETYP = "(" + REGISTRYCMDS[19] + ") " + REFERENCE + OPERATIONID;
    public static final String RC_SEDOM = "(" + REGISTRYCMDS[20] + ")" + SENSORDOMAIN + OPERATIONID + "?";
    public static final String RR_SEDOM = "(" + REGISTRYCMDS[21] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RU_SEDOM = "(" + REGISTRYCMDS[22] + ") " + REFERENCE + SENSORDOMAIN + OPERATIONID + "?";
    public static final String RG_SEDOM = "(" + REGISTRYCMDS[23] + ") " + REFERENCE + OPERATIONID;
    public static final String RC_SECAT = "(" + REGISTRYCMDS[24] + ")" + SENSORCATEGORY + OPERATIONID + "?";
    public static final String RR_SECAT = "(" + REGISTRYCMDS[25] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RU_SECAT = "(" + REGISTRYCMDS[26] + ") " + REFERENCE + SENSORCATEGORY + OPERATIONID + "?";
    public static final String RG_SECAT = "(" + REGISTRYCMDS[27] + ") " + REFERENCE + OPERATIONID;
    public static final String RC_SENSR = "(" + REGISTRYCMDS[28] + ")" + SENSOR + OPERATIONID + "?";
    public static final String RR_SENSR = "(" + REGISTRYCMDS[29] + ") " + SENSORREFERENCE + OPERATIONID + "?";
    public static final String RU_SENSR = "(" + REGISTRYCMDS[30] + ") " + SENSORREFERENCE + SENSOR + OPERATIONID + "?";
    public static final String RG_SENSR = "(" + REGISTRYCMDS[31] + ") " + SENSORREFERENCE + OPERATIONID;
    public static final String RC_OPGRP = "(" + REGISTRYCMDS[32] + ")" + OPERATORGROUP + OPERATIONID + "?";
    public static final String RR_OPGRP = "(" + REGISTRYCMDS[33] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RU_OPGRP = "(" + REGISTRYCMDS[34] + ") " + REFERENCE + OPERATORGROUP + OPERATIONID + "?";
    public static final String RG_OPGRP = "(" + REGISTRYCMDS[35] + ") " + REFERENCE + OPERATIONID;
    public static final String RC_OPTYP = "(" + REGISTRYCMDS[36] + ")" + OPERATORTYPE + OPERATIONID + "?";
    public static final String RR_OPTYP = "(" + REGISTRYCMDS[37] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RU_OPTYP = "(" + REGISTRYCMDS[38] + ") " + REFERENCE + OPERATORTYPE + OPERATIONID + "?";
    public static final String RG_OPTYP = "(" + REGISTRYCMDS[39] + ") " + REFERENCE + OPERATIONID;
    public static final String RC_OPRTR = "(" + REGISTRYCMDS[40] + ") " + REFERENCE + OPERATOR + OPERATIONID + "?";
    public static final String RR_OPRTR = "(" + REGISTRYCMDS[41] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RU_OPRTR = "(" + REGISTRYCMDS[42] + ") " + REFERENCE + " " + REFERENCE + OPERATOR + OPERATIONID + "?";
    public static final String RG_OPRTR = "(" + REGISTRYCMDS[43] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID;
    public static final String RL_DATYP = "(" + REGISTRYCMDS[44] + ")" + OPERATIONID;
    public static final String RL_CFGPM = "(" + REGISTRYCMDS[45] + ")" + OPERATIONID;
    public static final String RL_DSTYP = "(" + REGISTRYCMDS[46] + ")" + OPERATIONID;
    public static final String RL_SETYP = "(" + REGISTRYCMDS[47] + ")(?: t=(c|d) " + REFERENCE +")?" + OPERATIONID;
    public static final String RL_SEDOM = "(" + REGISTRYCMDS[48] + ")" + OPERATIONID;
    public static final String RL_SECAT = "(" + REGISTRYCMDS[49] + ")(?: " + REFERENCE + ")?" + OPERATIONID;
    public static final String RL_SENSR = "(" + REGISTRYCMDS[50] + ")" + OPERATIONID;
    public static final String RL_OPRTR = "(" + REGISTRYCMDS[51] + ") " + REFERENCE + OPERATIONID;
    public static final String RL_OPGRP = "(" + REGISTRYCMDS[52] + ")" + OPERATIONID;
    public static final String RL_OPTYP = "(" + REGISTRYCMDS[53] + ")" + OPERATIONID;
    public static final String RL_DATEL = "(" + REGISTRYCMDS[54] + ")" + OPERATIONID;
    public static final String RL_PRUNT = "(" + REGISTRYCMDS[55] + ")" + OPERATIONID;
    public static final String RL_LINKS = "(" + REGISTRYCMDS[56] + ") " + REFERENCE + OPERATIONID;
    public static final String RLINK_OP = "(" + REGISTRYCMDS[57] + ")" + LINK + OPERATIONID + "?";
    public static final String RULNK_OP = "(" + REGISTRYCMDS[58] + ")" + LINK + OPERATIONID + "?";
    public static final String RREG_PRU = "(" + REGISTRYCMDS[59] + ")" + PROCESSINGUNIT + OPERATIONID + "?";
    public static final String RURG_PRU = "(" + REGISTRYCMDS[60] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RDE_OPGP = "(" + REGISTRYCMDS[61] + ") " + REFERENCE + "(?: " + REFERENCE + ")?" + OPERATIONID + "?";
    public static final String RUD_OPGP = "(" + REGISTRYCMDS[62] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String RA_CG2DM = "(" + REGISTRYCMDS[63] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RR_CG2DM = "(" + REGISTRYCMDS[64] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RA_TP2CG = "(" + REGISTRYCMDS[65] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RR_TP2CG = "(" + REGISTRYCMDS[66] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RA_TP2DM = "(" + REGISTRYCMDS[67] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RR_TP2DM = "(" + REGISTRYCMDS[68] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RA_DE2DS = "(" + REGISTRYCMDS[69] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RR_DE2DS = "(" + REGISTRYCMDS[70] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RA_CG2CG = "(" + REGISTRYCMDS[71] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RR_CG2CG = "(" + REGISTRYCMDS[72] + ") " + REFERENCE + " " + REFERENCE + OPERATIONID + "?";
    public static final String RG_PRUNT = "(" + REGISTRYCMDS[73] + ") " + REFERENCE + OPERATIONID;
    public static final String RG_OGPST = "(" + REGISTRYCMDS[74] + ") " + REFERENCE + OPERATIONID;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Measurements">
    public static final String MDSTREAM = "(" + MEASUREMENTS[0] + ") " + DATASTREAM;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Identification">
    public static final String IDACCESS = "(" + IDENTIFICATION[0] + ")" + AUTHENTICATION;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SensorControl">
    public static final String SST_SNSR = "(" + SENSORCMDS[0] + ") " + SENSORREFERENCE + OPERATIONID + "?";
    public static final String SSP_SNSR = "(" + SENSORCMDS[1] + ") " + SENSORREFERENCE + OPERATIONID + "?";
    public static final String SCF_SNSR = "(" + SENSORCMDS[2] + ") " + SENSORREFERENCE + CONFIGURATIONVALUEARRAY + OPERATIONID + "?";
    public static final String SG_CONFG = "(" + SENSORCMDS[3] + ") " + SENSORREFERENCE + OPERATIONID + "?";
    public static final String SSUBS_DS = "(" + SENSORCMDS[4] + ") " + SENSORREFERENCE + OPERATIONID + "?";
    public static final String SSUNS_DS = "(" + SENSORCMDS[5] + ") " + SENSORREFERENCE + OPERATIONID + "?";
    public static final String SG_SPMOD = "(" + SENSORCMDS[6] + ") " + SENSORREFERENCE + OPERATIONID + "?";
    public static final String SG_VALUE = "(" + SENSORCMDS[7] + ") " + SENSORREFERENCE + OPERATIONID;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AccessControl">
    public static final String AC_USERD = "(" + ACCESSCMDS[0] + ")" + USERDATA + OPERATIONID + "?";
    public static final String AR_USERD = "(" + ACCESSCMDS[1] + ") " + REFERENCE + OPERATIONID + "?";
    public static final String AU_USERD = "(" + ACCESSCMDS[2] + ") " + REFERENCE + USERDATA + OPERATIONID + "?";
    public static final String AAU_USER = "(" + ACCESSCMDS[3] + ")" + LOGIN + OPERATIONID + "?";
    public static final String AD_SESSN = "(" + ACCESSCMDS[4] + ")" + SESSION + "?" + OPERATIONID + "?";
    public static final String AG_USERD = "(" + ACCESSCMDS[5] + ") " + REFERENCE + OPERATIONID;
    public static final String AGR_ACCS = "(" + ACCESSCMDS[6] + ") " + SENSORREFERENCE + ACCESSORARRAY + OPERATIONID + "?";
    public static final String AWD_ACCS = "(" + ACCESSCMDS[7] + ") " + SENSORREFERENCE + ACCESSORARRAY + OPERATIONID + "?";
    public static final String ALS_ACCS = "(" + ACCESSCMDS[8] + ") " + SENSORREFERENCE + OPERATIONID;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Acknowledgement">
    public static final String ACK_EXCP = "(" + ACKNOWLEDGMENTS[0] + ")" + TEXT + ERRORCODE + "?" + OPERATIONID;
    public static final String ACK_SUCS = "(" + ACKNOWLEDGMENTS[1] + ")" + TEXT + "?" + OPERATIONID;
    public static final String ACK_RPLY = "(" + ACKNOWLEDGMENTS[2] + ")" + REPLY + OPERATIONID;
    // </editor-fold>
}
