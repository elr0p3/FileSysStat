.PHONI := all gui grun gclean

all : gui

# tui :
# javac src/r0p3/*.java -d ./bin

# run :
# java -classpath ./bin/ r0p3.Main

# clean :
# rm ./bin/r0p3/*.class


gui :
	javac -classpath ./src/  src/**/*.java -d ./build

grun :
	java -classpath ./build/ r0p3GUI.Main

gclean :
	rm -rf ./bin/
