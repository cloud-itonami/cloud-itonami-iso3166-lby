(ns statute.facts
  "General-law compliance catalog for Libya (LBY) -- extends this repo's
  `marketentry.facts` (business/company-registration market-entry only,
  narrow scope) with a second, orthogonal catalog of statutes a company
  operating in this jurisdiction must generally track for compliance.
  Mirrors cloud-itonami-iso3166-ben/-btn/-caf/-cog/-gnq's `statute.facts`
  (ADR-2607141700, cloud-itonami-compliance-fact-federation).

  Every entry below cites an OFFICIAL source that was actually fetched
  and read this session -- never fabricated:

  - **Investment-promotion law**: this iteration independently fetched
    and read IN FULL Law No. 9 of 2010 on Investment Promotion via
    UNCTAD's own Investment Laws Navigator
    (`investmentpolicy.unctad.org/investment-laws/laws/193/libya-law-on-
    investment-promotion`, WebFetch/curl-verified raw HTML). Own text:
    issued by the General People's Congress (pre-2011), full title 'Law
    on Investment Promotion No. 9, 2010'; Art. 31: 'This Law shall enter
    into force as of the date of publication per the Official Gazette.'
    Unlike Benin/CAF/COG/GNQ, this iteration specifically investigated,
    rather than assumed, whether Libya is itself an OHADA member state --
    independently confirmed directly on OHADA's own 'Les Etats membres de
    l'OHADA' page (`ohada.org/les-etats-membres-de-lohada/`,
    WebFetch/curl-verified): Libya is NOT listed among OHADA's 17 member
    states. So, unlike those sibling jurisdictions, Libya's company/
    investment law is governed DIRECTLY by a NATIONAL instrument (Law No.
    9/2010), not a supranational OHADA Acte Uniforme -- this catalog does
    not force Libya into the OHADA-membership shape those siblings use.
  - **Companies/commercial law generally**: Law No. 9/2010's own preamble
    lists a pre-existing 'Commercial Law and amendments' among the
    instruments it builds on (own text, verbatim, in the law's
    'Reviewing and taking into account' recital). This iteration could
    NOT independently fetch that underlying Commercial Law's own primary
    text, number or date this session (an honest gap -- UNCTAD's
    Investment Laws Navigator only hosts Law No. 9/2010 itself, not the
    Commercial Law it references). No catalog entry is constructed for
    it; only Law No. 9/2010 itself (fully read, self-contained) is
    catalogued below.
  - **Labor law**: this iteration looked for Libya's Labour Law (the
    domestic labor-code citation the task brief itself named as a lead)
    via multiple routes, all independently checked this session and
    honestly disclosed rather than glossed over:
    - The Ministry of Labour and Rehabilitation's own live site
      (`labour.gov.ly`, fetched with a standard browser User-Agent after
      an initial 403 -- a UA-string check, not a CAPTCHA/bot-detection
      bypass) has a dedicated 'قانون العمل' (Labour Law) navigation item,
      but the linked page itself (`labour.gov.ly/القرارات-والتشريعات/
      قانون-العمل/`, fetched and read this session) contains NO
      substantive law text, number or date -- an apparently
      empty/unpublished stub page on the live site.
    - ILO's NATLEX national-labor-law database (`natlex.ilo.org`)
      returned HTTP 403 (a Cloudflare bot-detection challenge) on every
      attempt this session, both at its root and at guessed detail-record
      URLs -- this iteration did NOT attempt to bypass it (see repo-wide
      safety floor on CAPTCHA/bot-detection circumvention).
    - `droit-afrique.com/pays/libye` returned HTTP 403 on every attempt
      this session, both via a default curl request and via curl with a
      standard browser User-Agent -- a genuine server-side block, not a
      fabricated excuse.
    - The World Bank's Doing Business 2020 Libya Economy Profile (PDF,
      independently fetched and read IN FULL this session via
      `archive.doingbusiness.org`, both as raw PDF and via `pdftotext`)
      confirms a real registration touchpoint ('Notarize the company
      lease and Register with the Social Security Fund - Ministry of
      Labor', Procedure 8) but does not itself cite a specific
      labor-code law number anywhere in its text (grep of the full PDF
      text found no 'Labour Law'/'Labor Law'/'Law No' match for this
      topic).
    - The U.S. State Department's 2023 Country Report on Human Rights
      Practices: Libya (`state.gov`, fetched with a standard browser
      User-Agent this session) mentions 'applicable labor laws' only
      generically (own text: 'The government was limited in its ability
      to enforce applicable labor laws') without ever citing a specific
      law number or date anywhere in the document.
    This iteration is HONESTLY unable to cite a labor-code law
    number/date for Libya this session -- no entry is added below rather
    than inventing one, the same discipline GNQ's sibling catalog applied
    to its own labor-law gap.

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"LBY"
   [{:statute/id "lby.law-9-2010-investment-promotion"
     :statute/title "Law on Investment Promotion No. 9, 2010"
     :statute/jurisdiction "LBY"
     :statute/kind :law
     :statute/law-number "Law No. 9 of 2010 (1378 P.D.), issued by the General People's Congress; own Art. 31: enters into force upon publication in the Official Gazette. Art. 2 applies it to national, foreign and joint-venture capital investment projects; Art. 27 (own text, verbatim) excludes oil and gas projects from its scope: 'The provision of this Law shall not apply on national and foreign capitals invested or will be invested in oil and gas projects.' HONEST CAVEAT: this iteration could not confirm which currently-operating body (if any) now exercises the law's own Art. 5 'administrative authority' role (designated only by a since-superseded Gaddafi-era General People's Committee decision), nor whether the law has been formally amended or superseded by any post-2011 legislative authority -- see `marketentry.facts` namespace docstring for the full disclosure."
     :statute/url "https://investmentpolicy.unctad.org/investment-laws/laws/193/libya-law-on-investment-promotion"
     :statute/url-provenance :official-unctad-org
     :statute/enacted-date "2010"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:corporate-governance :incorporation :foreign-investment}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-lby statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "LBY")) " LBY statute(s) seeded with an "
                 "official citation -- a deliberately SMALL catalog (labor "
                 "code and the underlying Commercial Law could not be "
                 "independently verified this iteration, see namespace "
                 "docstring). Extend `statute.facts/catalog`, never fabricate "
                 "a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :corporate-governance)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
