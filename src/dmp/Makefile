JC = javac
JFLAGS = -g

SRCDIR = src
OBJDIR = obj

SRCS=$(wildcard $(SRCDIR)/*.java)
OBJS=$(patsubst $(SRCDIR)/%.java,$(OBJDIR)/%.class,$(SRCS))

EXE = stumpgrinder

build: $(EXE)

clean:
	rm -rf $(OBJDIR) $(EXE)

$(EXE): $(OBJS)

$(OBJS): | $(OBJDIR)
$(OBJDIR):
	mkdir -p $@

$(OBJDIR)/%.class: $(SRCDIR)/%.java
	$(JC) $(JFLAGS) -d $(OBJDIR) $<
