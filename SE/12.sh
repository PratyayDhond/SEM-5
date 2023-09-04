#!/bin/bash

year=2000

count=0

echo "List of Leap Years starting from the year 2000:"

while [ $count -lt 10 ]; do
    # Check if the year is a leap year
    if [ $((year % 4)) -eq 0 ] && [ $((year % 100)) -ne 0 ] || [ $((year % 400)) -eq 0 ]; then
        count=$((count + 1))
        echo "$count : $year"
    fi
    year=$((year + 1))
done
