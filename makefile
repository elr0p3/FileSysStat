.PHONI := all get_class run clear

all : get_class

get_class :
	javac src/r0p3/*.java

run :
	java -classpath ./src/ r0p3.Main

clean :
	rm src/r0p3/*.class
