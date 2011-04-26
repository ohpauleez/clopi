(ns clopi.core
  (:import (java.util.zip GZIPInputStream)
           (java.io StringReader)))


(defn gunzip
  "Unzip a gzip archive into an input stream"
  [archive]
  (GZIPInputStream. (clojure.java.io/input-stream archive)))

(defn project->map
  "Process a project.clj, generating a map of the data"
  [project-str]
  (if (< 1 (count project-str))
    (let [project-map-str (-> project-str
                            (.replace "(" "") 
                            (.replace ")" "") 
                            (.replaceFirst "defproject" "{") 
                            (str "}"))]
      (read-string project-map-str))
    {}))

#_(for [d [:dev-dependencies :dependencies]] (update-in r [d] #(vec (for [[k v] %] [(str k) v]))))
; that works, but merging/building is obviously not happening.
; outer for as a reduce?
; Eventually, they turn into strs though in count-dep-vec, so no real need here

(defn fetch-proj-url
  "Fetch the dependencies and dev-dependencies of a project.clj url"
  [url]
  (let [rdr (try
              (clojure.java.io/reader url)
              (catch Exception e (clojure.java.io/reader (StringReader. ""))))
        proj-map (try
                   (project->map (apply str (line-seq rdr)))
                   (catch Exception e {}))
        deps (try
               (select-keys proj-map [:dependencies :dev-dependencies])
               (catch Exception e (do #_(println "ERROR:" proj-map) {:dependencies [[]], :dev-dependencies [[]]})))]
    deps))

(defn fetch-github
  "Fetch the dependencies and dev-dependencies of a github host project"
  [url]
  (try
    (fetch-proj-url (.replaceFirst (str url "/raw/master/project.clj") "http:" "https:"))
    (catch Exception e {})))

(defn dispatch-url
  "Given a url, dispatch to the correct fetch-url call"
  [url]
  (condp #(.contains %2 %1) url
    "github"  (fetch-github url)
    ""        (fetch-proj-url url)))
 
(defn count-dep-vec
  [dep-vector stats-map-start]
  (reduce (fn [stats-map [proj-index version-str]]
            (let [proj-index (str proj-index)
                  ;version-str (.replaceFirst (str version-str) "\\[" "")] ;sometime version come in as "[1.2.0"
                  version-str (.replace (str version-str) "[" "")]
              (update-in stats-map [proj-index version-str] (fnil inc 0))))
          stats-map-start dep-vector))

(defn count-deps
  "Create a map of all artifacts, and how many times someone has a dependency on a specific version (depmap)"
  [deps]
  (reduce (fn [stats-map dep-map]
            (try
              (count-dep-vec (into (get dep-map :dependencies [])
                                   (get dep-map :dev-dependencies [])) stats-map)
              (catch Exception e (do (println "ERROR:" dep-map) stats-map))))
          (sorted-map) deps))

(defn ranked-depvec
  "Given a depmap, return a new vector [[XX 'artifact']...] sorted by the total dependency count, XX (sum of all versions)"
  [depmap]
  (sort #(> (second %1) (second %2))
        (map (fn [[artifact-name val-vec]] [artifact-name (apply + (vals val-vec))]) depmap)))

