def is_leap_year(year):
    if (year % 4 == 0 and year % 100 != 0) or (year % 400 == 0):
        return True
    else:
        return False

leap_years = [year for year in range(2000, 2100) if is_leap_year(year)]
print("First 10 leap years starting from 2000:", leap_years[:10])
