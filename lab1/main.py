# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.


# Use a breakpoint in the code line below to debug your script.


# Press the green button in the gutter to run the script.

# See PyCharm help at https://www.jetbrains.com/help/pycharm/


import networkx as nx
import numpy as np

# import pylab

# Convert RG to FA
vn_brut = input("Defineste elementele Vn (separandu-le prin spatiu)\n")
vn = vn_brut.split(" ")
vn.append(".")
print("VN = {}".format(vn))

nr_rel = int(input("Care este numarul de relatii?\n"))
matrice_graph = [[0 for x in range(len(vn))] for y in range(len(vn))];

#citirea datelor
for k in range(nr_rel):
    i = 0
    j = 0
    vertex_start, weight, vertex_terminal = input().split(" ")
#cream matricea de relatii
    for n in range(len(vn)):
        if (vn[n] == vertex_start): i = n
        if (vn[n] == vertex_terminal): j = n
        matrice_graph[i][j] = weight

print(matrice_graph)
# Ex. 2
# Check if there could exist such word in FA
word = input("Introduceti cuvantul pentru verificare:\n")

check_var = True
len_word = len(word)
h = 0
valori_finale = [0 for i in range(len(vn))]
j = len(vn) - 1

for i in range(j):
#check if relation exists
    if (matrice_graph[i][j] != 0):
        valori_finale[h] = matrice_graph[i][j]
        h = h + 1

for k in range(h):
    if (word[len_word - 1] != valori_finale[k]):
        check_var = False
    else:
        check_var = True; break;
print(valori_finale)
print(check_var)
i = 0
for k in range(len_word):
    ch = word[k]
    j = 0
    if (check_var == True):
        if (matrice_graph[i][j] == ch):
            i = j
            j = 0

    else:
        if (j != len(vn) - 1):
            j += 1

if (check_var):
    print("Cuvantul corespunde FA")
else:
    print("Cuvantul nu corespunde FA")