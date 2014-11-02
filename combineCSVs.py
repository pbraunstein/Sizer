#!/usr/bin/env python
#
# Among csv parameters, combines them into one nice csv file
# Also controls for files to be in different state orders
#

from sys import argv
from sys import exit

# CONSTANTS
OUT_FILE = "combined.csv"
#In files
IN_FILES = ["gdp2013.csv", "area_abbd.csv", "Obesity.csv", "population_abbd.csv", "AvgTemps.csv"]

def main():
	fileDicts= readInFiles()
	keys = getKeys(fileDicts[0])
	writeOut(fileDicts, keys)

# Reads in all files into dicts, puts each dict in a list
def readInFiles():
	toReturn = []

	for f in IN_FILES:
		dictio = {}

		with open(f, 'rU') as filer:
			for line in filer:
				line = line.strip()
				listL = line.split(",")
				
				if line.startswith("#"):  # Header
					dictio["#KEY"] = listL[1]  # What we are going to call this column

				else:  # Normal line
					dictio[listL[0]] = listL[1]

		toReturn.append(dictio)
	return toReturn


# Assembles list of keys and alphabetizes #KEY ends up first
def getKeys(inputDict):
	keys = inputDict.keys()
	toReturn = list(keys[:])  # Deep copy? Constructor? How do I make you happy?
	toReturn.sort()

	return toReturn


# Writes out the all the files using the keys list that has been alphabetized
# to make the header and everythang in the right order
def writeOut(fileDicts, keys):
	with open(OUT_FILE, 'w') as filew:
		for k in keys:
			toWrite = []
			for fd in fileDicts:
				try:
					toWrite.append(fd[k])
				except KeyError:
					print "key =", k
					print "dict =", fd["#KEY"]
					exit(1)

			filew.write(k + "," + ",".join(toWrite) + "\n")


if __name__ == '__main__':
	main()