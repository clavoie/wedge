wedge
=====

Function call verification for use with clojure.test

## Installation

Add the following dependency to your `project.clj` file:

[![Clojars Project](http://clojars.org/lock-key/latest-version.svg)](http://clojars.org/wedge)

## Usage

```clojure
(ns my-tests
  (:require [clojure.test :refer :all]
            [wedge.core :refer :all]))

(defn call-a [one two]
  (+ one two))

(defn call-b [one two]
  (call-a one two))

(defn call-c
  ([] (call-b 1 2))
  ([one] (call-b one 2))
  ([one two] (call-b one two)))

(deftest def-stub-not-called-test
  (def-stub [call-a counter]
    (is (= (called counter []) 0))
    (is (= (called counter [0]) 0))
    (is (= (called counter [0 0]) 0)))

(deftest def-stub-called-once-test
  (def-stub [call-a counter]
    (is (= (called counter [0 0]) 0))
    (is (= 1 (call-b 0 1)))
    (is (= (called counter []) 0))
    (is (= (called counter [0]) 0))
    (is (= (called counter [0 1]) 1))
    (is (= 1 (call-b 0 1)))
    (is (= (called counter [0 1]) 2)))))

```

## Documentation

[API](http://clavoie.github.io/wedge/)

## License

Copyright © 2015 Chris LaVoie

Distributed under the Eclipse Public License, the same as Clojure.
