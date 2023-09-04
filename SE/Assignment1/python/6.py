op1=float(input("Enter operand 1: "))
op=(input("Enter operand: "))
op2=float(input("Enter operand 2: "))

if op=='+':
	print("RESULT: {}".format(op1+op2))
elif op=='-':
	print("RESULT: {}".format(op1-op2))
elif op=='*':
	print("RESULT: {}".format(op1*op2))
else:
	print("RESULT: {}".format(op1/op2))

