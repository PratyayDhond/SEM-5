cp=float(input("Enter the cost price in rupees:\n"))
sp=float(input("Enter the selling price in rupees:\n"))
if(cp<sp):
	print("You are at a profit of {} rupees".format(sp-cp))
elif(sp==cp):
	print("No net profit of loss made!\n")
else:
	print("You have made a loss of {} rupees ".format(cp-sp))

