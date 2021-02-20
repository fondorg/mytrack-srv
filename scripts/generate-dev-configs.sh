#!/usr/bin/env sh
SCRIPT_PATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 || exit ; pwd -P )"
CONF_PATH="kube-configs/dev-configmap.yaml"

if [ -e "$SCRIPT_PATH/../$CONF_PATH" ]; then
  echo "$CONF_PATH already exists. exiting..."
  exit 0
fi

cat  > $CONF_PATH <<EOF
apiVersion: v1
kind: ConfigMap
metadata:
  name: mytrack-srv-config-file
data:
  application.properties: |
    keycloak.auth-server-url=http://keycloak:9999/auth
    spring.datasource.url=jdbc:mysql://mysql:3306/mytrack_srv
    keycloak.resource=mytrack-srv
EOF

echo "$CONF_PATH generated successfully"
exit 0
