(ns yarop.core-test
  (:require [clojure.test :refer :all]
            [yarop.core :refer :all]))

(deftest result-from-value-test
  (testing "result-from-value returns a proper result"
    (let [res (value->result "value")]
      (is (= (count res) 2) "the returned result should be a pair")
      (is (= (first res) (nth res 0) (res 0) (value res) "value") "the first element should be the value")
      (is (= (second res) (nth res 1) (res 1) (error res) nil) "the second element should be `nil`")
      (is (success? res) "the returned result should represent success"))))

(deftest result-from-error-test
  (testing "result-from-error returns a proper result"
    (let [res (error->result "error")]
      (is (= (count res) 2) "the returned result should be a pair")
      (is (= (first res) (nth res 0) (res 0) (value res) nil) "the first element should be `nil`")
      (is (= (second res) (nth res 1) (res 1) (error res) "error") "the second element should be the error")
      (is (failure? res) "the returned result should represent failure"))))

(deftest result-test
  (testing "result returns a proper result"
    (let [res (result "value" "error")]
      (is (= (count res) 2) "the returned result should be a pair")
      (is (= (first res) (nth res 0) (res 0) (value res) "value")
          "the first element should be the value")
      (is (= (second res) (nth res 1) (res 1) (error res) "error")
          "the second element should be the error"))))

(deftest =>-test
  (let [error-code "some error"
        f-success (fn [x] (value->result (inc x)))
        f-failure (fn [x] (error->result error-code))]
    (testing "both successful"
      (is (= (=> 0 f-success f-success) (value->result 2))))
    (testing "second fails"
      (is (= (=> 0 f-success f-failure) (result nil error-code))))
    (testing "first fails"
      (is (= (=> 0 f-failure f-failure) (result nil error-code))))))

