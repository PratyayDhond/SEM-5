#!/bin/bash

read -p "Enter the file name: " filename

if [ -e "$filename" ]; then
    while separator= read -r line; do # read 1 line at a time
        echo "$line"
    done < "$filename" # checks if file is read completely or not
else
    echo "File '$filename' does not exist."
fi
