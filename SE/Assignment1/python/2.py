num=int(input("Enter a number to check if it's prime or not: "))

i=2
answers=["is not a Prime", "is a prime"]
flag=1

if num==0 or num==1:
	print("The number is neither prime nor composite\n")
else:
	while i*i <= num:
		if(num%i==0):
			flag=0
			break

		i+=1

	print(f'{num} {answers[flag]}')
	print("\n")
