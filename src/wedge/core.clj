(ns wedge.core
  (:require
   [clojure.inspector :refer [atom?]]
   [wedge.private.core :as private]))

(defn not-called?
  [counter]
  (empty? @counter))

(defn called
  [counter arg-fns]
  (assert (atom? counter) "Counter parameter must be an atom")
  (get @counter args 0))

(defmacro def-stub [bindings & body]
  (assert (vector? bindings) "The bindings for def-stub must be a vector")
  (assert (even? (count bindings)) "There must be an even number of forms for def-stub bindings")
  (if (empty? bindings)
    `(do ~@body)
    (let [[fn-name counter] bindings]
      `(let [~counter (atom {})
             origin-fn# ~fn-name]
         (with-redefs [~fn-name (fn [& args#] (swap! ~counter ~private/add-call args#) (apply origin-fn# args#))]
           (def-stub [~@(vec (drop 2 bindings))]
             ~@body))))))
