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

(defn augment-search
  "Perform a search query, but augment the results with a given depmap"
  ([query depmap ranked?]
   (let [res (augment-search query depmap)]
     #_(sort #() res)))
  ([query depmap]
   (let [res (clojars-search query)
         ;; TODO depmap is currently a seq of vectors eg: ["aleph" {"0.1.5-SNAPSHOT" 2, "0.1.0-SNAPSHOT" 1}], and below doesn't contain/use version info
         res_aug (reduce #(conj %1 (assoc %2 "deps" (depmap (clojars->depmap %2)))) [] res)]
     res_aug)))

