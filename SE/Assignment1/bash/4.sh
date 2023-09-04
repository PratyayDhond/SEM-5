#!/bin/bash

read -p "Enter the radius of the circle: " radius

if [[ ! "$radius" =~ ^[0-9]+(\.[0-9]+)?$ ]]; then
  echo "Invalid input. Please enter a valid number for the radius."
  exit 1
fi


area=$(echo "3.14159 * $radius * $radius" | bc -l)
echo "The area of the circle with radius $radius is $area"
circumference=$(echo "2 * 3.14159 * $radius" | bc -l)
echo "The circumference of the circle with radius $radius is $circumference"
