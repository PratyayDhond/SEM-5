#!/bin/bash

read -p "Enter a name for the file: " fileName

# init file
cat <<EOF > "$fileName"
Cat
dog
bear
hello
elephant
hello
tiger
hello
horse
EOF

# og content
echo "File contents before removing lines with 'hello':"
cat "$fileName"

# The grep -v command is used to invert the match in the grep command,
# which means it selects all lines that do NOT match the specified pattern.
grep -v 'hello' "$fileName" > temp.txt

mv temp.txt "$fileName"

echo "File contents after removing lines with 'hello':"
cat "$fileName"
