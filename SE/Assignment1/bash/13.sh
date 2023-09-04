#!/bin/bash

read -p "Enter a string: " input

str=$(echo "$input" | tr -d ' ' | tr '[:upper:]' '[:lower:]')

# Reverse the string
rev=$(echo "$str" | rev)

if [ "$str" = "$rev" ]; then
    echo "The string \`$input\` is a palindrome."
else
    echo "The string `$input` is not a palindrome."
fi
