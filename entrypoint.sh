#!/bin/bash

PROP_FILE="/etc/$1.properties"
truncate -s 0 $PROP_FILE
echo "..................... Writing to $PROP_FILE: ............... "

while IFS='=' read -r -d '' n v; do
    if [[ $n == SPRING* ]]; then
        VAR_NAME="$(echo "$n" | tr '[:upper:]_' '[:lower:].')"
        echo "$VAR_NAME=$v" >> $PROP_FILE
    fi
done < <(env -0)

while IFS='=' read -r -d '' n v; do
    if [[ $n == TCDI_* ]]; then
        VAR_NAME="$(echo "$n" | tr '[:upper:]_' '[:lower:].' | cut -c 6-)"
        echo "$VAR_NAME=$v" >> $PROP_FILE
    fi
done < <(env -0)

# Add git.branch if it exists
if [[ -n "${git.branch}" ]]; then
    echo "git.branch=${git.branch}" >> $PROP_FILE
fi

echo "................. Properties ................."
cat $PROP_FILE
echo "................. End Properties ................."

# Set up correct classpath to include all dependencies
JAVA_OPTS="-Dspring.config.location=file://$PROP_FILE"

# Run the application with the correct classpath
cd /opt/devgateway/tcdi/admin/deps
exec java -cp .:BOOT-INF/classes:BOOT-INF/lib/* org.devgateway.toolkit.forms.wicket.FormsWebApplication $JAVA_OPTS $@

