import re

def allJson(l):
	count = 1 
	print '['
	for one in l:
		print '{'
		print "\"name\":\"%s\"," % one[1]
		print "\"path\":\"%s\"," % one[0]
		print "\"singer\":\"%s\"" % one[2]
		print '}'
		if(count!=len(l)):
			print ','
		count=count+1
	print ']'

l = []
count = 0
pathPattern = r'<m_szPath>(.*)</m_szPath>'
namePattern = r'<m_szSongName>(.*)</m_szSongName>'
artistPattern = r'<m_szArtist>(.*)</m_szArtist>'


fpath = open('songPath.txt','r');
fName = open('songName.txt','r');
fArtist = open('songArtist.txt','r');
lpath = fpath.readline()
lname = fName.readline()
lArtist = fArtist.readline()

while lpath and lname and lArtist:
	srpath = re.search(pathPattern,lpath,re.M|re.I|re.S)
	srname = re.search(namePattern,lname,re.M|re.I|re.S)
	srartist = re.search(artistPattern,lArtist,re.M|re.I|re.S)
	if srpath and srname and srartist:
		count=count+1
		l.append((srpath.group(1),srname.group(1),srartist.group(1)))
	else:
		print "not found"
		break
	lpath = fpath.readline()
	lname = fName.readline()
	lArtist = fArtist.readline()

fpath.close()
fName.close()
fArtist.close()


allJson(l)

