(ns clopi.search
  (:require [clj-json.core :as json])) 


(defn clojars-search
  [query]
  (let [search-url (str "http://clojars.org/search?q=" query "&format=json")
        rdr (clojure.java.io/reader search-url)
        res (:results (json/parse-string (apply str (line-seq rdr))))]))

(defn augment-search
  "Perform a search query, but augment the results with a given depmap"
  ([query depmap ranked?]
   (let [res (augment-search query depmap)]
     #_(sort #() res)))
  ([query depmap]
   (let [res (clojars-search query)
         res_aug (reduce #(conj %1 (assoc %2 "deps" (depmap (%2 "SOME NAME HERE")))) [] res)])))



