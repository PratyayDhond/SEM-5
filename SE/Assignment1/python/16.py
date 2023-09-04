# Function to print a numeric pyramid
def numeric_pyramid(n):
    for i in range(1, n + 1):
        print(" " * (n - i), end="")
        for j in range(1, i + 1):
            print(j, end="")
        for j in range(i - 1, 0, -1):
            print(j, end="")
        print()

def special_character_pyramid(n):
    for i in range(1, n + 1):
        print(" " * (n - i), end="")
        for j in range(1, i + 1):
            print('*', end="")
        for j in range(i - 1, 0, -1):
            print('*', end="")
        print()


num_rows = int(input("Enter the number of rows for the pyramid: "))


print("Numeric Pyramid:")
numeric_pyramid(num_rows)


print("Special Character Pyramid:")
special_character_pyramid(num_rows)

       

