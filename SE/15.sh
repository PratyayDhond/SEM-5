#!/bin/bash

read -p "Enter first fileName: " file1
read -p "Enter second fileName: " file2

if cmp -s "$file1" "$file2"; then
    echo "The contents of $file1 and $file2 are the same."
else
    echo "The contents of $file1 and $file2 are different."
fi
