def max(a,b):
	if a> b:
		return a
	return b

a=float(input("Enter num1: "))
b=float(input("Enter num2: "))
c=float(input("Enter num3: "))

print("{}".format(max(a,max(b,c))))
