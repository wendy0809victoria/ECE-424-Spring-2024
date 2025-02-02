import csv
from logging import warn, error, debug
from user import User

## parse homes.txt
#  input:
#    f: filename
#  output:
#    a dict of all users from homes.txt with key=user_id, value=User object
def cp1_1_parse_homes(f):
    dictUsers_out = dict()
    with open(f) as csv_f:
        for i in csv.reader(csv_f):
            # TODO
            if len(i) == 1:
                dictUsers_out[int(i[0].strip())] = User(int(i[0].strip()))
            if len(i) == 4:
                dictUsers_out[int(i[0].strip())] = User(int(i[0].strip()), float(i[1].strip()), float(i[2].strip()), bool(int(i[3].strip())))
            if len(i) != 1 and len(i) != 4:
                continue
    return dictUsers_out


## parse friends.txt
#  input:
#    f: filename
#    dictUsers: dictionary of users, output of cp1_1_parse_homes()
#  no output, modify dictUsers directly
def cp1_2_parse_friends(f, dictUsers):
    with open(f) as csv_f:
        for i in csv.reader(csv_f):
            # TODO 
            if len(i) != 2:
                continue
            dictUsers[int(i[0].strip())].friends.add(int(i[1].strip()))


# return all answers to Checkpoint 1.3 of MP Handout in variables
# order is given in the template
def cp1_3_answers(dictUsers):
    # TODO: return your answers as variables in the given order
    u_cnt = len(list(dictUsers.keys()))
    u_ids = list(dictUsers.keys())
    u_noloc_cnt = 0
    u_noloc_nofnds_cnt = 0
    p_b = 0
    p_u1 = 0
    p_u2 = 0
    
    for i in range(0, u_cnt):
        if dictUsers[u_ids[i]].home_shared == False:
            u_noloc_cnt += 1
        if dictUsers[u_ids[i]].home_shared == False and len(dictUsers[u_ids[i]].friends) == 0:
            u_noloc_nofnds_cnt += 1
    p_b = (u_cnt - u_noloc_cnt) / u_cnt
    p_u1 = (u_cnt - u_noloc_nofnds_cnt) / u_cnt
    
    no_shared_friends = 0
    for i in range(0, u_cnt):
        if dictUsers[u_ids[i]].home_shared == False and len(dictUsers[u_ids[i]].friends) == 0:
            no_shared_friends += 1
        if dictUsers[u_ids[i]].home_shared == False and len(dictUsers[u_ids[i]].friends) != 0:
            ct = 0
            for j in range(0, len(dictUsers[u_ids[i]].friends)):
                if dictUsers[list(dictUsers[u_ids[i]].friends)[j]].home_shared == True:
                    ct += 1
            if ct == 0:
                no_shared_friends += 1
    
    p_u2 = (u_cnt - no_shared_friends) / u_cnt
    
    return u_cnt, u_noloc_cnt, u_noloc_nofnds_cnt, p_b, p_u1, p_u2
