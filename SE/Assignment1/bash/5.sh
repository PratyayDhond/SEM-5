#!/bin/bash

read -p "Enter marks for Subject 1: " subject1

read -p  "Enter marks for Subject 2: " subject2

read -p "Enter marks for Subject 3: " subject3

total_marks=$((subject1 + subject2 + subject3))

if [ "$total_marks" -ge 240 ]; then
  division="Division 1"
elif [ "$total_marks" -ge 180 ]; then
  division="Division 2"
elif [ "$total_marks" -ge 120 ]; then
  division="Division 3"
else
  division="Fail"
fi

echo "Total Marks: $total_marks"
echo "Result: $division"
