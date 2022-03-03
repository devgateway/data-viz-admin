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

