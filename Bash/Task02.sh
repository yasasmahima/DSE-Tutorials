#!/bin/bash
javac Task01.java
java Task01

echo $?

if [ $? -eq 0 ]
then 
	echo "Successful"
else
	echo "Error"
fi
