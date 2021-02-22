#!/usr/bin/env sh
SCRIPT_PATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 || exit ; pwd -P )"
CONF_PATH="kube-configs/dev-secret.yaml"
mkdir -p $SCRIPT_PATH/../kube-configs

if [ -e "$SCRIPT_PATH/../$CONF_PATH" ]; then
  echo "$CONF_PATH already exists. exiting..."
  exit 0
fi

MYSQL_ROOT_PASSWORD="password"
KEYCLOAK_RESOURCE="mytrack-srv"
KEYCLOAK_USER="admin"
KEYCLOAK_PASSWORD="admin"
KEYCLOAK_DB_USER="keycloak"
KEYCLOAK_DB_PASSWORD="password"
KEYCLOAK_DATABASE="keycloak"

cat <<EOF
Values used:
mysql.rootpassword=$MYSQL_ROOT_PASSWORD
keycloak.resource=$KEYCLOAK_RESOURCE
keycloak environment variables:
keycloak.user=$KEYCLOAK_USER
keycloak.password=$KEYCLOAK_PASSWORD
keycloak.dbuser=$KEYCLOAK_DB_USER
keycloak.dbpassword=$KEYCLOAK_DB_PASSWORD
keycloak.database=$KEYCLOAK_DATABASE
EOF

ENC_MYSQL_ROOT_PASSWORD=$(echo "$MYSQL_ROOT_PASSWORD" | tr -d '\n' | base64)
ENC_KEYCLOAK_RESOURCE=$(echo "$KEYCLOAK_RESOURCE" | tr -d '\n' | base64)
ENC_KEYCLOAK_USER=$(echo "$KEYCLOAK_USER" | tr -d '\n' | base64)
ENC_KEYCLOAK_PASSWORD=$(echo "$KEYCLOAK_PASSWORD" | tr -d '\n' | base64)
ENC_KEYCLOAK_DB_USER=$(echo "$KEYCLOAK_DB_USER" | tr -d '\n' | base64)
ENC_KEYCLOAK_DB_PASSWORD=$(echo "$KEYCLOAK_DB_PASSWORD" | tr -d '\n' | base64)
ENC_KEYCLOAK_DATABASE=$(echo "$KEYCLOAK_DATABASE" | tr -d '\n' | base64)

cat  > $CONF_PATH <<EOF
apiVersion: v1
kind: Secret
metadata:
  name: mytrack-srv-secret
type: Opaque
data:
  mysql.rootpassword: $ENC_MYSQL_ROOT_PASSWORD
  keycloak.resource: $ENC_KEYCLOAK_RESOURCE
  keycloak.user: $ENC_KEYCLOAK_USER
  keycloak.password: $ENC_KEYCLOAK_PASSWORD
  keycloak.dbuser: $ENC_KEYCLOAK_DB_USER
  keycloak.dbpassword: $ENC_KEYCLOAK_DB_PASSWORD
  keycloak.database: $ENC_KEYCLOAK_DATABASE
EOF

echo "$CONF_PATH generated successfully"
exit 0
