import itertools

def check_capt(pw1,pw2):
    #pw1,pw2 (string,string): a pair of input password
    #output (boolean): if pw1 and pw2 can be transformed by this category of rule
    #e.g. pw1 = abcdE, pw2= abCde, output =True
    
    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************

    if pw1.lower() != pw2.lower():
        return False
    else:
        return True
        

def check_capt_transformation(pw1, pw2):
    #pw1,pw2 (string,string): a pair of input password
    #output (string): transformation between pw1 and pw2
    #consider if head char is capt transformed, if tail char is capt transformed, and # of chars that has been capt transformed in total
    #example pw1 = abcde, pw2 = AbcDe, transformation = head\t2 (head char is capt transformed, in total 2 chars are capt transformed)
    #example pw1 = abcdE, pw2 = AbcDe, transformation = head\ttail\t3 (head char and tail chars are capt transformed, in total 3 chars are capt transformed)
    #example pw1 = abcde, pw2 = abcDe, transformation = 1 (in total 1 chars are capt transformed)

    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************

    output = ''
    head = 'head\\t'
    tail = 'tail\\t'
    ct = 0
    if pw1[0] != pw2[0] and pw1[0].lower() == pw2[0].lower():
        output += head
        ct += 1
    if pw1[-1] != pw2[-1] and pw1[-1].lower() == pw2[-1].lower():
        output += tail
        ct += 1
    for i in range(len(pw1)):
        if pw1[i] != pw2[i] and pw1[i].lower() == pw2[i].lower():
            ct += 1
    output += str(ct)
    return output

def apply_capt_transformation(ori_pw, transformation):
    #ori_pw (string): input password that needs to be transformed
    #transformation (string): transformation in string
    #output (list of string): list of passwords that after transformation (all possiblities)
    #ori_pw = "abcde", transformation = "head\t2", output = [ABcde, AbCde, AbcDe]
    
    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************

    trans = transformation[0].split('\t')
    # print(f'trans: {trans}') # apply_capt: ['head\t1', 1176]
    output = []
    
    # if len(trans) == 2:
        # if trans[0] == 'head':
            # str = ''
            # if ord(ori_pw[0]) >= 97 and ord(ori_pw[0]) <= 122:
                # str += ori_pw[0].upper()
            # elif ord(ori_pw[0]) >= 65 and ord(ori_pw[0]) <= 90:
                # str += ori_pw[0].upper()
            # else:
                # str += ori_pw[0]
            # for c in range(1, len(ori_pw)):
                # str += ori_pw[c]
            # output.append(str)
        # elif trans[0] == 'tail':
            # str = ''
            # for c in range(0, len(ori_pw)-1):
                # str += ori_pw[c]
            # if ord(ori_pw[-1]) >= 97 and ord(ori_pw[-1]) <= 122:
                # str += ori_pw[-1].upper()
            # elif ord(ori_pw[-1]) >= 65 and ord(ori_pw[-1]) <= 90:
                # str += ori_pw[-1].upper()
            # output.append(str)
            
    return output
