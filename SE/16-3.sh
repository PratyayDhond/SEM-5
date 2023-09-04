#!/bin/bash

echo "Enter the number of rows for the pyramid: "
read n

for ((i = 1; i <= n; i++)); do
    for ((s = 1; s <= (n - i); s++)); do
        echo -n "  "
    done

    for ((j = 1; j <= i*2-1; j++)); do
        echo -n "* "
    done
    echo
done
