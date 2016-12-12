CREATE STREAM stream1 (id INTEGER, first_name STRING, last_name STRING, email STRING, gender STRING, ip_adress STRING)
  WRAPPER 'GenericPull'
  PROTOCOL 'CSV'
  TRANSPORT 'File'
  DATAHANDLER 'Tuple'
  OPTIONS('readfirstline' 'false','filename' 'C:\\Users\\jp\\Documents\\repositories\\odysseus\\server\\cql\\parser.novel.cql\\de.uniol.inf.is.odysseus.parser.novel.cql\\queries\\data1.csv');
  
SELECT id FROM stream1 WHERE id > 10   



CREATE SINK out1 (id INTEGER, first_name STRING, last_name STRING)
WRAPPER 'GenericPush'
PROTOCOL 'CSV'
TRANSPORT 'File'
DATAHANDLER 'Tuple'
OPTIONS('filename' 'C:\\Users\\jp\\Documents\\repositories\\odysseus\\server\\cql\\parser.novel.cql\\de.uniol.inf.is.odysseus.parser.novel.cql\\queries\\out1.csv')

CREATE VIEW view1 FROM (SELECT first_name, last_name FROM stream1 WHERE id > 10)
