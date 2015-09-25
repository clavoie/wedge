(ns wedge.private.core-tests
  (:require [clojure.test :refer :all]
            [wedge.private.core :refer :all]))

(deftest add-call-tests
  (let [args [1 2 3]
        args2 [2 3 4]
        counter {args 1}]
    (is (= {args 2} (add-call counter args)))
    (is (= {args 1 args2 1} (add-call counter args2)))))

(deftest are-equal-tests
  (let [eq-1 #(= 1 %)
        eq-2 #(= 2 %)
        eq-3 #(= 3 %)
        eq-t (fn [_] true)]
    (is (are-equal [eq-1 eq-2 eq-3] [1 2 3]))
    (is (are-equal [eq-1 eq-t eq-3] [1 2 3]))
    (is (not (are-equal [eq-1 eq-2 eq-3] [1 5 3])))))

(deftest sum-counts-tests
  (let [eq-1 #(= 1 %)
        eq-2 #(= 2 %)
        eq-3 #(= 3 %)
        eq-t (fn [_] true)
        counter {[1 2 3] 1 [1 4 3] 2}]
    (is (= 0 (sum-counts counter [eq-1 eq-1 eq-1])))
    (is (= 1 (sum-counts counter [eq-1 eq-2 eq-3])))
    (is (= 3 (sum-counts counter [eq-1 eq-t eq-3])))))
