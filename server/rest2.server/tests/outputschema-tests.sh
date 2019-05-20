#!/usr/bin/env bash

err_report() {
	echo "Line $1 failed."
	exit 1
}

trap 'err_report $LINENO' ERR


read -r -d '' QUERYTEXT << EOM || true
person = ACCESS({
              source='person',
              wrapper='GenericPush',
              transport='TCPClient',
              protocol='SizeByteBuffer',
              datahandler='Tuple',
              options=[['host', 'localhost'],['port', '65440']],
              schema=[
                ['timestamp', 'STARTTIMESTAMP'],
                ['id', 'INTEGER'],
                ['name', 'STRING'],
                ['email', 'STRING'],
                ['creditcard', 'STRING'],
                ['city', 'STRING'],
                ['state', 'STRING']
              ]
            }
          )
EOM

read -r -d '' POSTDATA << EOM || true
{
	"parser": "PQL",
	"queryText": "$QUERYTEXT"
}
EOM

curl -s --fail -H "Content-Type:application/json" -X POST --data "$POSTDATA" -u System:manager localhost:8888/services/outputschema | jq

