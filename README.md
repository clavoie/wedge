wedge
=====

Function call verification for use with clojure.test

## Installation

Add the following dependency to your `project.clj` file:

[![Clojars Project](http://clojars.org/wedge/latest-version.svg)](http://clojars.org/wedge)

## Usage

```clojure
(ns my-tests
  (:require [clojure.test :refer :all]
            [wedge.core :refer :all]))

(defn assert-some-param [param]
  (assert (integer? param) "param needs to be an integer"))

(defn helper-fn [amount param]
  (+ amount param))

(defn main-fn [param]
  (assert-some-param param)
  (if (odd? (helper-fn 1 param))
    (* param param)
    (dec param)))

(deftest main-fn-tests
  (with-wedge [assert-some-param assert-counter
               helper-fn helper-counter]
    (is (= 9 (main-fn 2)))
    
    ;; was assert-some-param called at all?
    (is (called? assert-counter))
    
    (is (= 1 (main-fn 1)))
    
    ;; count the total number of times helper-fn was called
    (is (= 2 (total-calls helper-counter)))
    
    ;; count the total number of times helper-fn was call with '1' for both arguments
    (is (= 1 (call-count helper-counter [1 1])))
    
    ;; count the total number of times helper-fn was call with '1' for the first argument
    (is (= 2 (call-count helper-counter [1 ?])))
    
    ;; count the total number of times helper-fn was call with '1' for the first argument
    ;;  and any second argument greater than 0
    (is (= 2 (call-count helper-counter [1 [? #(< 0 %)]])))))

```

## Documentation

[API](http://clavoie.github.io/wedge/)

## Rationale

Often times our code is composed of fns for which there are already tests. Assertions, validations, helper functions, etc. When writing a test
for a top level fn, or a fn composed of other fns for which their are already tests, we don't want to attempt to test the logic in the
helper fns, we simply want to verify that the helper fn is called. The helper fn test is responsible for verifying the helper fn,
not the test we are currently writing.

To this end, wedge. Hopefuly most of the time `called?` is sufficient for a majority of needs, but there is some flexibility added in case you need to
test a range of parameters, such as Moq, or some similar library.

## License

Copyright © 2015 Chris LaVoie

Distributed under the Eclipse Public License, the same as Clojure.
