#!/bin/bash

read -p "Enter fileName: " fileName  # Replace with the desired file path

if [ -e "$fileName" ]; then
    echo "Hello World!" >> "$fileName"
    echo "Appended 'Hello World' to $fileName"
else
    echo "Hello World!" > "$fileName"
    echo "Created $fileName with 'Hello World!'"
fi
