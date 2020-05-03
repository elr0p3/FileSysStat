.PHONI := get_class run clear

all : get_class

get_class :
	javac src/*.java

run :
	@java src/Main

clean :
	rm src/*.class
