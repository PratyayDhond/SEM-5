#!/bin/bash

echo "Enter the number of rows for the pyramid: "
read n

for ((i = 1; i <= n; i++)); do
    for ((s = 1; s <= (n - i); s++)); do
        echo -n "  "
    done

    for ((j = 1; j <= i; j++)); do
        echo -n "$j "
    done

    for ((k = (i - 1); k >= 1; k--)); do
        echo -n "$k "
    done

    echo
done
