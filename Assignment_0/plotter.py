import matplotlib.pyplot as plt
import numpy as np


plt.style.use('_mpl-gallery')


def calcMean(path):
    fileRead = open(path)
    fileRead.readline()
    width = int(fileRead.readline().rstrip('\n'))
    prob = float(fileRead.readline().rstrip('\n'))
    timeArr = fileRead.readlines()
    netSum = 0
    for i in range(len(timeArr)):
        netSum = netSum + int(timeArr[i].rstrip('\n'))
    return float(netSum)/float(len(timeArr)-3)
    fileRead.close()


def plotViolin(path):
    fileRead = open(path)
    fileRead.readline()
    width = int(fileRead.readline().rstrip('\n'))
    prob = float(fileRead.readline().rstrip('\n'))

    timeArr = fileRead.readlines()
    for i in range(len(timeArr)):
        timeArr[i] = int(timeArr[i].rstrip('\n'))
    arr = np.arange(len(timeArr))

    plt.xlabel("Variance")
    plt.ylabel("Time Taken")
    plt.title("Widht:"+str(width)+" & Probability:"+str(prob))
    plt.violinplot(timeArr, showmeans=True, showmedians=True)
    plt.savefig("images/"+path+"Violin.png", bbox_inches="tight",dpi=800)
    plt.clf()
    fileRead.close()


def plotHist(pathArr):
    widhts = []
    probs = []
    avg = []
    for i in pathArr:
        avg.append(calcMean(i))
        readFile = open(i)
        readFile.readline()
        widhts.append(int(readFile.readline().rstrip('\n')))
        probs.append(float(readFile.readline().rstrip('\n')))
    plt.hist(avg, linewidth=0.5, edgecolor="white")
    plt.xlabel("Average Time")
    plt.ylabel("Counts")
    plt.savefig("images/Hist.png", bbox_inches="tight", dpi=800)
    plt.clf()


def plotBar(pathArr):
    widhts = []
    probs = []
    avg = []
    for i in pathArr:
        avg.append(calcMean(i))
        readFile = open(i)
        readFile.readline()
        widhts.append(int(readFile.readline().rstrip('\n')))
        probs.append(float(readFile.readline().rstrip('\n')))
    plt.bar(x=probs, height=avg,width=0.015, edgecolor="white")
    plt.ylabel("Average Time")
    plt.xlabel("Probablity = 1/Width")
    plt.savefig("images/Bar2.png", bbox_inches="tight",dpi=800)
    plt.clf()


arrPath = ["1000_10_0.1.txt",
           "1000_10_0.2.txt",
           "1000_10_0.3.txt",
           "1000_10_0.4.txt",
           "1000_10_0.5.txt",
           "1000_10_0.6.txt",
           "1000_10_0.7.txt",
           "1000_10_0.8.txt",
           "1000_10_0.9.txt",
           "1000_4_0.25.txt",
           "1000_5_0.1.txt",
           "1000_5_0.2.txt",
           "1000_5_0.5.txt",
           "1000_6_0.166666.txt",
           "1000_7_0.142857.txt",
           "1000_8_0.125.txt",
           "1000_9_0.11111111.txt"]

arrPath2 = ["1000_10_0.1.txt",
            "1000_10_0.2.txt",
            "1000_10_0.3.txt",
            "1000_10_0.4.txt",
            "1000_10_0.5.txt",
            "1000_10_0.6.txt",
            "1000_10_0.7.txt",
            "1000_10_0.8.txt",
            "1000_10_0.9.txt"]

arrPath3 = [
    "1000_4_0.25.txt",
    "1000_5_0.2.txt",
    "1000_6_0.166666.txt",
    "1000_7_0.142857.txt",
    "1000_8_0.125.txt",
    "1000_9_0.11111111.txt"
]

# for i in arrPath:
#     plotViolin(path=i)
# plotHist(arrPath)
# plotBar(arrPath2)


# plotBar(arrPath3)