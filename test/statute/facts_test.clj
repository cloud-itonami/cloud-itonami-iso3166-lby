(ns statute.facts-test
  (:require [kotoba.lang.text :as str]
            [clojure.test :refer [deftest is]]
            [statute.facts :as facts]))

(deftest lby-has-spec-basis
  (let [sb (facts/spec-basis "LBY")]
    (is (= 1 (count sb)))
    (is (every? #(str/starts-with? (:statute/url %) "https://") sb))
    (is (every? :statute/law-number sb))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["LBY" "JPN" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ATL" "JPN"] (:missing-jurisdictions c)))))

(deftest by-topic-filters
  (is (= ["lby.law-9-2010-investment-promotion"]
         (mapv :statute/id (facts/by-topic "LBY" :corporate-governance))))
  (is (empty? (facts/by-topic "LBY" :labor)))
  (is (empty? (facts/by-topic "ATL" :corporate-governance))))
