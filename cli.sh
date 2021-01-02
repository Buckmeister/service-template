#!/usr/bin/env bash

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
