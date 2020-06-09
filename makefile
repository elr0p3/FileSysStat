.PHONI := all gui grun gclean tui run clean

all : gui


gui :
	javac -classpath ./src/  src/**/*.java -d ./build

grun :
	java -classpath ./build/ r0p3GUI.Main

gclean :
	rm -rf ./build/


tui :
	javac src/r0p3/*.java -d ./bin

run :
	java -classpath ./bin/ r0p3.Main

clean :
	rm -rf ./bin/
