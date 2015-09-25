(ns wedge.core
  (:require
   [clojure.test :as t]
   [wedge.private.core :as private]))

;; called-<
;; called->
;; called?

(defn is-called? [counter args times]
  (let [fn-called (get counter args 0)]
    (t/is (= fn-called times))))

(defmacro def-stub [bindings & body]
  (assert (vector? bindings) "The bindings for def-stub must be a vector")
  (assert (even? (count bindings)) "There must be an even number of forms for def-stub bindings")
  (let [[fn-name counter] bindings]
    `(let [~counter (atom {})
           origin-fn# ~fn-name]
       (with-redefs [~fn-name (fn [& args#] (swap! ~counter ~private/add-call args#) (apply origin-fn# args#))]
         ~@body))))
