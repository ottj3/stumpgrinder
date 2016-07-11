#!/usr/bin/python
import sys,random
from collections import deque

def readgraph(fname):
    fin = open(fname, "r")
    lab = {}
    n = int(fin.readline())
    for i in range(0,n):
        lab[i] = fin.readline().rstrip()
    edge = {}
    for i in range(0,n):
        e = fin.readline().split()
        for j in range(0,n):
            edge[lab[i],lab[j]] = float(e[j])
    return n,lab,edge

def findshortestpaths(n,lab,edge):
    path = {}
    for i in range(0,n):
        for j in range(0,n):
            path[lab[i],lab[j]] = -1
        queue = deque([[lab[i],0]])

        while queue:
            current,currpath = queue.popleft()
            if path[lab[i],current] == -1 or path[lab[i],current] > currpath:
                path[lab[i],current] = currpath
                for j in range(0,n):
                    if edge[current,lab[j]] == -1:
                        minedge = edge[lab[j],current]
                    elif edge[lab[j],current] == -1:
                        minedge = edge[current,lab[j]]
                    else:
                        minedge = min(edge[current,lab[j]],edge[lab[j],current])
                    if minedge != -1:
                        queue.extend([[lab[j],currpath+minedge]])
    return path

def randomgraph(n,lab):
    edge = {}
    for i in range(0,n):
        edge[lab[i],lab[i]] = 0
        for j in range(0,i):
            edge[lab[i],lab[j]] = edge[lab[j],lab[i]] = random.choice([-1,1])
    return edge

def printdot(n,lab,edge,depth):
    print "digraph G {\nnode [shape=plaintext]"
    for i in range(0,n):
        print '%s [label="%s [%d]"]' % (lab[i], lab[i], depth[lab[i]])
        for j in range(0,n):
            if i != j and edge[lab[i],lab[j]] != -1:
                if edge[lab[j],lab[i]] != -1 and i < j:
                    print "%s -> %s [dir = none]" % (lab[i],lab[j])
                elif edge[lab[j],lab[i]] == -1:
                    print "%s -> %s" % (lab[i],lab[j])
    print "}"

def sign(x, y):
    if x == -1 and y == -1: return 0
    elif x == -1: return -1
    elif y == -1: return 1
    elif x < y: return -1
    elif x > y: return 1
    else: return 0

def findroots(n,lab,edge):
    roots = []
    for i in range(0,n):
        roots.append(lab[i])
        for j in range(0,n):
            if i != j and edge[lab[j],lab[i]] != -1:
                roots.pop()
                break
    return roots

def finddepth(n,lab,edge,root):
    depth = {}
    for i in range(0,n):
        depth[lab[i]] = -1
    queue = deque([[x,0] for x in root])
    while queue:
        current,currdepth = queue.popleft()
        if depth[current] == -1 or depth[current] > currdepth:
            depth[current] = currdepth
            for j in range(0,n):
                minedge = edge[current,lab[j]]
                if minedge != -1:
                    queue.extend([[lab[j],currdepth+minedge]])
    return depth
    
def findScore(f1, f2, verbose):
    ntrue,labtrue,edgetrue = readgraph(f1)
    nprop,labprop,edgeprop = readgraph(f2)
    #nprop = ntrue
    #labprop = labtrue
    #random.seed
    #edgeprop = randomgraph(nprop,labprop)

    pathtrue = findshortestpaths(ntrue,labtrue,edgetrue)
    pathprop = findshortestpaths(nprop,labprop,edgeprop)
    roottrue = findroots(ntrue,labtrue,edgetrue)
    rootprop = findroots(nprop,labprop,edgeprop)
    depthtrue = finddepth(ntrue,labtrue,edgetrue,roottrue)
    depthprop = finddepth(nprop,labprop,edgeprop,rootprop)

    score1 = 0.0
    total1 = 0.0
    for i in range(0,ntrue):
        if labtrue[i][0].isalpha():
            for j in range(0,ntrue):
                if labtrue[j][0].isalpha():
                    for k in range(0,ntrue):
                        if labtrue[k][0].isalpha() and i != j and \
                                i != k and j != k:
                            signtrue = sign(pathtrue[labtrue[i],labtrue[j]], \
                                                pathtrue[labtrue[i],labtrue[k]])
                            signprop = sign(pathprop[labtrue[i],labtrue[j]], \
                                                pathprop[labtrue[i],labtrue[k]])
                            if verbose:
                                print "true %s-%s dist %d, %s-%s dist %d <%d>, prop %s-%s dist %d, %s-%s dist %d <%d> %.1f" % \
                                    (labtrue[i],labtrue[j],\
                                         pathtrue[labtrue[i],labtrue[j]],\
                                         labtrue[i], labtrue[k],\
                                         pathtrue[labtrue[i],labtrue[k]],\
                                         signtrue,\
                                         labtrue[i], labtrue[j],\
                                         pathprop[labtrue[i],labtrue[j]],\
                                         labtrue[i], labtrue[k],\
                                         pathprop[labtrue[i],labtrue[k]],\
                                         signprop, \
                                         (1-abs(signtrue-signprop)/2.0))
                            score1 = score1 + (1-abs(signtrue-signprop)/2.0)
                            total1 = total1 + 1.0

    score2 = 0.0
    total2 = 0.0
    for i in range(0,ntrue):
        if labtrue[i][0].isalpha():
            for j in range(0,ntrue):
                if labtrue[j][0].isalpha() and i != j:
                    signtrue = sign(depthtrue[labtrue[i]], depthtrue[labtrue[j]])
                    signprop = sign(depthprop[labtrue[i]], depthprop[labtrue[j]])
                    if verbose:
                        print "true %s depth %d, %s depth %d <%d>, prop %s depth %d, %s depth %d <%d> %.1f" % \
                            (labtrue[i], depthtrue[labtrue[i]], \
                                 labtrue[j], depthtrue[labtrue[j]], \
                                 signtrue,\
                                 labtrue[i], depthprop[labtrue[i]], \
                                 labtrue[j], depthprop[labtrue[j]], \
                                 signprop, \
                                 (1-abs(signtrue-signprop)/2.0))
                    score2 = score2 + (1-abs(signtrue-signprop)/2.0)
                    total2 = total2 + 1.0

    # printdot(ntrue,labtrue,edgetrue,depthtrue)
    # printdot(nprop,labprop,edgeprop,depthprop)

    print "average sign distance for skeleton %.3f%%" % (100.0 * score1 / total1)
    print "average sign distance for orientation %.3f%%" % (100.0 * score2 / total2)

# if __name__ == "__main__":
#     findScore(sys.argv[1], sys.argv[2], False)