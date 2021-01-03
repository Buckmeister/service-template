#!/usr/bin/env bash

prereqs_missing() {
  echo
  echo "Prerequisites missing!"
  echo
  echo "In order to use that script, you need to install"
  echo "HTTPie (including the httpie-jwt-auth plugin) and jq."
  echo
  echo "On Debian based distributions, you can issue:"
  echo
  echo "sudo apt-get install httpie jq"
  echo "pip3 -U install httpie-jwt-auth"
  echo
  echo "On MacOS, you can install via brew by issuing:"
  echo
  echo "brew install httpie jq"
  echo "pip3 -U install httpie-jwt-auth"
  echo
  exit 1
}

command -v jq >/dev/null 2>&1 || prereqs_missing
command -v http >/dev/null 2>&1 || prereqs_missing
pip3 list 2>/dev/null | grep httpie-jwt-auth >/dev/null || prereqs_missing

[ -r .env.cli ] && source .env.cli

echo
[ -z "$AUTH_ENDPOINT" ] && read -rp 'Auth Endpoint: ' AUTH_ENDPOINT
[ -z "$KC_CLIENT_ID" ] && read -rp 'Client ID: ' KC_CLIENT_ID
[ -z "$KC_USERNAME" ] && read -rp 'Username: ' KC_USERNAME
[ -z "$KC_PASSWORD" ] && read -srp 'Password: ' KC_PASSWORD
echo
echo

export JWT_AUTH_TOKEN=$(http -f POST "$AUTH_ENDPOINT" grant_type=password client_id="$KC_CLIENT_ID" username="$KC_USERNAME" password="$KC_PASSWORD" 2>/dev/null | jq -r '.access_token')

http --auth-type=jwt $*
