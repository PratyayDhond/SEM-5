with open("animals.txt", "r") as file:
    lines = file.readlines()

filtered_lines = [line for line in lines if "hello" not in line]

with open("animals.txt", "w") as file:
    file.writelines(filtered_lines)

# Display the modified file
with open("animals.txt", "r") as file:
    modified_content = file.read()

print("Modified content:")
print(modified_content)