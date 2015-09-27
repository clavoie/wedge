(ns wedge.core
  "Function call verification for use with clojure.test"
  (:require
   [wedge.private.core :as private]))

(defn called?
  "Tests that a fn is called at all, for any set of parameters. Returns true if the fn is called inside the with-wedge binding, and false otherwise

  fn-counter - the counter which tracks the number of times a specific fn is called while within a with-wedge binding"
  [fn-counter]
  (private/assert-fn-counter fn-counter)
  (not-empty @fn-counter))

(defn total-calls
  "Counts all calls to the fn, regardless of the arguments passed to it

  fn-counter - the counter which tracks the number of times a specific fn is called while within a with-wedge binding"
  [fn-counter]
  (private/assert-fn-counter fn-counter)
  (apply + (vals @fn-counter)))

(defmacro call-count
  "Counts the number of times a fn has been called within a with-wedge binding for a certain set of arguments to the fn.

  Example:

  ;; assume the fn (defn foo [a b] ...) and the following code is inside a (with-wedge [foo foo-counter] ...) binding

  (call-count foo-counter [1 2]) ;; counts the number of times (foo 1 2) was called
  (call-count foo-counter [? 2]) ;; counts the number of times foo was called with 2 for the 'b' parameter. (foo 1 2) and (foo 2 2) would both match
  (call-count foo-counter [? ?]) ;; counts the number of times foo was called
  (call-count foo-counter [1 [? #(< 1 %)]]) ;; counts the number of times foo was called with the 'b' parameter value greater than 1. (foo 1 2) would match but
                                               (foo 1 1) would not

  fn-counter  - the counter which tracks the number of times a specific fn is called while within a with-wedge binding
  search-args - a vector of arguments matching the fn calls which should be counted. The parameters to the vector can either be the literal values used to call the fn,
                the special form '?' to mean 'all argument values', or the special form '[? predicate]', where predicate is a fn which takes one argument and returns
                a true or false value indicating if that argument should be included in the fn calls counted."
  [fn-counter search-args]
  (assert (vector? search-args) "search-args must be a vector")
  (let [search-predicates
        (for [search-arg search-args]
          (cond
           (and (vector? search-arg) (= (count search-arg) 2) (= '? (first search-arg))) (second search-arg)
           (= '? search-arg) '(fn [_] true)
           true `(fn [fn-args#] (= fn-args# ~search-arg))))]
    `(private/sum-counts (deref ~fn-counter) ~(vec search-predicates))))

(defmacro with-wedge
  "Defines a set of bindings to track the number of times a fn is called within the body of with-wedge. Redefs the fns to a shim fn which calls the original fn, counts
  the call to the fn, and records with what arguments the fn was called. More than one fn can be tracked at a time within the body of with-wedge. Use wedge.core/call-count
  or one of its derrivative fns/macros to count the number of times a fn was called within the body of with-wedge.

  Example:

  ;; assume the fn (defn foo [a b] ...) and (bar [c d] ...)

  (deftest foo-tests
    (with-wedge [foo foo-counter bar bar-counter]
      (is (zero? (count-call foo-counter [? ?])))
      ;; etc
    ))

  bindings - a vector defining a relationship between a fn which should be tracked and an object which will track the calls to the fn. There must be an even number of symbols in
            the binding vector. The first symbol in the binding vector must be the name of a fn to track calls to, the second symbol in the vector must be a name for the object
            to track the calls to the fn.
  body     - the body of the test
  "
  [bindings & body]
  (assert (vector? bindings) "The bindings for with-wedge must be a vector")
  (assert (even? (count bindings)) "There must be an even number of forms for with-wedge bindings")
  (if (empty? bindings)
    `(do ~@body)
    (let [[fn-name fn-counter] bindings]
      `(let [~fn-counter (atom {})
             origin-fn# ~fn-name]
         (with-redefs [~fn-name (fn [& args#] (swap! ~fn-counter ~private/record-fn-call (if (nil? args#) [] args#)) (apply origin-fn# args#))]
           (with-wedge [~@(vec (drop 2 bindings))]
             ~@body))))))
