from user import User
from utils import distance_km
import numpy as np
import statistics

def cp2_1_simple_inference(dictUsers):
    dictUsersInferred = dict()  # dict to return, store inferred results here
    # you should keep everything in dictUsers as is / read-only
    # TODO
    u_cnt = len(list(dictUsers.keys()))
    u_ids = list(dictUsers.keys())
    for i in range(0, u_cnt):
        friends = list(dictUsers[u_ids[i]].friends)
        lat = []
        lon = []
        if dictUsers[u_ids[i]].home_shared == True or len(friends) == 0:
            dictUsersInferred[u_ids[i]] = dictUsers[u_ids[i]]
        if dictUsers[u_ids[i]].home_shared == False:
            for fri in friends:
                if dictUsers[fri].home_shared == True:
                    lat.append(dictUsers[fri].home_lat)
                    lon.append(dictUsers[fri].home_lon)
            if len(lat) != 0:
                dictUsersInferred[u_ids[i]] = User(u_ids[i], sum(lat) / len(lat), sum(lon) / len(lon))
            else:
                dictUsersInferred[u_ids[i]] = dictUsers[u_ids[i]]
    return dictUsersInferred


def cp2_2_improved_inference(dictUsers):
    dictUsersInferred = dict()  # dict to return, store inferred results here
    # you should keep everything in dictUsers as is / read-only
    # TODO
    u_cnt = len(list(dictUsers.keys()))
    u_ids = list(dictUsers.keys())
    inferred_lat = []
    inferred_lon = []
    for i in range(0, u_cnt):
        friends = list(dictUsers[u_ids[i]].friends)
        hs_friends = []
        for hs in range(len(friends)):
            if dictUsers[friends[hs]].home_shared == True:
                hs_friends.append(friends[hs])
        lat = []
        lon = []
        if dictUsers[u_ids[i]].home_shared == False and len(hs_friends) == 0:
            latlat = []
            lonlon = []
            for j in range(len(friends)):
                ff = list(dictUsers[friends[j]].friends)
                for fri in ff:
                    if dictUsers[fri].home_shared == True:
                        latlat.append(dictUsers[fri].home_lat)
                        lonlon.append(dictUsers[fri].home_lon)
            if len(latlat) != 0:
                dictUsersInferred[u_ids[i]] = User(u_ids[i], statistics.median(latlat), statistics.median(lonlon))
                continue
        if dictUsers[u_ids[i]].home_shared == True:
            dictUsersInferred[u_ids[i]] = dictUsers[u_ids[i]]
        if dictUsers[u_ids[i]].home_shared == False and len(friends) == 0:
            dictUsersInferred[u_ids[i]] = dictUsers[u_ids[i]]
        if dictUsers[u_ids[i]].home_shared == False and len(friends) != 0:
            for fri in friends:
                if dictUsers[fri].home_shared == True and dictUsers[fri].home_lat != 0 and dictUsers[fri].home_lon != 0:
                    lat.append(dictUsers[fri].home_lat)
                    lon.append(dictUsers[fri].home_lon)
            fri_lat = []
            fri_lon = []
            for j in range(len(lat)):
                lattitude = lat[j]
                longitude = lon[j]
                if lattitude >= sum(lat)/len(lat)-np.sqrt(np.var(lat)) and lattitude <= sum(lat)/len(lat)+np.sqrt(np.var(lat)) and longitude >= sum(lon)/len(lon)-np.sqrt(np.var(lon)) and longitude <= sum(lon)/len(lon)+np.sqrt(np.var(lon)):
                    fri_lat.append(lattitude)
                    fri_lon.append(longitude)
            if len(fri_lat) != 0:
                lat_infer = statistics.median(fri_lat)
                lon_infer = statistics.median(fri_lon)
                dictUsersInferred[u_ids[i]] = User(u_ids[i], lat_infer, lon_infer)
                inferred_lat.append(lat_infer)
                inferred_lon.append(lon_infer)
            else:
                if len(lat) != 0 and len(lat) != 1:
                    dictUsersInferred[u_ids[i]] = User(u_ids[i], statistics.median(lat), statistics.median(lon))
                else:
                    if len(inferred_lat) != 0:
                        dictUsersInferred[u_ids[i]] = User(u_ids[i], statistics.median(inferred_lat), statistics.median(inferred_lon))
                    else:
                        dictUsersInferred[u_ids[i]] = dictUsers[u_ids[i]]
    return dictUsersInferred


def cp2_calc_accuracy(truth_dict, inferred_dict):
    # distance_km(a,b): return distance between a and be in km
    # recommended standard: is accuate if distance to ground truth < 25km
    if len(truth_dict) != len(inferred_dict) or len(truth_dict)==0:
        return 0.0
    sum = 0
    for i in truth_dict:
        if truth_dict[i].home_shared:
            sum += 1
        elif truth_dict[i].latlon_valid() and inferred_dict[i].latlon_valid():
            if distance_km(truth_dict[i].home_lat, truth_dict[i].home_lon, inferred_dict[i].home_lat,
                           inferred_dict[i].home_lon) < 25.0:
                sum += 1
    return sum * 1.0 / len(truth_dict)
    
