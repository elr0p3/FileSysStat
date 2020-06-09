.PHONI := all gui run gclean tui trun clean

all : gui


gui :
	javac -classpath ./src/  src/**/*.java -d ./build

run :
	java -classpath ./build/ r0p3GUI.Main

gclean :
	rm -rf ./build/*


tui :
	javac src/r0p3/*.java -d ./bin

trun :
	java -classpath ./bin/ r0p3.Main

clean :
	rm -rf ./bin/*
