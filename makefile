.PHONI := all get_class run clear

all : get_class

get_class :
	javac src/r0p3/*.java -d ./bin

run :
	java -classpath ./bin/ r0p3.Main

clean :
	rm ./bin/r0p3/*.class


gui :
	javac -classpath ./src/  src/**/*.java -d ./build

grun :
	java -classpath ./build/ r0p3GUI.Main

gclean :
	rm ./bin/r0p3GUI/*.class
