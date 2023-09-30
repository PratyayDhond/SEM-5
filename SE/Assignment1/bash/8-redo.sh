#!/bin/bash

# Function to calculate division based on total marks
calculate_division() {
    total_marks=$1
    if ((total_marks >= 80)); then
        division="I"
    elif ((total_marks >= 60)); then
        division="II"
    elif ((total_marks >= 40)); then
        division="III"
    elif ((total_marks )); then
        division="Fail"
    else
        division="Fail"
    fi
    echo "$division"
}

# Initialize variables
declare -A student_marks
declare -A student_divisions

# Input the number of students and subjects
read -p "Enter the number of students: " num_students
read -p "Enter the number of subjects: " num_subjects

# Input marks for each student and subject
for ((i = 1; i <= num_students; i++)); do
    echo "Student $i:"
    for ((j = 1; j <= num_subjects; j++)); do
        read -p "Enter marks for Subject $j: " marks
        student_marks["student_${i}_subject_${j}"]=$marks
    done
done

# Calculate division for each student
for ((i = 1; i <= num_students; i++)); do
    total_marks=0
    for ((j = 1; j <= num_subjects; j++)); do
        mark_key="student_${i}_subject_${j}"
        total_marks=$((total_marks + student_marks[$mark_key]))
    done
    division=$(calculate_division $total_marks)
    student_divisions["student_$i"]=$division
done

# Display divisions for each student
echo "Student Divisions:"
for ((i = 1; i <= num_students; i++)); do
    echo "Student $i: ${student_divisions["student_$i"]}"
done
