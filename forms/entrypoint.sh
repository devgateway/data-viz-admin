#!/bin/sh

 		PROP_FILE="/etc/$1.properties"
		echo "Writing to $PROP_FILE:"
    truncate -s 0 $PROP_FILE

	echo "..................... Writing to $PROP_FILE: ............... "

		PROPERTIES="$(cat <<-EOF
			server.port
			spring.application.name
			spring.liquibase.enabled
			spring.datasource.url
			spring.datasource.jdbc.url
			spring.datasource.username
			spring.datasource.password
			spring.jpa.hibernate.ddl.auto


		EOF
		)"

		echo "$PROPERTIES" | while IFS= read PROPERTY; do
			VAR_NAME="$(echo "$PROPERTY" | tr '[:lower:].-' '[:upper:]__')"
			eval VALUE="\$$VAR_NAME"
			if [ -n "$VALUE" ]; then
				echo "$PROPERTY=$VALUE"
			fi
		done | tee -a "$PROP_FILE"
    echo "................. Properties ................."
    cat $PROP_FILE
    echo "................. End sou ................."
    JAVA_ARGS="$(tr '\n' ' ' <<-EOF
			-cp .:lib/*
			org.devgateway.toolkit.forms.wicket.FormsWebApplication
		EOF
		)"

	JAVA_OPTS="-Dspring.config.location=file://$PROP_FILE"
 	exec java $JAVA_ARGS  $JAVA_OPTS $@ nobody
	;;
*)
	exec $@
	;;

