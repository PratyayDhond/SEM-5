content = """Cat
dog
bear
hello
elephant
hello
tiger
hello
horse"""

# Write the content to a file named "animals.txt"
with open("animals.txt", "w") as file:
    file.write(content)