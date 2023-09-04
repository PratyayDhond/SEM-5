def findGrade(marks):
	s=""
	if(marks<0 or marks>100):
		print("NULL")
	elif(marks>=85):
		s="i division"
	elif(marks<85 and marks>60):
		s="ii division"
	elif(marks<=60 and marks>=40):
		s="iii division"
	else:
		s="FAIL"

	return s

n=int(input("Enter number of subjects: "))
i=0
subs=[]
m=[]
while i< n:
	sub_name=str(input("Enter the name of subject: "))
	marks=float(input("Enter marks (between 0-100 inclusive): "))
	subs.append(sub_name)
	m.append(marks)
	i+=1
i=0
print("\n-----------REPORT--------------\n")
while i< n:
	print("{}\t{}\t{}".format(subs[i],m[i],findGrade(m[i])))
	i+=1	
