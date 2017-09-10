import re

pathPattern = r'<m_szPath>(.*)</m_szPath>'
pattern2 = r'(.*)\n'

littlePath = open("songPath.txt",'r')
morePath = open("545path.txt")

line1 = littlePath.readline()
line2 = morePath.readline()

littleArray = []
duoyu = []

while line1:
	line1Path = re.search(pathPattern,line1,re.I|re.M|re.S)
	if line1Path:
		littleArray.append(line1Path.group(1))
	else:
		print "not fount %s" % line1,line1Path.group()
		break
	line1 =littlePath.readline()


while line2:
	songname = re.search(pattern2,line2,re.I|re.M|re.S)
	if songname:
		if songname.group(1) not in littleArray:
			duoyu.append(songname.group(1))
	else:
		print 'not fount'
		break;
	line2 = morePath.readline()

for sn in duoyu:
	print "{"
	print "\"name\":\"%s\"," % sn
	print "\"path\":\"%s\"," % sn
	print "\"singer\":\"%s\"" % sn
	print "},"

