(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest lby-has-spec-basis
  (let [sb (facts/spec-basis "LBY")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/business-registration-spec-basis "LBY")))
    (is (some? (facts/office-balance-floor-spec-basis "LBY")))))

(deftest lby-rep-spec-basis-is-honestly-absent
  (testing "no representative/director personal-liability extension was found for LBY this iteration -- deliberately not claimed"
    (is (nil? (facts/rep-spec-basis "LBY")))))

(deftest lby-corporate-number-spec-basis-is-honestly-absent
  (testing "no distinct tax-identification-number-issuance step was independently confirmed for LBY this iteration -- deliberately not claimed"
    (is (nil? (facts/corporate-number-spec-basis "LBY")))))

(deftest lby-office-balance-floor-is-the-flagship-spec-basis
  (testing "Ministry of Economy and Trade's own LYD 150,000 minimum representative-office bank-balance figure is a real, directly-fetched documented figure -- not fabricated"
    (let [ob (facts/office-balance-floor-spec-basis "LBY")]
      (is (some? ob))
      (is (= 150000 (:office-balance-floor-lyd ob)))
      (is (string? (:office-balance-floor-legal-basis ob))))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ")))
  (is (nil? (facts/business-registration-spec-basis "ATL")))
  (is (nil? (facts/office-balance-floor-spec-basis "ATL"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "LBY")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "LBY" all)))
    (is (not (facts/required-evidence-satisfied? "LBY" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["LBY" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))
