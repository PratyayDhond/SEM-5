import filecmp

file1 = input("Enter the first file name: ")
file2 = input("Enter the second file name: ")

if filecmp.cmp(file1, file2):
    print("The contents of the two files are the same.")
else:
    print("The contents of the two files are different.")
