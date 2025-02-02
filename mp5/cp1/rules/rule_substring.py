def check_substring(pw1,pw2):
    #pw1,pw2 (string,string): a pair of input password
    #output (boolean): if pw1 and pw2 can be considered as substring of the other 
    # eg. pw1 = abc, pw2 = abcd, output true
    # eg. pw1 = abcde, pw2 = abcd, output true
    
    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************
    
    # len1 = len(pw1)
    # len2 = len(pw2)
    # common_length = min(len1, len2)
    # check_true = True
    # for i in range(common_length):
        # if pw1[i] == pw2[i]:
            # continue
        # else:
            # check_true = False
            # break
    # return check_true
    return ((pw1.find(pw2) != -1) or (pw2.find(pw1) != -1))

def check_substring_transformation(pw1, pw2):
    #pw1,pw2 (string,string): a pair of input password
    #output (string): transformation between pw1 and pw2
    #example: pw1=123hello!!, pw2=hello, output=head\t123\ttail\t!!
    #example: pw1=hello!!, pw2=hello, output=head\t\ttail\t!!
    
    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************
    
    if pw2 not in pw1:
        return ''
    len1 = len(pw1)
    len2 = len(pw2)
    head = "head\t"
    tail = "\ttail\t"
    return head + pw1[:pw1.find(pw2)] + tail + pw1[pw1.find(pw2)+len2:]

def guess_target_as_substring(ori_pw):
    #the first transformation applied in rule_substring
    #guess the possible passwords as a substring
    #decide to only consider the substring from head or from tail
    #e.g. pw1=abc123, output = [a,ab,abc,abc1,abc12,3,23,123,c123,bc123]
    #in transformation dictionary, the transformation = 'special_trans_as_substring'

    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************
    
    output = []
    length = len(ori_pw)
    for i in range(length):
        output.append(ori_pw[0:i+1])
        output.append(ori_pw[length-i-1:length])
    return output

def apply_substring_transformation(ori_pw, transformation):
    #ori_pw (string): input password that needs to be transformed
    #transformation (string): transformation in string
    #output (list of string): list of passwords that after transformation
    #add head string to head, add tail string to tail
    
    # ***********************************************************************
    # ****************************** TODO ***********************************
    # ***********************************************************************
    
    if transformation == "special_trans_as_substring":
        return guess_target_as_substring(ori_pw)
    else:
        trans = transformation[0].split('\t')
        head = ''
        tail = ''
        if len(trans) >= 2:
            head = trans[1]
        if len(trans) == 4:
            tail = trans[3]
        transformed_pw = head + ori_pw + tail
        return [transformed_pw]
