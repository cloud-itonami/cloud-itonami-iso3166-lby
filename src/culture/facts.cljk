(ns culture.facts
  "Country-level regional-culture catalog for Libya (LBY) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"LBY"
   [{:culture/id "lby.dish.bazin"
     :culture/name "Bazin"
     :culture/country "LBY"
     :culture/kind :dish
     :culture/summary "Unleavened bread of Libyan cuisine prepared with barley, water and salt, typically served with a tomato sauce, eggs, potatoes and mutton or camel; recognized as a traditional and national food of Libya."
     :culture/url "https://en.wikipedia.org/wiki/Bazin_(bread)"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "lby.dish.asida"
     :culture/name "Asida"
     :culture/country "LBY"
     :culture/kind :dish
     :culture/summary "Arabic pudding made by stirring wheat flour into boiling water; the Libyan variation is served with a sweet date or carob syrup (rub) or honey, with melted butter around the asida."
     :culture/url "https://en.wikipedia.org/wiki/Asida"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "lby.dish.couscous"
     :culture/name "Couscous"
     :culture/country "LBY"
     :culture/kind :dish
     :culture/summary "Staple food throughout the Maghrebi cuisines of Algeria, Tunisia, Mauritania, Morocco and Libya; in Libya it is traditionally served with lamb, camel meat or beef, and as a dessert (maghrood) with dates, sesame and honey."
     :culture/url "https://en.wikipedia.org/wiki/Couscous"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "lby.dish.usban"
     :culture/name "Usban"
     :culture/country "LBY"
     :culture/kind :dish
     :culture/summary "Traditional North African sausage of rice, herbs, lamb, liver and heart, made in Tunisia and, to a lesser extent, Libya, typically served with rice or couscous on special occasions."
     :culture/url "https://en.wikipedia.org/wiki/Usban"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "lby.product.rub"
     :culture/name "Rub (date syrup)"
     :culture/country "LBY"
     :culture/kind :product
     :culture/summary "Thick, dark-brown, very sweet syrup extracted from dates or carob, widely used in Libya."
     :culture/url "https://en.wikipedia.org/wiki/Libyan_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "lby.beverage.mint-tea"
     :culture/name "Maghrebi mint tea"
     :culture/country "LBY"
     :culture/kind :beverage
     :culture/summary "Gunpowder green tea prepared with spearmint leaves and sugar, traditional to the Greater Maghreb including Libya, where mint tea is central to social life."
     :culture/url "https://en.wikipedia.org/wiki/Maghrebi_mint_tea"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "lby.heritage.leptis-magna"
     :culture/name "Leptis Magna"
     :culture/country "LBY"
     :culture/kind :heritage
     :culture/summary "Ancient Roman city in the Khoms region about 130 km east of Tripoli, Libya; the Archaeological Site of Leptis Magna was designated a UNESCO World Heritage Site in 1982."
     :culture/url "https://en.wikipedia.org/wiki/Leptis_Magna"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "lby.heritage.cyrene"
     :culture/name "Cyrene"
     :culture/country "LBY"
     :culture/kind :heritage
     :culture/summary "Ancient Greek colony and Roman city near present-day Shahhat in northeastern Libya, a UNESCO World Heritage Site since 1982."
     :culture/url "https://en.wikipedia.org/wiki/Cyrene,_Libya"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "lby.heritage.ghadames"
     :culture/name "Old Town of Ghadames"
     :culture/country "LBY"
     :culture/kind :heritage
     :culture/summary "Oasis town in the Nalut District of northwestern Libya near the Algerian and Tunisian borders; its old town was inscribed as a UNESCO World Heritage Site in 1986."
     :culture/url "https://en.wikipedia.org/wiki/Ghadames"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-lby culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "LBY"))
                 " LBY entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
