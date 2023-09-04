#!/bin/bash

read -p "Enter a number : " num

isPrime=1

if [ $num -le 1 ]; then
    echo "$num is neither Prime nor Composite" # 0 and 1 are not prime numbers
    return 0
fi

for ((i=2; i*i<=$num; i++)); do
    if [ $(( $num % $i )) -eq 0 ]; then
      isPrime=0
      break # Not a prime number
    fi
done

if [ $isPrime -eq 1 ]; then
    echo "$num is Prime"
else 
    echo "$num is Not Prime"
fi