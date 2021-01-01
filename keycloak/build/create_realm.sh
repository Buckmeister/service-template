#!/usr/bin/env bash

PATH=$PATH:$JBOSS_HOME/bin

declare -a KC_USERS="$KC_USERS"
declare -a KC_ROLES="$KC_ROLES"
declare -a KC_ROLE_ASSIGNMENTS="$KC_ROLE_ASSIGNMENTS"

eval KC_CLIENT_BASE_URL="$KC_CLIENT_BASE_URL"
eval KC_CLIENT_ROOT_URL="$KC_CLIENT_ROOT_URL"

echo
echo "Using the following Keycloak Details:"
echo "Realm:         $KC_REALM"
echo "Host:          $KEYCLOAK_HOST"
echo "Frontend Name: $KC_FRONTEND_NAME"
echo "Backend Name:  $KC_BACKEND_NAME"
echo
echo "Adding realm, clients, roles and users"

redirectUrisParam="redirectUris=[\"$KC_CLIENT_BASE_URL*\"]"
rootUrlParam="rootUrl=\"$KC_CLIENT_ROOT_URL\""
baseUrlParam="baseUrl=\"$KC_CLIENT_BASE_URL\""
webOriginsParam="webOrigins=["$KC_CLIENT_WEB_ORIGINS"]"

echo "Frontend client parameters:"
echo " -s $redirectUrisParam"
echo " -s $baseUrlParam"
echo " -s $rootUrlParam"
echo " -s $webOriginsParam"
echo

kcadm.sh config credentials \
  --server http://"$KEYCLOAK_HOST":8080/auth \
  --realm master \
  --user "$KEYCLOAK_USER" \
  --password "$KEYCLOAK_PASSWORD"

kcadm.sh create realms -s realm="$KC_REALM" -s enabled=true

kcadm.sh create clients \
  -r "$KC_REALM" \
  -s clientId="$KC_BACKEND_NAME" \
  -s publicClient=true

kcadm.sh create clients \
  -r "$KC_REALM" \
  -s clientId="$KC_FRONTEND_NAME" \
  -s publicClient=true \
  -s directAccessGrantsEnabled=true \
  -s "$webOriginsParam" \
  -s "$redirectUrisParam" \
  -s "$baseUrlParam" \
  -s "$rootUrlParam"

for kc_user in "${KC_USERS[@]}"; do
  IFS=':' read -ra fields <<<"$kc_user"

  username="${fields[0]}"
  password="${fields[1]}"

  kcadm.sh create users \
    -r "$KC_REALM" \
    -s username="$username" \
    -s enabled=true

  kcadm.sh set-password \
    -r "$KC_REALM" \
    --username "$username" \
    --new-password "$password"
done

for rolename in "${KC_ROLES[@]}"; do
  kcadm.sh create roles \
    -r "$KC_REALM" \
    -s name="$rolename"
done

for kc_role_assignment in "${KC_ROLE_ASSIGNMENTS[@]}"; do
  IFS=':' read -ra fields <<<"$kc_role_assignment"

  username="${fields[0]}"
  rolename="${fields[1]}"

  kcadm.sh add-roles \
    -r "$KC_REALM" \
    --uusername "$username" \
    --rolename "$rolename"
done

exit 0
