#!/bin/bash

read -p "Enter a number: " number
factorial=1

if [ $number -lt 0 ]; then
    echo "Factorial is undefined."
elif [ $number -eq 0 ]; then
    echo "Factorial of 0 is 1."
else
    # Calculate the factorial using a loop
    for ((i = 2; i <= number; i++)); do
        factorial=$((factorial * i))
    done

    echo "Factorial of $number is $factorial."
fi
