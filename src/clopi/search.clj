(ns clopi.search
  (:require [clj-json.core :as json])) 


(defn clojars-search
  [query]
  (let [search-url (str "http://clojars.org/search?q=" query "&format=json")
        rdr (clojure.java.io/reader search-url)
        resp (json/parse-string (apply str (line-seq rdr)))]
    (resp "results")))

(defn- clojars->depmap [m]
  (let [gn (m "group_name")
        jn (m "jar_name")]
    (if (= gn jn)
      (str gn)
      (str gn "/" jn))))

(defn augment-result [final-res res depmap]
  (let [deps (get (depmap (clojars->depmap res)) (res "version") 0)]
    (conj final-res (assoc res "deps" deps))))

(defn augment-search
  "Perform a search query, but augment the results with a given depmap"
  ([query depmap ranked?]
   (if ranked?
     (sort #(> (%1 "deps") (%2 "deps")) (augment-search query depmap))
     (augment-search query depmap)))
  ([query depmap]
   (let [res (clojars-search query)
         res_aug (reduce #(augment-result %1 %2 depmap) [] res)]
     res_aug)))

