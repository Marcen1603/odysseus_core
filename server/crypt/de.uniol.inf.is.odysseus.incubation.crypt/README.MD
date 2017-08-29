<h1>Installation Instructions of OdysseusCrypt</h1>

1. Setup secret Database on every Datasource
	1.1. Install MySQL
	1.2. Create a new user "odysseus" with password "suessydo" (This are the default settings. You can change them.)
	1.3. Run ./server/crypt/de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.secret/DBdump/Secret.sql
		This will create the database and the tables, to store symmetric keys.

2. Setup public Database in the Cloud
	2.1. Install MySQL
	2.2. Create a new user "odysseus" with password "suessydo" (This are the default settings. You can change them.)
	2.3. Authorize the new user to connect from any client.
	2.4. Run ./server/crypt/de.uniol.inf.is.odysseus.incubation.crypt.keymanagement/Public.sql
		This will create the databases for the keymanagement server.

3. Setup the newest version of Odysseus-Server on every Datasource, Datasink and Cloud-Machine.
	3.1. Install the Odysseus-Server
	3.2. Install the CryptClient-Feature on the Datasource, Datasinks and the machines in the cloud.
	3.3. Install the CryptServer-Feature on the Keymanagement-Server in the Cloud.