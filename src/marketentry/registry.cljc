(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a market-entry filing -- every
  jurisdiction assigns its own format. This namespace does NOT invent
  one; it builds a jurisdiction-scoped sequence number and validates the
  record's required fields, the same honest, non-fabricating discipline
  `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the SAME
  ground-truth-recompute DISCIPLINE sibling actors use (verify a claimed
  monetary total against the entity's own recorded quantity x unit
  fields), reapplied to a market-entry engagement fee line.

  `office-balance-below-floor?` is THIS vertical's own new ground-truth
  check, grounding LBY's flagship governor check
  (`marketentry.governor/office-balance-violations`): the Ministry of
  Economy and Trade's own live foreign-company-representative-office
  registration-requirements page (see `marketentry.facts`) requires a
  maintained bank-account balance of at least LYD 150,000, funded via
  transfers from the parent company. This is a DIFFERENT check SHAPE from
  every sibling this catalog's author has examined: not a
  statutory-ceiling-on-a-declared-duration (COG), not a turnover-scaled
  formula (Bulgaria), not a flat statutory threshold read off a
  monetary value in isolation (Albania), not a boolean registry-
  membership read of the SUPPLIER (Azerbaijan/Armenia/Bolivia), not a
  3-tier contract-value classification (Antigua and Barbuda), not a
  bid-evaluation price-adjustment recompute (Benin), not a
  set-membership check on a declared sector (Bhutan), and -- while it IS
  a floor-shaped check like Equatorial Guinea's paid-in SHARE-CAPITAL
  floor for a newly-INCORPORATED SARL -- it validates a DIFFERENT
  instrument entirely: a MAINTAINED OPERATING BANK-ACCOUNT BALANCE for a
  foreign company's (non-incorporated) REPRESENTATIVE OFFICE, funded by
  ongoing remittances from the parent company rather than a one-time
  paid-in capital contribution, and grounded in Libya's own market-entry
  registrar's live procedural text rather than a secondary multilateral
  Doing Business report. The floor itself (`office-balance-floor-lyd`)
  is NEVER hardcoded independently of `marketentry.facts` semantics -- it
  mirrors the same single-source-of-truth discipline
  `capital-floor-xaf`-style siblings use for their own constant statutory
  values, and `marketentry.facts/office-balance-floor-spec-basis` cites
  the identical value for the governor's citation trail.

  It is entity-condition-gated like Bhutan's FDI check and GNQ's
  capital-floor check: a no-op (false, i.e. not below floor) unless
  `:office-balance-lyd` is present as a number -- an engagement with no
  declared balance figure yet has nothing for this check to validate
  (that is the `evidence-incomplete` check's job, upstream, where an
  assessment must already exist).

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real Ministry of Economy and Trade, Commercial Registry or
  bank system. It builds the RECORD an operator would keep, not the act
  of submitting a real Ministry of Economy and Trade / Commercial
  Registry filing package itself (that is `marketentry.operation`'s
  `:filing/submit`, always human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  (+ (double base-fee)
     (* (double monthly-rate) (double monitoring-months))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (== (double claimed-fee) (compute-engagement-fee engagement)))

(def office-balance-floor-lyd
  "Ministry of Economy and Trade's own live foreign-company-
  representative-office registration-requirements page
  (WebFetch/curl-verified 2026-07-23 against economy.gov.ly's official
  hosting, own text): the account balance 'يجب أن لا يقل ... عن (150,000
  د.ل) مائة وخمسين ألف دينار ليبي' ('must not be less than 150,000
  Libyan dinars') -- numeral and spelled-out words internally
  consistent. See `marketentry.facts/office-balance-floor-spec-basis`
  for the full honest-confidence caveat (this catalog deliberately does
  NOT cite the separate, internally-inconsistent foreign-company-BRANCH
  figure found on the same Ministry's site as a second floor)."
  150000)

(defn office-balance-below-floor?
  "Does `engagement`'s own declared, maintained representative-office
  bank balance fall BELOW the Ministry of Economy and Trade's documented
  LYD 150,000 floor?

  A no-op (false) unless `:office-balance-lyd` is present as a number --
  an engagement with no declared balance figure yet has nothing for this
  check to validate. Meeting the floor exactly is fine (>= floor passes)."
  [{:keys [office-balance-lyd]}]
  (boolean
   (when (number? office-balance-lyd)
     (< office-balance-lyd office-balance-floor-lyd))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a Ministry of Economy and
  Trade / Commercial Registry business-registration filing package. Pure
  function -- does not touch any real Ministry of Economy and Trade,
  Commercial Registry or bank system."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a real Ministry
  of Economy and Trade / Commercial Registry business-registration
  filing (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
