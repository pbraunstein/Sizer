#!/usr/bin/env python
#
# Script to take two column csv file and switch out state names
# with abbreviations (I've done this manually too many times)
#

# Imports
from sys import argv
from sys import exit
import string

# CONSTANTS
MAP_FILE = "abbreviationMap.csv"

def main():
	if len(argv) != 2:
		print "Usage:", argv[0], "FILE_TO_ABBREVIATIZE"
		exit(1)

	mapDict = readInMap()

	readInWriteOut(argv[1], mapDict)


def readInWriteOut(input, mapDict):
	filew = open(getNewName(input), 'w')

	with open(input, 'r') as filer:
		for line in filer:
			if line.startswith("#"):
				filew.write(line)
			else:
				line = "".join(line.split()).upper()
				#10q http://stackoverflow.com/questions/265960/best-way-to-strip-punctuation-from-a-string-in-python
				exclude = set(string.punctuation)
				exclude.remove(",")  # Need this for csv parsing
				line = "".join(ch for ch in line if ch not in exclude)
				listL = line.split(",")
				listL[0] = mapDict[listL[0]]  # Swap magic
				filew.write(",".join(listL) + "\n")

	filew.close()


def getNewName(input):
	listL = input.split(".")

	return listL[0] + "_abbd." + listL[1]


def readInMap():
	mapDict = {}

	with open(MAP_FILE, 'r') as filer:
		for line in filer:

			if line.startswith("#"):
				continue

			line = "".join(line.split()).upper()

			mapDict[line.split(",")[0]] = line.split(",")[1]

	return mapDict


if __name__ == '__main__':
	main()
