(ns wedge.core-test
  (:require [clojure.test :refer :all]
            [wedge.core :refer :all]))


(defn call-a [t]
  (inc t))

(defn call-b [t]
  (call-a t))

(defn call-c
  ([] 1)
  ([a] 2))



(comment

  (deftest is-called?-tests
  (let [counter (atom {1 {}})]
    ))


(deftest def-stub-not-called-test
  ;; maybe should be def-stub [counter a]
  (def-stub [call-a counter]
    (is-called? counter [0] 0)))

(deftest def-stub-called-once-test
  (def-stub [call-a counter]
    (is-called? counter [0] 0)
    (is (= 1 (call-b 0)))
    (println @counter)
    (is-called? counter [] 0)
    (is-called? counter [0] 1)))
)
