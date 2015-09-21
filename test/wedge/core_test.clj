(ns wedge.core-test
  (:require [clojure.test :refer :all]
            [wedge.core :refer :all]))

(defn call-a [t]
  (inc t))

(defn call-b [t]
  (call-a t))

(deftest def-stub-not-called-test
  (def-stub [counter call-a]
    (is-called? counter 0)))

(deftest def-stub-called-once-test
  (def-stub [counter call-a]
    (is-called? counter 0 0)
    (is (= 1 (call-b 0)))
    (is-called? counter 0)
    (is-called? counter 1 0)))
