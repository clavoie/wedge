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
    (is (called? assert-counter))
    (is (= 1 (main-fn 1)))
    (is (= 2 (total-calls helper-counter)))
    (is (= 1 (call-count helper-counter [1 1])))
    (is (= 2 (call-count helper-counter [1 ?])))
    (is (= 2 (call-count helper-counter [1 [? #(< 0 %)]])))))

```

## Documentation

[API](http://clavoie.github.io/wedge/)

## Rational

Often times our is composed of fns for which there are already tests. Assertions, validations, helper functions, etc. When writing a test
for a top level fn, or a fn composed of other fns for which their are already tests, we don't want to attempt to test the logic in the
helper functions, we simply want to verify that the helper function is called. The helper fn test is responsible for verifying the helper fn,
not the test we are currently writing.

To this end, wedge. Hopefuly most of the time `called?` is sufficient for most needs, but there is some flexibility added in case you need to
be as flexible as Moq, or some similar library.

## License

Copyright © 2015 Chris LaVoie

Distributed under the Eclipse Public License, the same as Clojure.
