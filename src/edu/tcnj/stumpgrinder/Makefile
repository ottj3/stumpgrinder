
SRCS = $(wildcard *.java)
OBJS = $(patsubst %.java,%.class,$(SRCS))

$(OBJS) : $(SRCS)
	javac $(SRCS)

.PHONY : clean
clean : 
	-rm *.class
