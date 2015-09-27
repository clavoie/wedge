(defproject wedge "1.0.1-SNAPSHOT"
  :description "Function call verification for use with clojure.test"
  :url "https://github.com/clavoie/wedge"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :plugins [[codox "0.8.13"]]
  :global-vars {*warn-on-reflection* true}
  :codox {:src-dir-uri "http://github.com/clavoie/wedge/blob/master/"
          :src-linenum-anchor-prefix "L"})
