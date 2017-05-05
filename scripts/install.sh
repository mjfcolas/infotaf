#!/bin/sh
VERSION=
PATH_TO_JAR=/chemin/restapi-$VERSION.jar
PATH_TO_FRONT=/chemin
POSTSGRESQL_USER=
POSTSGRESQL_DB=

echo 'Arret du service'
service infotaf stop
echo 'suppression du service'
rm /etc/init.d/infotaf
echo 'creation du nouveau service'
cp infotaf /etc/init.d/infotaf
echo 'copie du package java'
cp restapi-$VERSION.jar $PATH_TO_JAR
echo 'Suppression du front'
rm -rf $PATH_TO_FRONT/*
echo 'copie du front'
cp front/* $PATH_TO_FRONT

echo 'maj de la BDD'
psql -U $POSTSGRESQL_USER -d $POSTSGRESQL_DB -a -f schema_bdd

echo 'demarrage du service'
service infotaf start
