SyntaxHighlighter.brushes.Pql = function()
{
// Copyright 2014 Christian Kuka christian@kuka.cc

// PQL Operator
var operator =	'ACCESS ADWIN AGGREGATE APPENDTO ASSOCIATIVESTORAGE ASSUREHEARTBEAT ASSUREORDER BATCHPRODCUER BENCHMARK BENCHMARKRESULT BUFFER BUFFEREDFILTER CACHE CALCLATENCY CHANGECORRELATE CHANGEDETECT CLASSIFICATION_LEARN CLASSIFY CLUSTERING COALESCE COMPARE CONTEXTENRICH CONVERTER CONVOLUTION COUNTSTATS CSVFILESINK CSVFILESOURCE DATABASESINK DATABASESOURCE DATARATECALC DBENRICH DETECTFACES DIFFERENCE DISTINCT DISTRIBUTION DISTRIBUTION_MERGE DUMMY DUPLICATEELIMINATION ELEMENTWINDOW EM ENRICH EXISTENCE EXISTENCETOPAYLOAD FASTHEATMAP FASTMEDIAN FEATUREEXTRACTION FILESINK FILTER FREQUENTITEMSET GENERATERULES GENERATOR GROUPSPLITFILEWRITER HASHFRAGMENT HDFSOURCE HEATMAP HMM JOIN JXTARECEIVER JXTASENDER KALMAN KEYVALUETOTUPLE LATENCYCONVERT LATENCYTOPAYLOAD LEFTJOIN LINEARREGRESSION LINEARREGRESSIONMERGE MAP MERGE PATTERN PREDICATEWINDOW PRIOIDJOIN PROBABILISTIC PROJECT PUBLISH QUALITY RANGEFRAGMENT RECEIVE RECOGNIZEFACES REGRESSION RENAME REPLACEMENT RETRIEVE ROUTE RRFRAGMENT SAMPLE SAMPLEFROM SASE SELECT SENDER SENTIMENTDETECTION SINK SLICEIMAGE SOCKETSINK SOMP STATEMAP STORE STOREINERTIA STOREKINECT STOREURG STREAM SUBSCRIBE SYNCHRONIZE SYNCWITHSYSTEMTIME TESTPRODUCER THROUGHPUT TIMESHIFT TIMESTAMP TIMESTAMPORDERVALIDATE TIMESTAMPTOPAYLOAD TIMEWINDOW TUPLETOKEYVALUE TWITTERSOURCE UDO UNION UNNEST VECTORQUANTIZATION VKINECTSINK WINDOW WSENRICH';

// PQL Parameter		
var parameter = 'ACTIVE ADVANCE ALGORITHM ALLOWOUTOFORDER APPEND APPENDGLOBALMEDIAN APPENDTO APPLICATIONTIMEDELAY APPLICATIONTIMEFACTOR APPLICATIONTIMEUNIT ARGUMENTS ATTR ATTRIBUTE ATTRIBUTES BATCHSIZE BUFFERTIME CACHESIZE CACHING CHARSET CLASS CLASSIFIER CLEAREND CLUSTERER CONFIDENCE CONNECTION CONNECTTOSERVER CONTROL COUNT CREATEONHEARTBEAT DATAFIELDS DATAHANDLER DATARATE DATEFORMAT DAY DB DEBUG DEBUGCLASSIFIER DELIMITER DELIVERFIRSTELEMENT DELIVERTIME DELTA DEPENDENT DESTINATION DOMAIN DRAIN DROP DUMP DUMPMETADATA EACH END ENRICHATTRIBUT EXPIRATIONTIME EXPLANATORY FACTOR FASTGROUPING FETCH_ATTRIBUTES FILENAME FILETYPE FLOATINGFORMATTER FRAGMENTS FUNCTION GESTURE GROUPATTRIBUTES GROUP_BY HEARTBEATRATE HIERARCHY HISTOGRAM HOST HOUR ID INCREMENTAL INDEX INIT INPUTDATAHANDLER INPUTPORT INPUTSCHEMA INTERVALLENGTH ISNOOP ITEMSET ITERATIONS JDBC KEEPINPUT KEYVALUEOUTPUT LANGUAGE LAZY_CONNECTION_CHECK LEARNER LEARNINGRATE LEFT_ATTRIBUTE LEFTHIGHPREDICATE LEFTLOWPREDICATE LOCALE LOGINNEEDED MAXBUFFERSIZE MAXELEMENTS MAXELEMENTSPERGROUP MAXRESULTS MEASUREMENT MEASUREMENTNOISE MEMORY METHOD MILLISECOND MINIMUMSIZE MINUTE MIXTURES MODE MONTH MULTITUPLEOUTPUT NAME NEWBROKER NGRAM NGRAMUPTO NOMINALS NUMBERFORMATTER NUMCLUSTER NUMERICAL OFFSET ONECLASSIFIER ONEMATCHPERINSTANCE OPERATION OPTIONS OUTER OUTERJOIN OUTPUTDATAHANDLER OUTPUTEACH OUTPUTMODE OVERLAPPINGPREDICATES PAIRS PARSINGMETHOD PARTITION PARTS PASSWORD PATH PATHS PERCENTILES PIPEID PORT PREDICATE PREDICATES PREDICATETYPE PROCESSNOISE PROPERTIES PROTOCOL QUERY RANGES READFIRSTLINE REALTIMEDELAY RECALCULATE RECORDDATARATE RELATIVETOLERANCE REMOVALSTRATEGY REMOVESTOPWORDS RESULTTYPE RETURN RIGHT_ATTRIBUTE RIGHTHIGHPREDICATE RIGHTLOWPREDICATE ROUNDINGFACTOR ROUTING SAMESTARTTIME SAMPLE SAMPLERATE SAMPLES SCHEMA SECOND SENDALWAYSHEARTBEAT SENDINGHEARTBEATS SERVICEMETHOD SHIFT SINK SINKNAME SINKPORT SINKTYPE SIZE SIZES SLICE SLIDE SOURCE SPLITDECISION START STARTATCURRENTTIME STATISTICS STEMMWORDS STORAGENAME STORE SUPPORT SUPPRESSCOUNTATTRIBUTE SYSTEMTIME TABLE TESTSETTEXT TESTSETTRUEDECISION TEXTDELIMITER TEXTTOBECLASSIFIED THREADS THRESHOLD TIME TIMEUNIT TIMEZONE TOLERANCE TOPICS TOPOLOGYTYPE TRAINSETMINSIZE TRAINSETTEXT TRAINSETTRUEDECISION TRANSACTIONS TRANSITION TRANSPORT TRIM TRUNCATE TYPE UNIQUEKEYS UNIT URL URLSUFFIX USER USEUDP VALUE VALUEATTRIBUTE VARIANCE WAITEACH WRAPPER WSDLLOCATION X XATTRIBUTE XLENGTH Y YATTRIBUTE YEAR YLENGTH';

// Odysseus Script
var preprocessors = '#ADDQUERY #BUFFERPLACEMENT #CONFIG #DEFINE #DOQUERYSHARING #DOREWRITE #DROPALLQUERIES #DROPALLSINKS #DROPALLSOURCES #ENDIF #IF #IFDEF #IFNDEF #IFSRCDEF #IFSRCNDEF #INPUT #INCLUDE #LOGIN #LOGOUT #LOOP #METADATA #ODYSSEUS_PARAM #PRINT #PARSER #QName #QUERY #RELOADFROMLOG #RUNQUERY #SCHEDULER #SLEEP #STARTQUERIES #STARTSCHEDULER #STOPSCHEDULER #TRANSCFG #UNDEF';

this.regexList = [
{ regex: /\/\/\/(.*)$/gm, css: 'comments' },// comments
{ regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string' },// double quoted strings
{ regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string' },// single quoted strings
{ regex: new RegExp(this.getKeywords(preprocessors), 'gm'), css: 'preprocessor' },// Odysseus Script
{ regex: new RegExp(this.getKeywords(operators), 'gm'), css: 'color5' },// PQL operator
{ regex: new RegExp(this.getKeywords(parameters), 'gm'), css: 'color4' }// PQL operator parameter
];
};

SyntaxHighlighter.brushes.Pql.prototype = new SyntaxHighlighter.Highlighter();
SyntaxHighlighter.brushes.Pql.aliases = ['PQL', 'pql'];
