
CREATE STREAM stream1 (id INTEGER, first_name STRING)
  WRAPPER 'GenericPull'
  PROTOCOL 'CSV'
  TRANSPORT 'File'
  DATAHANDLER 'Tuple'
  OPTIONS('readfirstline' 'false','filename' 'C:\\Users\\jp\\Documents\\repositories\\odysseus\\server\\cql\\parser.novel.cql\\de.uniol.inf.is.odysseus.parser.novel.cql\\queries\\data1.csv');

CREATE STREAM stream2 (id2 INTEGER, first_name2 STRING)
  WRAPPER 'GenericPull'
  PROTOCOL 'CSV'
  TRANSPORT 'File'
  DATAHANDLER 'Tuple'
  OPTIONS('readfirstline' 'false','filename' 'C:\\Users\\jp\\Documents\\repositories\\odysseus\\server\\cql\\parser.novel.cql\\de.uniol.inf.is.odysseus.parser.novel.cql\\queries\\data1.csv');  
  
CREATE SINK out1 (attr1 INTEGER, attr2 STRING)
	WRAPPER 'GenericPush'
	PROTOCOL 'CSV'
	TRANSPORT 'File'
	DATAHANDLER 'Tuple'
	OPTIONS('filename' 'C:\\Users\\jp\\Documents\\repositories\\odysseus\\server\\cql\\parser.novel.cql\\de.uniol.inf.is.odysseus.parser.novel.cql\\queries\\out1.csv')
	
STREAM TO out1 SELECT COUNT(id) AS counter, id2 FROM stream1 [SIZE 10 SECONDS TIME], stream2 [SIZE 10 SECONDS TIME]	