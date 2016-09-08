;(function()
{
// CommonJS
typeof(require) != 'undefined' ? SyntaxHighlighter = require('shCore').SyntaxHighlighter : null;

function Brush()
{	
// PQL Operator
var operators =	'ACCESS ADWIN AGGREGATE APPENDTO ASSOCIATIVESTORAGE ASSUREHEARTBEAT ASSUREORDER AUDIENCEENGAGEMENT BATCHPRODCUER BENCHMARK BENCHMARKRESULT BUFFER BUFFEREDFILTER CACHE CALCLATENCY CHANGECORRELATE CHANGEDETECT CLASSIFICATION_EVALUATE CLASSIFICATION_LEARN CLASSIFY CLUSTERING COALESCE COMBINE COMPARE CONTEXTENRICH CONVERSATIONREACH CONVERTER CONVOLUTION CSVFILESINK CSVFILESOURCE DATABASESINK DATABASESOURCE DATARATE DATARATECALC DBENRICH DETECTFACES DIFFERENCE DISTINCT DISTRIBUTION DUMMY DUPLICATEELIMINATION ELASTICSEARCHSINK ELEMENTWINDOW EM ENRICH EXISTENCE EXISTENCETOPAYLOAD FASTHEATMAP FASTMEDIAN FEATUREEXTRACTION FILESINK FILTER FREQUENTPATTERN GENERATERULES GENERATOR GROUPSPLITFILEWRITER HADOOPSINK HASHFRAGMENT HDFSOURCE HEATMAP HMM HTTPSTREAMACCESS INDUCTIVEMINER INTERSECTION JOIN JXTARECEIVER JXTASENDER KALMAN KDE KEYPERFORMANCEINDICATORS KEYVALUETOTUPLE LATENCYTOPAYLOAD LEFTJOIN LINEARREGRESSION LINEARREGRESSIONMERGE LOADBALANCINGSYNCHRONIZER LOSSY MAP MERGE MODBUSTCPSOURCE MONGODBSINK MOSAIK OPCDASOURCE PATTERN PREDICATEWINDOW PRIOIDJOIN PROBABILISTIC PROJECT PUBLISH RANGEFRAGMENT RECEIVE RECOGNIZEFACES RECOMMENDATION RECOMMENDATION_EVALUATE RECOMMENDATION_LEARN RECOVERYMERGE REDUCELOAD REGRESSION RENAME REPLACEMENT REPLICATIONMERGE RETRIEVE ROUTE RPIGPIOSINK RPIGPIOSOURCE RRFRAGMENT SAMPLE SAMPLEFROM SASE SELECT SENDER SENTIMENTANALYSIS SENTIMENTDETECTION SHAREOFVOICE SINK SLICEIMAGE SOCKETSINK SORT STATEMAP STORE STOREINERTIA STOREKINECT STOREURG STREAM SUBSCRIBE SYNCHRONIZE SYNCWITHSYSTEMTIME SYSTEMLOADTOPAYLOAD TESTPRODUCER TEXTPROCESSING THROUGHPUT TIMESHIFT TIMESTAMP TIMESTAMPORDERVALIDATE TIMESTAMPTOPAYLOAD TIMEWINDOW TOPK TRAJECTORYCOMPARE TRAJECTORYCONSTRUCT TTT TUPLEAGGREGATE TUPLETOKEYVALUE TWITTERSOURCE UDO UNION UNNEST VECTORQUANTIZATION VKINECTSINK WEBCRAWLER WINDOW WSENRICH';

// PQL Parameter		
var parameters = 'ACTIVE ADVANCE ALGORITHM ALLCOMPANIES ALLOWNULL ALLOWOUTOFORDER ALLTOPICS APPEND APPENDGLOBALMEDIAN APPENDTO APPLICATIONTIMEDELAY APPLICATIONTIMEFACTOR APPLICATIONTIMEUNIT ARGUMENTS ASCENDING ASSUMEBTU ATTR ATTRIBUTE ATTRIBUTES ATTRIBUTETRAINSETTEXT ATTRIBUTETRAINSETTRUEDECISION BATCHSIZE BATCHTIMEOUT BUCKETWIDTH BUFFERNEWINPUTELEMENTS BUFFERTIME CACHESIZE CACHING CHARSET CLASS CLASSIFIER CLASSNAME CLEAREND COLLECTIONNAME CONCRETETOPIC CONCRETETOPICS CONFIDENCE CONNECTION CONNECTTOSERVER CONTINUOUS CONTROL COUNT COUNTOFALLTOPICS CREATEONHEARTBEAT DATAFIELDS DATAHANDLER DATARATE DATEFORMAT DAY DB DEBUG DEBUGCLASSIFIER DELIMITER DELIVERFIRSTELEMENT DELIVERTIME DELTA DEPENDENT DESCENDING DESTINATION DOMAIN DONGRAM DOREMOVESTOPWORDS DOSTEMMING DRAIN DRAINATCLOSE DRAINATDONE DROP DUMP DUMPMETADATA EACH END ENDPREDICATE ENRICHATTRIBUT ESCAPE_NAMES EVALUATEONPUNCTUATION EXPIRATIONTIME EXPLANATORY EXPRESSIONS FACTOR FADING FASTGROUPING FETCH_ATTRIBUTES FILENAME FILETYPE FLOATINGFORMATTER FRAGMENTS FUNCTION GENERATINGTYPE GESTURE GROUPATTRIBUTES GROUP_BY HEARTBEATRATE HEARTBEATREATE HIERARCHY HISTOGRAM HOST HOUR ID INCOMINGTEXT INCREMENTAL INDEX INDEXNAME INIT INITIALERROR INITIALSTATE INPUTDATAHANDLER INPUTPORT INPUTSCHEMA INPUTTEXT INTERVALLENGTH INVARIANTTYPE ISNOOP ITEM ITEMSET ITERATIONS JDBC K KEEPINPUT KEYVALUEOUTPUT KPINAME KVEXPRESSIONS LANGUAGE LAZY_CONNECTION_CHECK LEARNER LEARNINGRATE LEFT_ATTRIBUTE LEFTHIGHPREDICATE LEFTLOWPREDICATE LINENUMBERS LOADNAME LOCALE LOGINNEEDED MAXBUFFERSIZE MAXELEMENTS MAXELEMENTSPERGROUP MAXRESULTS MAXTIMETOWAITFORNEWEVENTMS MAXTRAINSIZE MEASUREMENT MEASUREMENTNOISE MEMORY METHOD METRICS MILLISECOND MINFREQUENCE MINIMUMSIZE MINUTE MIXTURES MODE MONGODBNAME MONTH MULTITUPLEOUTPUT NAME NEWBROKER NGRAM NGRAMSIZE NGRAMUPTO NODE NOMINALS NO_OF_RECOMMENDATIONS NUMBERFORMATTER NUMCLUSTER NUMERICAL OFFSET ONECLASSIFIER ONEMATCHPERINSTANCE OPERATION OPTIONS OUTER OUTERJOIN OUTPUTDATAHANDLER OUTPUTMODE OVERLAPPINGPREDICATES OWNCOMPANY PAIRS PARSINGMETHOD PARTITION PARTS PASSWORD PATH PATHS PEERID PERCENTILES PIN PINSTATE PIPEID PORT POSITION_ATTRIBUTE_NAME PREDICATE PREDICATES PREDICATETYPE PROCESSNOISE PROTOCOL QUERY QUERYTRAJECTORY RANGES RATING READFIRSTLINE REALTIMEDELAY RECALCULATE RECOMMENDER RECORDDATARATE REFERENCESYSTEM RELATIVETOLERANCE REMOVALSTRATEGY REMOVESTOPWORDS RESULTTYPE RETURN RIGHT_ATTRIBUTE RIGHTHIGHPREDICATE RIGHTLOWPREDICATE ROUNDINGFACTOR ROUTING SAMESTARTTIME SAMPLERATE SAMPLES SCHEMA SCHEMANAME SCORINGFUNCTION SECOND SENDALWAYSHEARTBEAT SENDINGHEARTBEATS SERVICEMETHOD SHIFT SINK SINKNAME SINKPORT SINKTYPE SIZE SIZES SLICE SLIDE SOURCE SOURCENAME SPLITDECISION START STARTATCURRENTTIME STARTPREDICATE STARTTIMERAFTERFIRSTELEMENT STATISTICS STEMMWORDS STORAGENAME STORE SUBSETOFTERMS SUBTRAJECTORIES SUPPORT SUPPRESSCOUNTATTRIBUTE SUPPRESSERRORS SYSTEMTIME TABLE TABLESCHEMA TEMPNUMBER TESTSETTEXT TESTSETTRUEDECISION TEXTDELIMITER TEXTTOBECLASSIFIED THREADS THRESHOLD THRESHOLDVALUE TIME TIMEUNIT TIMEVALUE TIMEZONE TOLERANCE TOPICS TOPOLOGYTYPE TOTALQUANTITYOFTERMS TRAINSETMINSIZE TRAINSETTEXT TRAINSETTRUEDECISION TRAJECTORY_ID TRANSACTIONS TRANSITION TRANSPORT TRIM TRUNCATE TYPE TYPENAME UNIQUEKEYS UNIT UPDATERATE URL URLSUFFIX USE_DATATYPE_MAPPINGS USEMULTIPLE USER USERIDS USERNAMES USEUDP VALUE VALUEATTRIBUTE VARIABLES VARIANCE VERSION WAITEACH WAITFORALLCHANGED WITHMETADATA WRAPPER WRITERESOURCEUSAGE WSDLLOCATION X XATTRIBUTE XLENGTH Y YATTRIBUTE YEAR YLENGTH';

this.regexList = [
{ regex: /\/\/\/(.*)$/gm, css: 'comments' },// comments
{ regex: SyntaxHighlighter.regexLib.multiLineDoubleQuotedString, css: 'string' },// double quoted strings
{ regex: SyntaxHighlighter.regexLib.multiLineSingleQuotedString, css: 'string' },// single quoted strings
{ regex: /\s*#.*/gm, css: 'preprocessor' },// Odysseus Script
{ regex: new RegExp(this.getKeywords(operators), 'gmi'), css: 'keyword' },// PQL operator
{ regex: new RegExp(this.getKeywords(parameters), 'gmi'), css: 'color2' }// PQL operator parameter
];
this.forHtmlScript(SyntaxHighlighter.regexLib.scriptScriptTags);
};

Brush.prototype	= new SyntaxHighlighter.Highlighter();
Brush.aliases	= ['pql', 'PQL'];
SyntaxHighlighter.brushes.Pql = Brush;
// CommonJS
typeof(exports) != 'undefined' ? exports.Brush = Brush : null;
})();
