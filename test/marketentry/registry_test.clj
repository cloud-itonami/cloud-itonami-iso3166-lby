(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "LBY" 0)
        s (registry/register-submit "eng-1" "LBY" 0)]
    (is (= "LBY-DFT-000000" (get d "draft_number")))
    (is (= "LBY-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "LBY" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest office-balance-floor-recompute
  (testing "Ministry of Economy and Trade -- a declared office balance at or above the LYD 150,000 floor is fine"
    (is (false? (registry/office-balance-below-floor? {:office-balance-lyd 150000})))
    (is (false? (registry/office-balance-below-floor? {:office-balance-lyd 200000}))))
  (testing "a declared office balance below the floor is a violation"
    (is (true? (registry/office-balance-below-floor? {:office-balance-lyd 90000})))
    (is (true? (registry/office-balance-below-floor? {:office-balance-lyd 149999.99}))))
  (testing "entity-condition-gated: a no-op (false) unless :office-balance-lyd is a declared number"
    (is (false? (registry/office-balance-below-floor? {})))
    (is (false? (registry/office-balance-below-floor? {:office-balance-lyd nil})))
    (is (false? (registry/office-balance-below-floor? {:office-balance-lyd "unknown"})))))
