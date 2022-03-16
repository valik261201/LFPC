# Punctuation tokens
punctuationTokens = {
    ".": "DOT", ",": "COMMA", ";": "SEMICOLON", ":": "COLON", "{": "LBRACE", "}": "RBRACE", "(": "LPAREN",
    ")": "RPAREN",
    "[": "LBRACKET", "]": "RBRACKET", "+": "PLUS", "-": "MINUS", "/": "SLASH", "%": "MOD", "*": "MULT",
    "\"": "DOUBLE_QUOTE"
}

# Operation tokens
operationTokens = {
    "<": "SMALLER", ">": "GREATER", "=": "ASSIGN", "!": "NEGATION", "!=": "NOT_EQUAL",
    "==": 'EQUAL', ">=": "GREATER_AND_EQUAL", "<=": "SMALLER_AND_EQUAL",
}

# Keyword tokens
keywordTokens = {
    "int": "INT", "double": "DOUBLE", "bool": "BOOLEAN", "array": "ARRAY", "char": 'CHAR', "string": "STRING",
    "for": "FOR", "while": "WHILE", "do": "DO", "if": "IF", "else": "ELSE", "elif": "ELIF", "function": "FUNCTION",
    "main": "MAIN", "return": "RETURN", "and": "AND", "or": "OR", "true": "TRUE", "false": "FALSE", "print": "PRINT"
}



# Token class
class Token:
    def __init__(self, type, value):
        self.type = type
        self.value = value


# Lexer class
class Lexer:

    def __init__(self, inp):
        self.text = inp
        self.tokens = []
        self.current = 0
        self.line = -1
        self.length = 0
        self.error = False

    # Reset values for each line
    def initializer(self, input_line):
        self.text = input_line
        self.current = 0
        self.line += 1
        self.length = len(input_line)
        self.error = False

    # Increment position
    def inc_pos(self):
        self.current += 1

    # Main function to tokenize a line input into tokens
    def tokenizer(self, input_line):
        self.initializer(input_line)

        #we have 4 main cases when current char is a: single character, operation, digit or alphabetic
        #we loop until end of line
        while self.current < self.length:
            current_char = self.text[self.current]

            if current_char =='\n':
                break
            if current_char == ' ':
                self.inc_pos()
            elif current_char in punctuationTokens:
                self.setPunctuationTokens(current_char)
            elif current_char in operationTokens:
                self.setOperationTokens()
            elif current_char.isalpha():
                self.setKeywordTokens()
            elif current_char.isdigit():
                self.setDigitTokens()

    #for single characters we simply add them to tokens list
    def setPunctuationTokens(self, punctuation):
        if punctuation == "\"":               # " - double quotes
            self.inc_pos()
            position = self.current
            while position != self.length:
                if self.text[position] != "\"":   # read the string inside double quotes
                    position += 1
                else:
                    position += 1
                    break
            s = self.text[self.current:position-1]  #string from double quotes
            token = Token(s, "STRING_VAL")
            self.tokens.append(token)
            self.current = position
        else:
            token = Token(punctuation, punctuationTokens.get(punctuation))
            self.tokens.append(token)
            self.inc_pos()

    # We have a special function for operations like comparison operators since they can be more than 1 char
    def setOperationTokens(self):
        position = self.current
        operation = ""
        while position != self.length:
            #if operation is longer than 2 chars like ">==" we have an error
            if position - self.current > 2:
                self.error = True
                print("Too many operators error!")
            if self.text[position] in operationTokens:
                position += 1
            else:
                break
        operation = self.text[self.current:position]

        if not self.error:
            if operation in operationTokens:
                token = Token(operation, operationTokens.get(operation))
                self.tokens.append(token)
            else:
                print("Operation Error! Check line here: ", self.line)
        else:
            print("Operation Error! Check line: ", self.line)

        self.current = position

    #we create an alphanumeric word and if it doesn't belong to keywords it'll be an identifier
    def setKeywordTokens(self):
        position = self.current
        while position != self.length:
            if self.text[position].isalnum():
                position += 1
            else:
                break
        s = self.text[self.current:position]   # get the keyword
        if s in keywordTokens:
            token = Token(s, keywordTokens.get(s))
            self.tokens.append(token)
        else:
            token = Token(s, "IDENTIFIER")
            self.tokens.append(token)
        self.current = position

    #we loop until we have either digits or a '.' char and create a number based off that
    def setDigitTokens(self):
        position = self.current
        #keeping track of dots to avoid syntax error like "1.2.3" is not a number
        dot_counter = 0
        while True:
            if position >= self.length:
                break
            if not self.text[position].isdigit() or self.text[position].isdigit() != '.':
                #if current char is a letter then error like "1a.2" or "1a"
                if self.text[position].isalpha(): # isalpha returns true if letter
                    self.error = True
                    break
            if self.text[position] == '.':
                dot_counter += 1
            position += 1

        number = self.text[self.current:position]

        #if we have no dots in the number string then it's an integer
        #if we have 1 dot it'll be a double
        #otherwise it's a syntax error
        if not self.error:
            if dot_counter == 0:
                token = Token(number, "INT_VAL")
                self.tokens.append(token)
            elif dot_counter == 1:
                token = Token(number, "DOUBLE_VAL")
                self.tokens.append(token)
            else:
                print("Syntax Error! Wrong number format! Check line: ", self.line)
        else:
            print("Syntax Error! Wrong number format! Check line: ", self.line)

        self.current = position

    #print tokens array
    def print_tokens(self):
        for token in self.tokens:
            print(token.value, " ", token.type)

lex = Lexer('')
with open("E:\\UTM\\LFPC_LABS\\lab3\\text.txt") as open_text:
    for line in open_text:
        print(line)
        lex.tokenizer(line)

lex.print_tokens()