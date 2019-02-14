#!/usr/bin/env bash

err_report() {
	echo "Line $1 failed."
	exit 1
}

trap 'err_report $LINENO' ERR


test() {
	RESULT="$1"
	EXPECTED="$2"
	MSG="$3"
	if [ "$RESULT" != "$EXPECTED" ]; then
		echo "Failed:   $MSG"
		echo "Result:   $RESULT"
		echo "Expected: $EXPECTED"
		false
	fi
}


RESULT=$(curl -s --fail -u System:manager localhost:8888/queries)
test "$RESULT" "[]" "Test empty queries list"

RESULT=$(curl -s -o /dev/null -w "%{http_code}" -u System:manager localhost:8888/queries/1)
test "$RESULT" "404" "Test query by ID not found 404 status code"

RESULT=$(curl -s -o /dev/null -w "%{http_code}" -u System:manager localhost:8888/queries/abc)
test "$RESULT" "404" "Test query by name not found 404 status code"

RESULT=$(curl -s -o /dev/null -w "%{http_code}" -H "Content-Type:application/json" -X POST --data "{}" -u System:manager localhost:8888/queries)
test "$RESULT" "400" "Test post empty query"


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

RESULT=$(curl -s -o /dev/null -w "%{http_code}" -H "Content-Type:application/json" -X POST --data "$POSTDATA" -u System:manager localhost:8888/queries)
test "$RESULT" "200" "Test adding query without name"

RESULT=$(curl -s -o /dev/null -w "%{http_code}" -u System:manager localhost:8888/queries/0)
test "$RESULT" "200" "Test get query 0"


read -r -d '' QUERYTEXT << EOM || true
person2 = ACCESS({
              source='person2',
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
	"queryText": "$QUERYTEXT",
	"name": "myquery"
}
EOM

RESULT=$(curl -s -o /dev/null -w "%{http_code}" -H "Content-Type:application/json" -X POST --data "$POSTDATA" -u System:manager localhost:8888/queries)
test "$RESULT" "200" "Test adding query with name"

RESULT=$(curl -s -o /dev/null -w "%{http_code}" -u System:manager localhost:8888/queries/1)
test "$RESULT" "200" "Test get query 1"

RESULT=$(curl -s -o /dev/null -w "%{http_code}" -u System:manager localhost:8888/queries/myquery)
test "$RESULT" "200" "Test get query 'myquery'"
