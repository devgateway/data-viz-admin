#!/bin/sh
case "$1" in
	admin)
		JAVA_ARGS="$(tr '\n' ' ' <<-EOF
			-cp .:lib/*
			org.devgateway.toolkit.forms.wicket.FormsWebApplication
		EOF
		)"
		exec java $JAVA_ARGS $@
		;;
	*)
		exec $@
		;;
esac
