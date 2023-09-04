file_name = input("Enter the file name: ")

try:
    with open(file_name, 'r') as file:
        for line in file:
            print(line, end='')
except FileNotFoundError:
    print(f"File '{file_name}' not found.")
except Exception as e:
    print(f"An error occurred: {str(e)}")
