# Define the file name
file_name = input("Enter name of file in directory: ")

try:
    with open(file_name, "a") as file:
        file.write("Hello, world\n")
        print("Text appended to the existing file.")
except FileNotFoundError:
    with open(file_name, "w") as file:
        file.write("Hello, world\n")
        print("File created, and text written to it.")
except Exception as e:
    print("An error occurred:", str(e))
