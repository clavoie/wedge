(ns wedge.core-test
  (:require [clojure.test :refer :all]
            [wedge.core :refer :all]))

(defn call-a [one two]
  (+ one two))

(defn call-b [one two]
  (call-a (inc one) (inc two)))

(defn call-c
  ([] (call-c 1 2))
  ([one two] (call-b one two)))

(deftest called?-tests
  (with-wedge [call-a a-counter]
    (is (not (called? a-counter)))
    (is (= 3 (call-a 1 2)))
    (is (called? a-counter))))

(deftest total-calls-tests
  (with-wedge [call-a a-counter]
    (is (zero? (total-calls a-counter)))
    (is (= 3 (call-a 1 2)))
    (is (= 1 (total-calls a-counter)))
    (is (= 7 (call-a 3 4)))
    (is (= 2 (total-calls a-counter)))))

(deftest call-count-tests
  (with-wedge [call-a a-counter
               call-b b-counter
               call-c c-counter]
    (is (= 5 (call-c)))
    (is (= 7 (call-a 2 5)))

    (is (= 1 (call-count c-counter [])))
    (is (= 1 (call-count c-counter [1 2])))
    (is (= 1 (call-count c-counter [1 ?])))
    (is (= 1 (call-count c-counter [1 [? #(even? %)]])))
    (is (zero? (call-count c-counter [1 [? #(odd? %)]])))

    (is (= 1 (call-count b-counter [1 2])))
    (is (= 1 (call-count b-counter [? 2])))
    (is (= 1 (call-count b-counter [1 [? #(even? %)]])))
    (is (zero? (call-count b-counter [1 [? #(odd? %)]])))

    (is (= 1 (call-count a-counter [2 3])))
    (is (= 2 (call-count a-counter [2 ?])))
    (is (= 2 (call-count a-counter [2 [? #(odd? %)]])))
    (is (zero? (call-count a-counter [1 [? #(even? %)]])))))
