CREATE STREAM stream1 (id INTEGER, first_name STRING, last_name STRING, email STRING, gender STRING, ip_adress STRING)
  WRAPPER 'GenericPush'
  PROTOCOL 'CSV'
  TRANSPORT 'TCPClient'
  DATAHANDLER 'Tuple'
  OPTIONS('filename' 'data1');
  
SELECT value FROM stream1
WHERE 10 > value;