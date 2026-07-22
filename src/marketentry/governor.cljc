(ns marketentry.governor
  "Market-Entry Compliance Governor -- the independent compliance layer
  that earns the MarketEntry-LLM the right to commit. The LLM has no
  notion of Libyan company/business-registration law, whether a claimed
  engagement fee actually equals base + months x rate, whether an
  engagement's own declared, maintained representative-office bank
  balance actually meets the Ministry of Economy and Trade's own
  documented LYD 150,000 floor, or when a draft stops being a draft and
  becomes a real-world Ministry of Economy and Trade / Commercial
  Registry filing, so this MUST be a separate system able to *reject* a
  proposal and fall back to HOLD.

  `:itonami.blueprint/governor` is `:market-entry-compliance-governor`
  (shared family keyword on blueprints).

  This blueprint's own text (docs/business-model.md Trust Controls:
  'any actual Ministry of Economy and Trade / Commercial Registry filing
  submission requires Market-Entry Compliance Governor clearance and
  always escalates to human sign-off'; 'a false or fabricated
  regulatory-requirement claim is a HARD hold') names exactly the checks
  below.

  Five checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them. The confidence/actuation gate is
  SOFT: it asks a human to look (low confidence / actuation), and the
  human may approve -- but see `marketentry.phase`: for `:stake
  :actuation/draft-filing`/`:actuation/submit-filing` NO phase ever
  allows auto-commit either. Two independent layers agree that
  actuation is always a human call.

    1. Spec-basis                  -- did the jurisdiction proposal cite
                                       an OFFICIAL source
                                       (`marketentry.facts`), or invent
                                       one?
    2. Evidence incomplete         -- for `:filing/draft`/
                                       `:filing/submit`, has the
                                       jurisdiction actually been
                                       assessed with a full evidence
                                       checklist on file?
    3. Office balance below floor  -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own declared,
                                       maintained representative-office
                                       bank balance
                                       (`:office-balance-lyd`) meets or
                                       exceeds the Ministry of Economy
                                       and Trade's own documented LYD
                                       150,000 floor, and HARD-hold if
                                       it falls short. FLAGSHIP check
                                       for this jurisdiction -- a
                                       MAINTAINED OPERATING BANK-BALANCE
                                       FLOOR for a foreign company's
                                       representative office (funded by
                                       ongoing parent-company
                                       remittances, not a one-time
                                       paid-in share-capital
                                       contribution to a newly-
                                       incorporated entity -- a
                                       genuinely different instrument
                                       from Equatorial Guinea's SARL
                                       paid-in-capital floor even though
                                       both are floor-shaped checks; no
                                       turnover formula, no flat
                                       monetary ceiling, no
                                       supplier-registry read, no
                                       3-tier value class, no
                                       bid-evaluation price adjustment,
                                       no sector set-membership).
                                       Sourced directly from Libya's own
                                       market-entry registrar (Ministry
                                       of Economy and Trade), not a
                                       secondary multilateral report.
                                       See `marketentry.facts` /
                                       `marketentry.registry`.
    4. Engagement fee mismatch     -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own `:claimed-
                                       fee` equals `base-fee +
                                       monthly-rate x monitoring-
                                       months` -- honest reapplication
                                       of the ground-truth-recompute
                                       discipline sibling actors use.
    5. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                       OR the op is `:filing/draft`/
                                       `:filing/submit` (REAL acts)
                                       -> escalate.

  Two more guards, double-draft/double-submit prevention, are enforced
  off dedicated `:drafted?`/`:submitted?` facts (never a `:status`
  value). Unlike GNQ's sibling catalog, LBY does NOT add a
  tax-identification-number-verification check: this iteration could not
  independently confirm a distinct tax-ID-issuance step for Libya (see
  `marketentry.facts` namespace docstring) -- a smaller, honest set of
  checks beats forcing in a check shape that isn't actually documented
  for this jurisdiction."
  (:require [marketentry.facts :as facts]
            [marketentry.registry :as registry]
            [marketentry.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Drafting a real Ministry of Economy and Trade / Commercial Registry
  filing package and submitting it are the two real-world actuation
  events this actor performs."
  #{:actuation/draft-filing :actuation/submit-filing})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:filing/draft`/`:filing/submit`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's market-entry requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :filing/draft :filing/submit} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:filing/draft`/`:filing/submit`, the jurisdiction's required
  registration evidence must actually be satisfied."
  [{:keys [op subject]} st]
  (when (contains? #{:filing/draft :filing/submit} op)
    (let [e (store/engagement st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction e) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(商業登記/口座残高証明/印紙税/商工会議所登録/社会保障登録等)が充足していない状態での提案"}]))))

(defn- office-balance-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own declared, maintained representative-office bank
  balance stays at or above the Ministry of Economy and Trade's own
  documented LYD 150,000 floor -- the flagship check this vertical adds.
  Entity-condition-gated (a no-op unless `:office-balance-lyd` is a
  declared number)."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (registry/office-balance-below-floor? e)
        [{:rule :office-balance-below-floor
          :detail (str subject " の申告口座残高(LYD " (:office-balance-lyd e)
                      ")がMinistry of Economy and Trade(وزارة الاقتصاد والتجارة)の"
                      "外国企業代表事務所口座残高基準(LYD " registry/office-balance-floor-lyd
                      ")を下回っており、提出提案は進められない")}]))))

(defn- engagement-fee-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own claimed fee equals base + months x rate."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when-not (registry/engagement-fee-matches-claim? e)
        [{:rule :engagement-fee-mismatch
          :detail (str subject " の申告手数料(" (:claimed-fee e)
                      ")が独立再計算値(" (registry/compute-engagement-fee e) ")と一致しない")}]))))

(defn- already-drafted-violations
  "For `:filing/draft`, refuses to draft the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/draft)
    (when (store/engagement-already-drafted? st subject)
      [{:rule :already-drafted
        :detail (str subject " は既にドラフト済み")}])))

(defn- already-submitted-violations
  "For `:filing/submit`, refuses to submit the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (when (store/engagement-already-submitted? st subject)
      [{:rule :already-submitted
        :detail (str subject " は既に提出済み")}])))

(defn check
  "Censors a MarketEntry-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (office-balance-violations request st)
                           (engagement-fee-mismatch-violations request st)
                           (already-drafted-violations request st)
                           (already-submitted-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
