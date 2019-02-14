#!/usr/bin/env bash

err_report() {
	echo "One auth failed on line $1"
	exit 1
}

trap 'err_report $LINENO' ERR

echo "Testing correct logins. Should output nothing."

# Basic Auth
curl -s --fail --header "Content-Type: application/json" -u System:manager -j localhost:8888/datatypes > /dev/null

# Session
curl -s --fail --header "Content-Type: application/json" -u System:manager -j -c cookies.txt localhost:8888/datatypes > /dev/null
curl -s --fail --header "Content-Type: application/json" -b cookies.txt localhost:8888/datatypes > /dev/null

# Bearer
TOKEN=$(curl -s --fail --header "Content-Type: application/json" -j --request POST --data '{"username": "System", "password": "manager"}' localhost:8888/services/login)
curl -s --fail --header "Content-Type: application/json" -j --header "Authorization: Bearer $TOKEN" localhost:8888/datatypes > /dev/null

echo "Done."


trap "echo 'Wrong auth failed as expected. Fine.'" ERR

echo "Testing wrong logins. Should output message on success."
curl -s --fail --header "Content-Type: application/json" -u unkown:user localhost:8888/datatypes > /dev/null
echo "Done."