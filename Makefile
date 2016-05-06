JC = javac
JFLAGS = -g

JAR    = stumpgrinder.jar

PACKAGE = edu/tcnj/stumpgrinder

SRCDIR = src/$(PACKAGE)
SRCS   = $(wildcard $(SRCDIR)/*.java)

OBJDIR = obj
OBJS   = $(patsubst $(SRCDIR)/%.java, $(OBJDIR)/$(PACKAGE)/%.class, $(SRCS))

.PHONY : build clean run

build: $(JAR)

clean :
	rm -rf $(OBJDIR)
	rm -f $(JAR)

rebuild: clean build

run:
	java -jar $(JAR)

$(JAR) : $(OBJS)
	cd $(OBJDIR); \
	jar cfe $(JAR) edu.tcnj.stumpgrinder.Dirty *; \
	cd -; \
	mv $(OBJDIR)/$(JAR) .;

$(OBJS) : $(SRCS) $(OBJDIR)
	$(JC) $(JFLAGS) -d obj $(SRCS)

$(OBJDIR) : 
	mkdir -p $(OBJDIR)
