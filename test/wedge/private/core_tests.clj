(ns wedge.private.core-tests
  (:require [clojure.test :refer :all]
            [wedge.private.core :refer :all]))

(deftest assert-fn-args-tests
  (is (thrown? AssertionError (assert-fn-args nil)))
  (is (thrown? AssertionError (assert-fn-args 1)))
  (is (nil? (assert-fn-args [])))
  (is (nil? (assert-fn-args '())))
  (is (nil? (assert-fn-args [1 2 3]))))

(deftest assert-fn-counter-map-tests
  (is (thrown? AssertionError (assert-fn-counter-map nil)))
  (is (thrown? AssertionError (assert-fn-counter-map (atom {}))))
  (is (nil? (assert-fn-counter-map {}))))

(deftest assert-fn-counter-tests
  (is (thrown? AssertionError (assert-fn-counter {})))
  (is (thrown? AssertionError (assert-fn-counter nil)))
  (is (thrown? AssertionError (assert-fn-counter 1)))
  (is (thrown? AssertionError (assert-fn-counter (atom []))))
  (is (nil? (assert-fn-counter (atom {})))))

(deftest matches-fn-call-tests
  (let [eq-1 #(= 1 %)
        eq-2 #(= 2 %)
        eq-3 #(= 3 %)
        eq-t (fn [_] true)]
    (is (matches-fn-call [eq-1 eq-2 eq-3] [1 2 3]))
    (is (matches-fn-call [eq-1 eq-t eq-3] [1 2 3]))
    (is (not (matches-fn-call [eq-1 eq-2 eq-3] [1 5 3])))))

(deftest record-fn-call-tests
  (let [args [1 2 3]
        args2 [2 3 4]
        counter {args 1}]
    (is (= {args 2} (record-fn-call counter args)))
    (is (= {args 1 args2 1} (record-fn-call counter args2)))))

(deftest sum-counts-tests
  (let [eq-1 #(= 1 %)
        eq-2 #(= 2 %)
        eq-3 #(= 3 %)
        eq-t (fn [_] true)
        counter {[1 2 3] 1 [1 4 3] 2}]
    (is (= 0 (sum-counts counter [eq-1 eq-1 eq-1])))
    (is (= 1 (sum-counts counter [eq-1 eq-2 eq-3])))
    (is (= 3 (sum-counts counter [eq-1 eq-t eq-3])))))
