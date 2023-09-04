#!/bin/bash

echo "Lightning Calculator"
echo "-----------------"

# Input first operand
read -p "Enter the first operand: " op1

# Input operator
read -p "Enter the operator (+, -, *, /): " operator

# Input second operand
read -p "Enter the second operand: " op2

# Perform arithmetic operations based on the operator
case "$operator" in
  "+")
    result=$((op1 + op2))
    ;;
  "-")
    result=$((op1 - op2))
    ;;
  "*")
    result=$((op1 * op2))
    ;;
  "/")
    if [ "$op2" -eq 0 ]; then
      echo "Error: Division by zero is not allowed."
      exit 1
    fi
    result=$((op1 / op2))
    ;;
  *)
    echo "Error: Invalid operator. Please use +, -, *, or /."
    exit 1
    ;;
esac

echo "Result: $op1 $operator $op2 = $result"
