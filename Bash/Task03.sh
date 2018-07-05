#!/bin/bash
read var

printf "%g" "$var" &> /dev/null
if [[ $? == 0 ]] ; then
    echo "$var is a number."
else
    echo "$var is not a number."
fi
