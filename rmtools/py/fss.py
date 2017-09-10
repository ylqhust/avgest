import re

mp3 = open("mp3.txt",'r')
pattern = r'<(.*)\.mp3'

line = mp3.readline()

while line:
	rs = re.search(pattern,line,re.I|re.M|re.S)
	if(rs):
		print rs.group(1)
	else:
		print 'not fount'
	line = mp3.readline()

mp3.close()