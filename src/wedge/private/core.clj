(ns wedge.private.core
  "Private functions for wedge.core. Do not reference symbols in this ns as they are subject to change")

(defn inc-or-1
  "Increments a value if it is an integer, otherwise returns one"
  [value]
  (if (integer? value)
    (inc value)
    1))

(defn increment-call
  [value]
  (if (vector? value)
    (let [[call-times children] value]
      [(inc call-times) children])
    [0 {}]))

(defn are-equal [preds args]
  (if (= (count preds) (count args))
    (every? true?
            (for [[pred arg] (partition 2 (interleave preds args))]
              (pred arg)))))

(comment

  {:a [0 {:b [1 {}]}]}

  )

(defn add-call
  "Records a fn call to a fn counter"
  [counter args]
  (assert (map? counter) "The fn counter must be a map")
  (if-let [entry (get counter args)]
    (assoc counter args (inc entry))
    (assoc counter args 1)))
