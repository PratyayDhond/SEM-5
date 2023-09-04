#!/bin/bash

read -p "Enter name of the directory: " directoryName

if [ -d "$directoryName" ]; then
    echo "Directory '$directoryName' already exists."
else
    mkdir "$directoryName"
    echo "Directory '$directoryName' created."
fi
