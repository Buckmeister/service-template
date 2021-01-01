#!/usr/bin/env bash

if [ "$1" == "--help" ]; then
  shift
  echo
  echo "$0:"
  echo "Build and Boot all Services"
  echo
  echo "Usage:"
  echo "$0: [--rebuild | --repull | --prod | --help]"
  echo
  echo "--rebuild:  Rebuild all Services"
  echo "--repull:  Repull and Rebuild all Services"
  echo "--prod:  Build a production release"
  echo
  exit 0
fi

SCRIPT_PATH=$PWD
(command -v realpath >/dev/null 2>&1 && command -v dirname >/dev/null 2>&1) && {
  SCRIPT_PATH=$(realpath "$(dirname "${0}")")
}
echo
echo "[CMD]-> Changing directory to: $SCRIPT_PATH"
echo
cd "$SCRIPT_PATH" || exit 1

if [ "$1" == "--rebuild" ]; then
  REBUILD=true
  shift
  echo "[INFO]-> Using command line --rebuild"
fi

if [ "$1" == "--repull" ]; then
  REPULL=true
  shift
  echo "[INFO]-> Using command line --repull"
fi

if [ "$1" == "--prod" ]; then
  BUILD_TAG=prod
  shift
  echo "[INFO]-> Using command line --prod"
fi

SERVICE_NAME="$1"

DOCKER_COMPOSE_FILE="docker-compose.yml"
echo "[INFO]-> Using docker-compose file: $DOCKER_COMPOSE_FILE"
if [ "$BUILD_TAG" != "" ]; then
  ENV_FILE_TAG=".${BUILD_TAG}"
  DC_BUILD_TAG_CMD_ARGS="-f docker-compose${ENV_FILE_TAG}.yml"
  echo "[INFO]-> Using additional build tag specific docker-compose file: docker-compose${ENV_FILE_TAG}.yml"
fi

ENV_FILE=".env${ENV_FILE_TAG}"
if [ ! -f "$ENV_FILE" ]; then
  ENV_TEMPLATE=assets/env/env.template
  echo "[WARN]-> Env file \"$ENV_FILE\" does not exist"
  echo "[INFO]-> Copying \"$ENV_TEMPLATE\" to \"$ENV_FILE\""
  cp "$ENV_TEMPLATE" "$ENV_FILE"
fi
echo "[INFO]-> Using env file: $ENV_FILE"
echo "[CMD]-> Sourcing env file: $ENV_FILE"
source "$ENV_FILE"

DC_CMD_ARGS="-f $DOCKER_COMPOSE_FILE"
if [ "$DC_BUILD_TAG_CMD_ARGS" != "" ]; then
  DC_CMD_ARGS+=" $DC_BUILD_TAG_CMD_ARGS"
fi

ENV_CMD_ARGS="--env-file $ENV_FILE"

DOWN_CMD_ARGS="-v"
BUILD_CMD_ARGS="--force-rm -q --parallel"
UP_CMD_ARGS="--quiet-pull -d"

echo
echo " ..----------------.."
echo " .-[ Build System ].-"
echo " ..----------------.."
echo
echo " .-APP_NAME :->${APP_NAME}<-"
echo " .-BUILD_TAG:->${BUILD_TAG}<-"

if [ "$REBUILD" == "true" ]; then
  echo "[INFO]-> Using Rebuild Mode: $REBUILD"
  echo
  echo "Rebuilding all services"

  DOWN_CMD_ARGS+=" --rmi local"

elif [ "$REPULL" == "true" ]; then
  echo "[INFO]-> Using Repull Mode: $REPULL"
  echo
  echo "Repulling and rebuilding all services"

  DOWN_CMD_ARGS+=" --rmi local --remove-orphans"
  BUILD_CMD_ARGS+=" --pull"
fi

if [ "$SERVICE_NAME" != "" ]; then
  echo
  echo "[INFO]-> Rebuilding Service \"$SERVICE_NAME\""
  echo
  echo "[RUN]-> docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS stop $SERVICE_NAME"
  docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS stop "$SERVICE_NAME"
  echo
  echo "[RUN]-> docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS build $BUILD_CMD_ARGS $SERVICE_NAME"
  docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS build $BUILD_CMD_ARGS "$SERVICE_NAME"
  echo
  echo "[RUN]-> docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS up $UP_CMD_ARGS $SERVICE_NAME"
  docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS up $UP_CMD_ARGS "$SERVICE_NAME"
  exit 0
fi

echo
echo "[RUN]-> docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS down $DOWN_CMD_ARGS"
docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS down $DOWN_CMD_ARGS

echo
echo "[RUN]-> docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS build $BUILD_CMD_ARGS"
docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS build $BUILD_CMD_ARGS

echo
echo "[RUN]-> docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS up $UP_CMD_ARGS"
docker-compose $ENV_CMD_ARGS $DC_CMD_ARGS up $UP_CMD_ARGS

echo
echo "Waiting for Keycloak to finish booting up"
echo "(this might take up to 90 seconds.)"

cnt=0
kc_connected="false"
result="false"
echo -n "  STATUS:"
while [ "$kc_connected" == "false" ]; do
  command -v wget >/dev/null 2>&1 && {
    wgetOptions="-O - --quiet --spider -S --tries=2 --timeout=5"
    result=$(wget $wgetOptions $KEYCLOAK_FRONTEND_URL 2>&1 | grep "HTTP/" | awk '{print $2}')
    echo -n " $result "
    ((cnt = cnt + 1))

    if [ "$result" == "200" ]; then kc_connected="true"; fi
  }
  echo -n "-"
  ((cnt = cnt + 1))
  if [[ $cnt -gt 90 ]]; then
    echo "Continuing"
    break
  fi
  sleep 1s
done

KC_SETUP_SCRIPT="${SCRIPT_PATH}/keycloak/build/create_realm.sh"
echo
echo
echo "[RUN]-> $KC_SETUP_SCRIPT"
docker exec --env-file "$ENV_FILE" -i "${KEYCLOAK_CONTAINER}" /bin/bash <"$KC_SETUP_SCRIPT"
echo
echo "Keycloak Frontend URL: $KEYCLOAK_FRONTEND_URL"
echo "Keycloak Admin User:   $KEYCLOAK_USER"
echo "Keycloak Password:     $KEYCLOAK_PASSWORD"
echo
echo "Frontend URL:          $FRONTEND_URL"
echo "Frontend Users:"

for kc_user in "${KC_USERS[@]}"; do
  IFS=':' read -ra fields <<<"$kc_user"
  username="${fields[0]}"
  password="${fields[1]}"
  echo "Username:              $username"
  echo "Password:              $password"
done

echo
echo "Backend URL:           $BACKEND_URL"
echo "Swagger UI URL:        $SWAGGER_UI_URL"
