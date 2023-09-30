#!/bin/bash

read -p "Enter name of the directory: " directoryName

if [ -d "$directoryName" ]; then
    echo "Directory '$directoryName' already exists."
else
    mkdir "$directoryName"

    if [ $? -eq 0 ]; then
        echo "Directory '$directoryName' created."
    else
        echo "There was some issue creating the directory with title '$directoryName'"
    fi
fi
