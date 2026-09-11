# cloud-itonami-iso3166-lby

**LBY**: Libya.

- No verifiable currently-operating agency ('Privatisation and
  Investment Board' / 'General Authority of Investment and Ownership of
  Public Companies') administering Law No. 9 of 2010 on Investment
  Promotion was found this iteration -- the Libyan Investment Authority
  (lia.ly) is confirmed to be Libya's sovereign wealth fund, NOT an
  investment-promotion agency. This vertical's market-entry mechanism is
  therefore grounded in the Ministry of Economy and Trade's own live
  company/business-registration and foreign-company-office procedures
  instead of a named investment-promotion authority.
- Libya is NOT an OHADA member state (independently confirmed) --
  company/investment law is purely national: Law No. 9 of 2010 on
  Investment Promotion (excludes oil & gas, Art. 27), administered in
  practice through the Ministry of Economy and Trade's Commercial
  Registry (confirmed by World Bank Doing Business 2020's own text).
- Ministry of Economy and Trade's own LYD 150,000 minimum maintained
  bank-balance requirement for a foreign company representative office
  (economy.gov.ly) -- this vertical's flagship governor check.

AGPL-3.0-or-later.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Libya:

- `src/culture/facts.cljk` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
