#!/usr/bin/env sh
SCRIPT_PATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 || exit ; pwd -P )"
CONF_PATH="kube-configs/dev-secret.yaml"
mkdir -p $SCRIPT_PATH/../kube-configs

if [ -e "$SCRIPT_PATH/../$CONF_PATH" ]; then
  echo "$CONF_PATH already exists. exiting..."
  exit 0
fi

MYSQL_USERNAME="root"
MYSQL_PASSWORD="password"
KEYCLOAK_RESOURCE="mytrack-srv"

cat <<EOF
Values used:
mysql.username=$MYSQL_USERNAME
mysql.password=$MYSQL_PASSWORD
keycloak.resource=$KEYCLOAK_RESOURCE
EOF

ENC_MYSQL_USERNAME=$(echo "$MYSQL_USERNAME" | tr -d '\n' | base64)
ENC_MYSQL_PASSWORD=$(echo "$MYSQL_PASSWORD" | tr -d '\n' | base64)
ENC_KEYCLOAK_RESOURCE=$(echo "$KEYCLOAK_RESOURCE" | tr -d '\n' | base64)

cat  > $CONF_PATH <<EOF
apiVersion: v1
kind: Secret
metadata:
  name: mytrack-srv-secret
type: Opaque
data:
  mysql.username: $ENC_MYSQL_USERNAME
  mysql.password: $ENC_MYSQL_PASSWORD
  keycloak.resource: $ENC_KEYCLOAK_RESOURCE
EOF

echo "$CONF_PATH generated successfully"
exit 0
