CREATE STREAM stream1 (id INTEGER, first_name STRING, last_name STRING, email STRING, gender STRING, ip_adress STRING)
  WRAPPER 'GenericPull'
  PROTOCOL 'CSV'
  TRANSPORT 'File'
  DATAHANDLER 'Tuple'
  OPTIONS('readfirstline' 'false','filename' 'C:\\Users\\jp\\Documents\\repositories\\odysseus\\server\\cql\\parser.novel.cql\\de.uniol.inf.is.odysseus.parser.novel.cql\\queries\\data1.csv');   

CREATE SINK out1 (id INTEGER, first_name STRING, last_name STRING)
	WRAPPER 'GenericPush'
	PROTOCOL 'CSV'
	TRANSPORT 'File'
	DATAHANDLER 'Tuple'
	OPTIONS('filename' 'C:\\Users\\jp\\Documents\\repositories\\odysseus\\server\\cql\\parser.novel.cql\\de.uniol.inf.is.odysseus.parser.novel.cql\\queries\\out1.csv')
	
STREAM TO out1 SELECT id FROM stream1 [SIZE 5 SECONDS TIME] WHERE id > 10