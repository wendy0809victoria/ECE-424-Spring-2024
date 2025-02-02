from timeit import default_timer as timer

import util
# import models
import numpy as np
import mp4_model as models


input_file_folder = f'data'
X_file_name = f'{input_file_folder}/apg-X.json'
y_file_name = f'{input_file_folder}/apg-y.json'
meta_file_name = f'{input_file_folder}/apg-meta.json'


def train_model(num_features):
    svm_model = models.SVM(X_file_name, y_file_name, meta_file_name, num_features=num_features)
    svm_model.generate()

    ''' generate all the feature names used by current mode '''
    feature_name_list = [svm_model.vec.feature_names_[i] for i in svm_model.column_idxs]
    util.dump_json(feature_name_list, 'data', f'all_feature_names_{svm_model.X_train.shape[1]}.json')

    report_train, report_test = util.evaluate_classifier_perf(svm_model)

    stat(svm_model)

    print('training perf: ', report_train)
    print('testing perf: ',report_test)
    perf = report_test['model_performance']
    return perf['f1'], perf['precision'], perf['recall']


def stat(svm_model):
    ''' TODO: implement me '''
    # return in the following order
    svm_model.generate(True)
    train_cnt = svm_model.X_train.shape[0]
    test_cnt = svm_model.X_test.shape[0]
    # train_mal_cnt = svm_model.m_train.shape[0]
    train_mal_cnt = len(svm_model.m_train)
    # train_benign_cnt = svm_model.y_train.shape[0]
    train_benign_cnt = len(svm_model.y_train)
    # test_mal_cnt = svm_model.m_test.shape[0]
    test_mal_cnt = len(svm_model.m_test)
    # test_benign_cnt = svm_model.y_test.shape[0]
    test_benign_cnt = len(svm_model.y_test)
    # train_cnt = test_cnt = train_mal_cnt = train_benign_cnt = test_mal_cnt = test_benign_cnt = None
    return train_cnt, test_cnt, train_mal_cnt, train_benign_cnt, test_mal_cnt, test_benign_cnt


if __name__ == '__main__':
    start = timer()
    train_model(num_features=None) # use all the features
    print('=' * 50)
    train_model(num_features=5000) # select 5000 features first, then train a new model
    end = timer()
    print(f'tik tok: {end - start:.4f} seconds')
