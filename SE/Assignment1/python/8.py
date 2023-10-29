def findGrade(marks):
	str=""
	if(marks<0 or marks>100):
		print("NULL")
	elif(marks>=85):
		str="I division"
	elif(marks<85 and marks>60):
		str="II division"
	elif(marks<=60 and marks>=40):
		str="III division"
	else:
		str="FAIL"

	return str

subject = str(input("Enter Subject Name: "));
marks = float(input("Enter marks between (0-100 inclusive) : "));

print(f' For {subject} your grade is : {findGrade(marks)} ');