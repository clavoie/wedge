(ns wedge.private.core-tests
  (:require [clojure.test :refer :all]
            [wedge.private.core :refer :all]))

(deftest inc-or-1-tests
  (is (= 1 (inc-or-1 [])))
  (is (= 1 (inc-or-1 :hello)))
  (is (= 1 (inc-or-1 nil)))
  (is (= 1 (inc-or-1 0)))
  (is (= 2 (inc-or-1 1))))

(deftest add-call-tests
  (is (= {1 {\a {:hey 1}}} (add-call {} [1 \a :hey])))
  (is (= {1 {\a {:hey 2}}} (add-call {1 {\a {:hey 1}}} [1 \a :hey]))))
