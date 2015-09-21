(ns wedge.core
  (:require [clojure.test :as t]))

(defn inc-or-1 [v]
  (if (nil? v) 1 (inc v)))

(defn add-call [a args]
  (update-in a (vector args) inc-or-1))

(defn is-called? [counter times & args]
  (t/is (= (get-in @counter args 0) times)))

(defmacro def-stub [counter fn-name & body]
  `(let [~counter (atom {})
         origin-fn# ~fn-name]
     (with-redefs [~fn-name (fn [& args#] (swap! ~counter add-call args#) (apply origin-fn# args#))]
       ~@body)))
