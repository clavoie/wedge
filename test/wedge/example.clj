(ns wedge.core-test
  (:require [clojure.test :refer :all]
            [wedge.core :refer :all]))

(defn assert-some-param [param]
  (assert (integer? param) "param needs to be an integer"))

(defn helper-fn [amount param]
  (+ amount param))

(defn main-fn [param]
  (assert-some-param param)
  (if (odd? (helper-fn 1 param))
    (* param param)
    (dec param)))

(deftest main-fn-tests
  (with-wedge [assert-some-param assert-counter
               helper-fn helper-counter]
    (is (= 9 (main-fn 2)))
    (is (called? assert-counter))
    (is (= 1 (main-fn 1)))
    (is (= 2 (total-calls helper-counter)))
    (is (= 1 (call-count helper-counter [1 1])))
    (is (= 2 (call-count helper-counter [1 ?])))
    (is (= 2 (call-count helper-counter [1 [? #(< 0 %)]])))))
