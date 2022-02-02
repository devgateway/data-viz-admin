#!/bin/sh


	PROP_FILE="tcdi-admin.properties"
	echo "Writing to $PROP_FILE:"

	PROPERTIES="$(cat <<-EOF
		server.port
		spring.application.name
		spring.liquibase.enabled
		spring.datasource.jdbc-url
		spring.datasource.url
		spring.mail.host
	EOF
	)"

	echo "$PROPERTIES" | while IFS=read PROPERTY; do
		VAR_NAME="$(echo "$PROPERTY" | tr '[:lower:].-' '[:upper:]__')"
		eval VALUE="\$$VAR_NAME"
		if [ -n "$VALUE" ]; then
			echo "$PROPERTY=$VALUE"
		fi
	done | tee -a "$PROP_FILE"

	JAR="tcdi-admin-forms-0.0.1-SNAPSHOT.jar"
	JAVA_OPTS="-Dspring.config.location=file://$PROP_FILE"
	#exec /bin/sh -c "java -jar '$JAR' $JAVA_OPTS $@" nobody
	exec /bin/bash
	;;
*)
	exec $@
	;;