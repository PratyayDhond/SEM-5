#!/bin/bash

echo "Enter the student's score: "
read score

# Convert score to an integer
score=$(($score))

case $score in
    [8-9][0-9]*|100)
        result="Division 1"
        ;;
    [6-7][0-9]*)
        result="Division 2"
        ;;
    [4-5][0-9]*)
        result="Division 3"
        ;;
    [1-3][0-9]|[0-9]*)
        result="Fail"
        ;;
    *)
        result="Invalid Score"
        ;;
esac

echo "Result: $result"
