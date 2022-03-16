import pandas as pd

#Varianta 10
#grammar
#Q={q0, q1, q2, q3}
#S={a, b, c}
#F={q3}
#q0 -> a q1
#q1 -> b q2
#q2 -> c q3
#q3 -> a q1
#q1 -> b q1
#q0 -> b q2


# initially creating a list of strings that are grammar production rules

grammar = [
    '0 a 1',
    '1 b 2',
    '2 c 3',
    '3 a 1',
    '1 b 1',
    '0 b 2 '
]


#using this function we are representing our NFA as a dictionary of dictionaries.
#for each state we create a dictionary which is keyed by the letters of our alphabet {a, b, c}
#and the global dictionary is keyed by the states of our NFA

def parseGrammar(grammar):

    nfa = {}
    for grammarRules in grammar:
        rulePart = grammarRules.split(' ') # eliminate spaces

        if not rulePart[0] in nfa:
            nfa[rulePart[0]] = {}

        if not rulePart[1] in nfa[rulePart[0]]:
            nfa[rulePart[0]][rulePart[1]] = ''

        nfa[rulePart[0]][rulePart[1]] += rulePart[2]

    return nfa


#the algorithm for converting NFA to DFA
#we determine the set of states and values in nfa and add the new states that's length is bigger than 1

def NFAtoDFA(nfa):

    states = []
    values = []

    for state in nfa:
        states.append(state)


    for state in nfa:
        for value in nfa[state]:
            if len(nfa[state][value]) > 1:
                if not nfa[state][value] in states:
                    states.append(nfa[state][value])
            else:
                if not nfa[state][value][0] in states:
                    states.append(nfa[state][value][0])

    for state in nfa:
        for value in nfa[state]:
            if not value in values:
                values.append(value)

    for state in states:
        if not state in nfa:
            newState = list(state)
            for value in values:
                val = []

#for all the states that derive from the new state formated
#we add the transitions to val list
                for st in newState:
                    if value in nfa[st]:
                        val.append(nfa[st][value])
#we add the new state in the global dict keyed by states
                if not state in nfa:
                    nfa[state] = {}
#add the elements of the list to the inner dict which is keyed by alphabet elements
#the values in the inner dict are joined as all the transitions from the formed state are considered
                nfa[state][value] = ''.join(set(''.join(val)))
                states.append(''.join(set(''.join(val))))

    return nfa

print("The initial NFA")
nfa = parseGrammar(grammar)
print(nfa)
NFA = pd.DataFrame(nfa)
NFA = NFA.fillna("X")
print(NFA.transpose())

print("The DFA after conversion")
dfa = NFAtoDFA(nfa)
print(dfa)
DFA = pd.DataFrame(dfa)
DFA = DFA.fillna("X")
print(DFA.transpose())