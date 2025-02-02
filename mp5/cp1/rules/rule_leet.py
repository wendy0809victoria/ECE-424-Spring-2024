import itertools

#a more complete leet_list
#leet_list = [{'4', 'a', '@', '/\\', '/-\\'}, {'b', '|3', '8', '|o'}, {'<', 'K', 'g', 'S', '9', '6', 'c', '('}, {'0', '()', '{}', 'o', '[]'}, {'!', '|', '][', '#', ')-(', '1', 'i', 'l', '}-{', '|-|', '+', 't', ']-[', 'h', '(-)', '7'}, {'5', 's', '$'}, {'+', 't'}, {'/\\/\\', 'm', '/v\\', '/|\\', '/\\\\', '|\\/|', '(\\/)', "|'|'|"}, {'\\|/', '\\|\\|', '\\^/', '//', 'w', '|/|/', '\\/\\/'}, {'|\\|', '|\\\\|', 'n', '/|/', '/\\/'}, {'u', '|_|'}, {'2', '(\\)', 'z'}, {'(,)', 'q', 'kw'}, {'v', '|/', '\\|', '\\/', '/'}, {'k', '/<', '|{', '\\<', '|<'}, {'<|', 'o|', '|)', '|>', 'd'}, {'f', 'ph', '|=', 'p#'}, {'l', '|_'}, {'j', 'y', '_|'}, {'}{', 'x', '><'}, {"'/", 'y', '`/'}, {'p', '|D', 'r', '|2'}, {'r', '|Z', '|?'}, {'e', '3'}]
leet_list = [{"@","a"},{"3","e"},{"1","i"},{"0","o"},{"$","s"},{"+","t"},{"4","a"},{"5","s"},{"|","i"},{"!","i"}]
def check_leet(pw1,pw2):
    #pw1,pw2 (string,string): a pair of input password
    #output (boolean): if pw1 and pw2 can be transformed by this category of rule
    #e.g. pw1 = abcde, pw2 = @bcd3 , output = True
    
    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************

    if len(pw1) != len(pw2):
        return False
    check = True
    for i in range(len(pw1)):
        if pw1[i] == pw2[i]:
            continue
        if {pw1[i], pw2[i]} in leet_list:
            continue
        else:
            check = False
            break
    return check

def check_leet_transformation(pw1, pw2):
    #pw1,pw2 (string,string): a pair of input password
    #output (string): transformation between pw1 and pw2
    #example: pw1=abcd3 pw2 = @bcde, transformation = 3e\ta@ because pw1->pw2:3->e and a->@ and '3e'<'a@' for the order
    #for simplicity, duplicate item is allowed. example: pw1=abcda pw2 = @bcd@, transformation = a@\ta@ 
    
    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************

    if len(pw1) != len(pw2):
        return ''
    output = ''
    split = '\t'
    trans = []
    if check_leet(pw1,pw2):
        for i in range(len(pw1)):
            if pw1[i] == pw2[i]:
                continue
            if {pw1[i], pw2[i]} in leet_list:
                trans.append(pw1[i]+pw2[i])
    trans_sort = sorted(trans, key=lambda x: x[0])
    for t in trans_sort:
        output += t
        if t != trans_sort[-1]:
            output += split
    return output

def apply_leet_transformation(ori_pw, transformation):
    #ori_pw (string): input password that needs to be transformed
    #transformation (string): transformation in string
    #output (list of string): list of passwords that after transformation
    #only need to consider forward transformation and backward transformation combinations.
    #forward transformation: each term in transformation, can be and only be applied once on the ori_pw in forward direction (3->e,a->@)
    #backward: (e->3,@->a)
    
    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************
    
    trans = transformation[0].split('\t')
    leet_dict_1 = {}
    leet_dict_2 = {}
    for leet in leet_list:
        elements = []
        for element in leet:
            elements.append(element)
        leet_dict_1[elements[0]] = elements[1]
        leet_dict_2[elements[1]] = elements[0]
    
    output = []
    forward = []
    backward = []
    
    string = ''
    for t in [trans[0]]:
        for i in range(len(ori_pw)):
            if len(t) >= 1:
                if ori_pw[i] == t[0]:
                    if len(t) >= 2:
                        string += t[1]
                    else:
                        string += ori_pw[i]
                else:
                    string += ori_pw[i]
            else:
                string += ori_pw[i]
    output.append(string)
    
    string = ''
    for t in [trans[0]]:
        for i in range(len(ori_pw)):
            if len(t) >= 2:
                if ori_pw[i] == t[1]:
                    if len(t) >= 2:
                        string += t[0]
                    else:
                        string += ori_pw[i]
                else:
                    string += ori_pw[i]
            else:
                string += ori_pw[i]
    output.append(string)

    return set(output)
