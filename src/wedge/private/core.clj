(ns wedge.private.core
  "Private functions for wedge.core. Do not reference symbols in this ns as they are subject to change"
  (:require
   [clojure.inspector :refer [atom?]]))

(defn assert-fn-args
  [fn-args]
  (assert (coll? fn-args) "fn-args parameter must be a collection"))

(defn assert-fn-counter-map
  [fn-counter-map]
  (assert (map? fn-counter-map) "fn-counter-map parameter must be an atom containing a map"))

(defn assert-fn-counter
  "Asserts that a fn-counter parameter is of the correct type. Raises an error if it is not.

  fn-counter - the fn counter parameter to verify"
  [fn-counter]
  (assert (atom? fn-counter) "fn-counter parameter must be an atom")
  (assert-fn-counter-map @fn-counter))

(defn add-call
  "Records a fn call to a fn counter map, returning the new fn-counter map with the updated fn call counts.

  fn-counter-map - the map containing the number of times a fn has been called with certain parameters
  fn-args        - the vector containing the args with which the fn under test was called"
  [fn-counter-map fn-args]
  (assert-fn-counter-map fn-counter-map)
  (assert-fn-args fn-args)
  (if-let [entry (get fn-counter-map fn-args)]
    (assoc fn-counter-map fn-args (inc entry))
    (assoc fn-counter-map fn-args 1)))

(defn are-equal
  "Tests that all the predicates match a fn call, returning true if they do and falsy if they do not. Predicates will be called on the fn args in order.
  The first pred will be called on the first arg, the second pred on the second arg, and so forth

  preds   - a sequence of fns that take a single parameter and return true if the fn call arg should be included in the fn call count, or falsy otherwise
  fn-args - vector containing the args with which the fn under test was called"
  [preds fn-args]
  (if (= (count preds) (count fn-args))
    (every? true?
            (for [[pred fn-arg] (partition 2 (interleave preds fn-args))]
              (pred fn-arg)))))

(defn sum-counts
  "Sums all the times a fn was called for the given set of argument predicates. Returns the total number of times a fn was called while under test

  fn-counter - fn call counter for a specific fn under test
  preds      - a sequence of fns that take a single parameter and return true if the fn call arg should be included in the fn call count, or falsy otherwise"
  [fn-counter-map preds]
  (assert-fn-counter-map fn-counter-map)
  (apply + (for [[fn-args call-count] fn-counter-map :when (are-equal preds fn-args)] call-count)))

