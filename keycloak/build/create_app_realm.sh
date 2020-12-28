#!/usr/bin/env bash

PATH=${PATH}:${JBOSS_HOME}/bin

echo
echo "Using the following Keycloak Details:"
echo "Realm:         ${KC_REALM}"
echo "Host:          ${KEYCLOAK_HOSTNAME}"
echo "Role:          ${KC_ROLE_NAME}"
echo "User:          ${KC_USER_NAME}"
echo "Frontend Name: ${KC_FRONTEND_NAME}"
echo "Backend Name:  ${KC_BACKEND_NAME}"
echo
echo "Adding realm,clients, role and user"

redirectUrisParam="redirectUris=[\"${KC_CLIENT_BASE_URL}*\"]"
rootUrlParam="rootUrl=\"${KC_CLIENT_ROOT_URL}\""
baseUrlParam="baseUrl=\"${KC_CLIENT_BASE_URL}\""
webOriginsParam="webOrigins=[\"*\"]"
echo "Frontend client parameters:"
echo " -s ${redirectUrisParam}"
echo " -s ${baseUrlParam}"
echo " -s ${rootUrlParam}"
echo " -s ${webOriginsParam}"
echo

kcadm.sh config credentials \
  --server http://${KEYCLOAK_HOSTNAME}:8080/auth \
  --realm master \
  --user ${KEYCLOAK_USER} \
  --password ${KEYCLOAK_PASSWORD}

kcadm.sh create realms -s realm=${KC_REALM} -s enabled=true

kcadm.sh create clients \
  -r ${KC_REALM} \
  -s clientId=${KC_BACKEND_NAME} \
  -s publicClient=true

kcadm.sh create clients \
  -r ${KC_REALM} \
  -s clientId=${KC_FRONTEND_NAME} \
  -s publicClient=true \
  -s directAccessGrantsEnabled=true \
  -s ${webOriginsParam} \
  -s ${redirectUrisParam} \
  -s ${baseUrlParam} \
  -s ${rootUrlParam}

kcadm.sh create roles \
  -r ${KC_REALM} \
  -s name=${KC_ROLE_NAME} \
  -s 'description=App Admin'

kcadm.sh create users \
  -r ${KC_REALM} \
  -s username=${KC_USER_NAME} \
  -s enabled=true

kcadm.sh set-password \
  -r ${KC_REALM} \
  --username ${KC_USER_NAME} \
  --new-password ${KC_USER_PASSWORD}

kcadm.sh add-roles \
  -r ${KC_REALM} \
  --uusername ${KC_USER_NAME} \
  --rolename ${KC_ROLE_NAME}

echo
