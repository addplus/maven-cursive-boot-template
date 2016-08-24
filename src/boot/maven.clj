(ns boot.maven
  (:refer-clojure :exclude [import])
  (:require clojure.xml clojure.pprint))

(defn dbg [x] (clojure.pprint/pprint x) x)

(defn xml->map
  "Converts a parsed xml tree into a hash-map tree."
  [node]
  (let [[first-content :as content] (:content node)
        array-content? (fn [content-seq]
                         (apply = (map (comp first keys) content-seq)))]
    {(:tag node) (if (map? first-content)
                   (let [content-seq (map xml->map content)]
                     (if (array-content? content-seq)
                       (mapv (comp first vals) content-seq)
                       (apply merge content-seq)))
                   first-content)}))

(defn coord->sym [{:keys [groupId artifactId]}]
  (symbol (str groupId "/" artifactId)))

(defn pom->clj
  "Converts
   {:groupId \"org.clojure\"
    :artifactId \"clojure\"
    :version \"1.8.0\"
    :scope \"compile\"
    :exclusions [{:groupId \"com.amazonaws\", :artifactId \"aws-java-sdk\"}
                 {:groupId \"com.amazonaws\", :artifactId \"amazon-kinesis-client\"}]}

   into

   [org.clojure/clojure \"1.8.0\" :scope \"compile\"
    :exclusions [com.amazonaws/aws-java-sdk
                 com.amazonaws/amazon-kinesis-client]]"
  [{:keys [version exclusions] :as pom-dep}]
  (let [kw-params (dissoc pom-dep :groupId :artifactId :version :exclusions)]
    (vec (apply concat
           [(coord->sym pom-dep) version]
           (when exclusions
             [:exclusions (mapv coord->sym exclusions)])
           kw-params))))

(defn import
  "Makes a vector of maps of deps from a local pom.xml file"
  ([] (import "pom.xml"))
  ([pom-file]
    (when-let [rdr (java.io.File. pom-file)]
     (->> rdr
       clojure.xml/parse
       ; dbg
       xml->map
       :project
       :dependencies
       (mapv pom->clj)))))

(comment
  (import "pom.example.xml"))
