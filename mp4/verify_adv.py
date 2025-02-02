import os
import numpy as np
import json
import pickle
from sklearn.feature_extraction import DictVectorizer

class Verifier:
    def __init__(self, jsonFile):
        self.jsonFile = jsonFile
        self.features = [0] * 5000

    def getFeatureArray(self, featureList, addedFeatureList):
        with open(self.jsonFile, "r") as f:
            orig_data = json.load(f)
        with open(addedFeatureList, "r") as file:
            new_data = json.loads(file.read())
        keys_new = list(new_data.keys())
        for key_new in keys_new:
            orig_data[key_new] = new_data[key_new]
        return orig_data

if __name__ == "__main__":
    verifier_5E06B7 = Verifier(
        "./5E06B7510B55B52C94D2AB0D7BB94AAA454860C6F8729BA2842438D92CDB8EEE.json"
    )
    verifier_49875A = Verifier(
        "./49875A9C25EB18945A8E7F27C8188834CFF48070413604D477763EC7A20E9C4A.json"
    )
    input_file_folder = f"data"
    # featureList is the output file with the 5000 in the name
    # aFList_# is the additional features added to the json file of the outputs
    fList = f"{input_file_folder}/all_feature_names_5000.json"
    aFList_5E06B7 = f"{input_file_folder}/added-features-5E06B7.txt"
    aFList_49875A = f"{input_file_folder}/added-features-49875A.txt"

    # Merge the feature array with the added features
    feat_5 = verifier_5E06B7.getFeatureArray(fList, aFList_5E06B7)
    feat_4 = verifier_49875A.getFeatureArray(fList, aFList_49875A)
    # Load trained SVM model with 5000 features
    with open('mp4_SVM_models/svm-f5000.p', 'rb') as f:
        model = pickle.load(f)
    # Make predictions
    vec1 = DictVectorizer(sparse=True)
    feat_5 = vec1.fit_transform(feat_5)
    vec2 = DictVectorizer(sparse=True)
    feat_4 = vec2.fit_transform(feat_4)
    y_5 = model.clf.predict(feat_5)
    y_4 = model.clf.predict(feat_4)
    # Output predicted label and original label for each sample
    # Object needed to be printed out -  res = {"5E06B7": <value>, "49875A": <value>}
    # The predicted value can be 0 or 1.
    # Expected output
    # {"5E06B7": 1, "49875A": 0}
    res = {}
    res["5E06B7"] = y_5[0]
    res["49875A"] = y_4[0]
    print(res)

# Please put your thought as code comments below why these features might be helpful to make an adversarial sample.
# These features are selected based on the differences between a goodware and a malware.
# By adding some "unique" features of a goodware to a malware, the probability of the malware classified into a goodware is enhanced.
# Similarly, by adding some "unique" features of a malware to a goodware, the probability of the goodware classified into a malware is enhanced.
# These "unique" features might be helpful because both a malware and a goodware have specific characteristics or patterns that distinguish them from each other. 
# Incorporating these distinctive features into the respective opposite class can potentially confuse the classification model, leading to misclassification and thereby creating an adversarial sample.
