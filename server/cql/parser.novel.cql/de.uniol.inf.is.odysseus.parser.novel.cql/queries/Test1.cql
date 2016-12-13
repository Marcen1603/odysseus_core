
CREATE STREAM stream1 (id INTEGER, first_name STRING, last_name STRING, email STRING, gender STRING, ip_adress STRING)
  WRAPPER 'GenericPull'
  PROTOCOL 'CSV'
  TRANSPORT 'File'
  DATAHANDLER 'Tuple'
  OPTIONS('readfirstline' 'false','filename' 'C:\\Users\\jp\\Documents\\repositories\\odysseus\\server\\cql\\parser.novel.cql\\de.uniol.inf.is.odysseus.parser.novel.cql\\queries\\data1.csv');

  
  
SELECT * FROM stream1 WHERE 1 > id 

SELECT id FROM stream1 WHERE 10 == id