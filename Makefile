JCC = javac
RM = rm

JFLAGS = -Xlint

all: weather

SRCS = $(wildcard *.java)

.PHONY: clean

weather: $(SRCS)
	$(JCC) $(JFLAGS) $^

runclient:
	java Client

runserver:
	java Server

clean:
	rm *.class