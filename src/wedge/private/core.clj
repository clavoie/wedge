(ns wedge.private.core
  "Private functions for wedge.core. Do not reference symbols in this ns as they are subject to change")

(defn add-call
  "Records a fn call to a fn counter"
  [counter args]
  (assert (map? counter) "The fn counter must be a map")
  (if-let [entry (get counter args)]
    (assoc counter args (inc entry))
    (assoc counter args 1)))

(defn are-equal [preds args]
  (if (= (count preds) (count args))
    (every? true?
            (for [[pred arg] (partition 2 (interleave preds args))]
              (pred arg)))))

(defn sum-counts [counter preds]
  (assert (map? counter) "The counter must be a map")
  (apply + (for [[args call-count] counter :when (are-equal preds args)] call-count)))
