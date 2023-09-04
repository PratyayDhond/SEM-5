x=int(input("Enter a number to check if even or odd: "))

answer=["is even\n", "is odd\n"]

result =str(x)+" "+answer[x%2]

print(result)
