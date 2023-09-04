#!/bin/bash

read -p "Enter first  Number: " n1
read -p "Enter second Number: " n2
read -p "Enter third  Number: " n3

if (($n1 >= $n2 && $n1 >= $n3)); then
    largest=$n1
elif (($n2 >= $n1 && $n2 >= $n3)); then
    largest=$n2
else
    largest=$n3
fi

echo "The largest among the three is $largest"