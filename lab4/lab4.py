from collections import defaultdict

# context free grammar class
class CFG:
    def __init__(self, terminal=None, nonterminals=None, start=None):
        self.terms = set(terminal)
        self.terms.add('$')
        self.noTerm = set(nonterminals)
        self.prods = defaultdict(set)
        self.start = start

    def addProds(self, symbol, string):
        self.prods[symbol].update(string)

    def __findFreeNonTerms__(self, letter):
        # new non-terminal symbols, in case is accessible uppercase letter, it is used,
        # else any other uppercase letter or indexed letter

        if letter.upper() not in self.noTerm:
            self.noTerm.add(letter.upper())
            return letter.upper()
        for i in range(0, 26):
            L = chr(ord('A') + i)
            if L not in self.noTerm:
                self.noTerm.add(L)
                return L

        for i in range(0, 26):
            letter = chr(ord('A') + i)
            for j in range(1, 10):
                L = letter + '_' + str(j)
                if L not in self.noTerm:
                    self.noTerm.add(L)
                    return L

    @classmethod
    def __splitIntoSymbols__(cls, string):
        # split the string into non-terminals and terminals
        result = []
        for c in string:
            if c == '_' or c.isnumeric():
                result[-1] = result[-1] + c
            else:
                result.append(c)
        return result

    def removeDeletedSymbols(self):
        # remove the nonexistent non-terminals production (deleted symbols)
        good = False
        while not good:
            good = True
            newProds = defaultdict(set)  # in newProductions are introduced valid symbols and productions
            for k, v in self.prods.items():
                if k not in self.noTerm:
                    # if the current symbol doesn't exist, it is deleted together with its productions
                    continue
                for str in v:  # delete removed terminals in each production
                    str = CFG.__splitIntoSymbols__(str)  # split the string in non-terminals and terminals
                    newStr = [x for x in str if x in self.noTerm or x in self.terms]
                    # get terminals and non-terminals that exist
                    if str != newStr:  # if string != newString, continue
                        good = False
                    if len(newStr):  # if there is still something left in string after eliminations
                        newProds[k].add(''.join(newStr))
            self.prods = newProds
            self.noTerm = set(newProds.keys())  # update non-terminals

    def removeRedundantSymbols(self):
        # delete unproductive symbols
        productive = set()
        good = False
        while not good:
            good = True
            for k, v in self.prods.items():
                if k in productive:
                    continue
                for str in v:  # check all the productions for the symbol
                    symbs = CFG.__splitIntoSymbols__(str)
                    isProductive = True  # suppose that the left symbol is productive
                    for s in symbs:  # check if all the symbols in string are productive
                        if s not in productive and s not in self.terms:  # if it is unproductive, check other production
                            isProductive = False

                    if isProductive:  # if all symbols in production are productive, then it is productive too
                        productive.add(k)
                        good = False
        self.noTerm = productive
        # if start symbol is unproductive, it is not ok
        if self.start not in productive:
            raise ValueError('Start symbol is unproductive!')
        # remove unproductive symbols
        self.removeDeletedSymbols()

        # delete unreachable symbols
        reachable = set(self.start)
        stack = [self.start]
        while len(stack) > 0:  # find the reachable symbols from the start symbol
            top = stack[-1]
            stack.pop()
            for str in self.prods[top]:
                symbs = [x for x in CFG.__splitIntoSymbols__(str) if x in self.noTerm]
                for s in symbs:
                    if s not in reachable:
                        reachable.add(s)
                        stack.append(s)
        self.noTerm = reachable
        # remove unreachable symbols
        self.removeDeletedSymbols()

    def __replaceSymbol__(self, old, new):
        newProductions = defaultdict(set)
        for k, v in self.prods.items():
            for string in v:
                string = string.replace(old, new)
                newProductions[k].add(string)
        self.noTerm.remove(old)
        self.prods = newProductions
        self.removeDeletedSymbols()

    def correctTheTerms(self):
        terminal_dict = defaultdict(str)
        for terminal in self.terms:
            for k, v in self.prods.items():  # check if exists a production that contains only that terminal
                if len(v) == 1 and terminal in v:
                    terminal_dict[terminal] = k
                    break
            else:
                terminal_dict[terminal] = self.__findFreeNonTerms__(terminal)
                # substitute in all productions terminals
        new_productions = defaultdict(set)
        for key, val in self.prods.items():
            for string in val:
                if len(string) == 1 and string in self.terms:  # if A -> a - pass
                    new_productions[key].add(string)
                else:
                    for k, v in terminal_dict.items():  # else substitute terminal by new non-terminal
                        if k in string:
                            string = string.replace(k, v)
                            new_productions[v].add(k)  # add the production non-terminal -> terminal

                    new_productions[key].add(string)

        self.prods = new_productions  # update the product

    def renameLongerThan2(self):
        newNonterminals = dict()
        ok = False
        while not ok:
            ok = True
            newProductions = defaultdict(set)
            for k, v in self.prods.items():

                for string in v:
                    nonterminals = CFG.__splitIntoSymbols__(string)  # split string into non-terminals
                    if len(nonterminals) <= 2:  # if less than 2 non-terminals - pass
                        newProductions[k].add(string)
                        continue
                    ok = False

                    newNonterminal = None

                    for nonterminal in newNonterminals:  # check non-terminals dont duplicate
                        if ''.join(nonterminals[1:]) == newNonterminals.get(nonterminal):
                            newNonterminal = nonterminal

                    if newNonterminal is None:  # if no simple non-terminals, create
                        newNonterminal = self.__findFreeNonTerms__(nonterminals[1][0])
                        newNonterminals[newNonterminal] = ''.join(nonterminals[1:])
                        self.noTerm.add(newNonterminal)
                        newProductions[newNonterminal].add(
                            ''.join(nonterminals[1:]))

                    newProductions[k].add(nonterminals[0] + newNonterminal)  # production form
            self.prods = newProductions

    def removeEmptyProds(self):
        emptys = set()
        good = False

        while not good:
            good = True
            for k, v in self.prods.items():
                if k in emptys:
                    continue
                for string in v:
                    if string == '$' or all(
                            [nonTerms in emptys for nonTerms in CFG.__splitIntoSymbols__(string)]):
                        emptys.add(k)
                        good = False

        newProds = defaultdict(set)
        for k, v in self.prods.items():
            newProds[k] = set(v)  # add new productions that will delete one non-terminal
            for string in v:
                nonTerms = CFG.__splitIntoSymbols__(string)  # split string into symbols
                if len(nonTerms) == 1 and nonTerms[0] in emptys:  # if olny 1 epsilon, add $ to production
                    newProds[k].add('$')
                elif len(nonTerms) == 2:  # if there are 2 symbols (A -> BC)
                    if nonTerms[0] in emptys:  # if B e nullable
                        newProds[k].add(nonTerms[1])  # then add A -> C
                    if nonTerms[1] in emptys:  # if C e nullable
                        newProds[k].add(nonTerms[0])  # add A -> B
                    if nonTerms[0] in emptys and nonTerms[1] in emptys:
                        newProds[k].add('$')  # if both are nullable, add A -> $
        # remove the productions of form A -> $
        newProds = {k: v.difference(['$']) for k, v in newProds.items() if len(v.difference(['$'])) > 0}
        # update the productions and non-terminals
        self.noTerm = set(newProds.keys())
        self.prods = newProds
        # if start symbol was lambda, then add S -> $
        if self.start in emptys:
            self.prods[self.start].add('$')
        # delete removed non-terminals (A -> $)
        self.removeDeletedSymbols()

    def removeUnitProds(self):
        good = False
        while not good:
            good = True
            newProds = defaultdict(set)
            for k, v in self.prods.items():
                for string in v:
                    nonTerms = CFG.__splitIntoSymbols__(string)  # split into non-terminals (or 1 terminal)
                    if len(nonTerms) == 1 and nonTerms[0] in self.noTerm:  # if only 1 non-terminal - eliminate
                        good = False
                        if nonTerms[0] == k:  # if A -> A, ignore
                            continue
                        newProds[k].update(self.prods[nonTerms[0]])
                        # else add productons of the current symbol to that symbol
                    else:
                        newProds[k].add(string)  # if NOT (it's 2) leave it like that
            self.prods = newProds
            self.noTerm = set(newProds.keys())  # update prods, delete the removed non-terminals (A->A, remove A)
            self.removeDeletedSymbols()

    def __convertToCNF__(self):
        # remove unreachable and unproductive elements
        self.removeRedundantSymbols()
        # substitute the terminals in all productions, unless A -> a
        # for each terminal create an accessible non-terminal symbol
        self.correctTheTerms()
        # correct productions with more than 2 non-terminals by 2 non-terminals
        self.renameLongerThan2()
        # delete lambda-productions
        self.removeEmptyProds()
        # remove unit-productions
        self.removeUnitProds()

    def isCNF(self):
        # check if CNF
        for k, v in self.prods.items():
            for string in v:
                string = CFG.__splitIntoSymbols__(string)
                if len(string) != 2 and len(string) != 1:  # if longer than 2 then not in CNF
                    return False
                else:
                    if len(string) == 1 and string[0] not in self.terms:  # if not 1 el. prod. non-term. then not in CNF
                        return False
                    elif len(string) == 2 and (string[0] not in self.noTerm or string[1] not in self.noTerm):
                        # if there are 2 symbs with terminals, then not in CNF
                        return False
        return True

    def convertToCNF(self):
        if not self.isCNF():
            try:
                while not self.isCNF():
                    self.__convertToCNF__()
            except ValueError:
                self.start = None
                self.prods.clear()
                self.noTerm.clear()
                self.terms.clear()
                print('Start symbol is not productive!')
                return False

    def print(self, file=None):
        if file is None:
            print(f'Start symbol: {self.start}')
            if len(self.prods) == 0:
                return
            print(self.start, '->', ' | '.join(self.prods[self.start]))
            for key, val in sorted(self.prods.items(),
                                   key=lambda x: (-len(x[1]), list(x[1])[0] in self.terms)):
                if key != self.start:
                    print(key, '->', ' | '.join(val))
        else:
            with open(file, 'w') as f:
                f.write(f'Start symbol: {self.start}\n')
                if len(self.prods) == 0:
                    return
                f.write(self.start + ' -> ' + ' | '.join(self.prods[self.start]) + '\n')
                for key, val in sorted(self.prods.items(),
                                       key=lambda x: (-len(x[1]), list(x[1])[0] in self.terms)):
                    if key != self.start:
                        f.write(str(key) + ' -> ' + ' | '.join(val) + '\n')


if __name__ == '__main__':
    with open('input.txt', 'r') as f:
        lines = [x.strip() for x in f.readlines()]
        terms = [x.strip() for x in lines[0].split()]
        nonTerms = [x.strip() for x in lines[1].split()]
        start = lines[2].strip()
        g = CFG(terms, nonTerms, start)
        for line in lines[3:]:
            line = [x.strip() for x in line.split('->')]
            symb = line[0]
            strings = [x.strip() for x in line[1].split('|')]
            g.addProds(symb, strings)
    print('Input:')
    g.print()
    print('\nChomsky normal form: ')
    g.convertToCNF()
    g.print()
    g.print('output.txt')