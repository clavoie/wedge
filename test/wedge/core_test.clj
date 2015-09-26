(ns wedge.core-test
  (:require [clojure.test :refer :all]
            [wedge.core :refer :all]))

(defn call-a [one two]
  (+ one two))

(defn call-b [one two]
  (call-a one two))

(defn call-c
  ([] (call-b 1 2))
  ([one] (call-b one 2))
  ([one two] (call-b one two)))

(deftest call-count-tests
  (with-wedge [call-a a-counter]
    (is (= (call-count a-counter [? ?]) 0))
    (call-b 0 1)
    (call-b 0 2)
    (is (= (call-count a-counter [? ?]) 2))
    (is (= (call-count a-counter [? 2]) 1))
    (is (= (call-count a-counter [0 2]) 1))
    (is (= (call-count a-counter [0 [? #(> % 1)]]) 1))))
