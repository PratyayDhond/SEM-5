#!/bin/bash

calculate_grade() {
  percentage=$1
  if (( $(echo "$percentage >= 90" | bc -l) )); then
    echo "AA"
  elif (( $(echo "$percentage >= 8  0" | bc -l) )); then
    echo "AB"
  elif (( $(echo "$percentage >= 70" | bc -l) )); then
    echo "BB"
  elif (( $(echo "$percentage >= 60" | bc -l) )); then
    echo "BC"
  elif (( $(echo "$percentage >= 50" | bc -l) )); then
    echo "CC"
  elif (( $(echo "$percentage >= 40" | bc -l) )); then
    echo "CD"
  elif (( $(echo "$percentage >= 30" | bc -l) )); then
    echo "DD"
  else
    echo "FF"
  fi
}

read -p "Enter the number of students: " numberOfStudents
read -p "Enter the number of subjects: " numberOfSubjects

for ((i = 1; i <= numberOfStudents; i++)); do
  echo "Student $i:"
  total_marks=0

  for ((j = 1; j <= numberOfSubjects; j++)); do
    read -p "Enter marks for Subject $j: " marks
    total_marks=$((total_marks + marks))
  done

  percentage=$(echo "scale=2; $total_marks / $num_subjects" | bc -l)

  grade=$(calculate_grade $percentage)

  echo "Total Marks: $total_marks"
  echo "Percentage: $percentage%"
  echo "Grade: $grade"
  echo
done

