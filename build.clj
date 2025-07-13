(ns build
  (:require
   [clojure.edn :as edn]
   [clojure.tools.build.api :as b]
   [toddler.template :as template]))

(defonce salt (template/random-string))
(defonce index-file (format "/docs.index.%s.edn" salt))

(defn frontend-config
  []
  (let [config (edn/read-string (slurp "shadow-cljs.edn"))
        {{main-build :dev} :builds} config]
    (->
     main-build
     (update :modules (fn [current]
                        (reduce-kv
                         (fn [r k v]
                           (assoc r (keyword (str (name k) "." salt))
                                  (if (contains? v :depends-on)
                                    (update v :depends-on
                                            (fn [deps]
                                              (reduce
                                               (fn [r d]
                                                 (conj r (keyword (str (name d) \. salt))))
                                               #{}
                                               deps)))
                                    v)))
                         nil
                         current)))
     (dissoc :output-to :output-dir)
     (assoc :closure-defines `{eywa.main/ROUTER_BASE "eywa"
                               eywa.docs.lazy/MD_BASE "https://raw.githubusercontent.com/neyho/eywa-docs/refs/heads/master/web"
                               eywa.docs.lazy/MD_REFRESH_PERIOD 0
                               eywa.docs.lazy/SEARCH_INDEX ~(str "/eywa" index-file)}
            :asset-path "/eywa/js"))))

(defn search-index-config
  [root]
  (letfn [(->link [route]
            (str root route))]
    {:output (str "target/" index-file)
     :mds [;;
           {:route (->link "/overview")
            :topic "Overview"
            :path "web/docs/overview.md"}
           ;;
           {:route (->link "/graphql")
            :topic "GraphQL"
            :path "web/docs/graphql.md"}
           {:route (->link "/graphql/queries")
            :topic "GraphQL Queries"
            :path "web/docs/graphql/queries.md"}
           {:route (->link "/graphql/mutations")
            :topic "GraphQL Mutations"
            :path "web/docs/graphql/mutations.md"}
           {:route (->link "/graphql/extend")
            :topic "GraphQL Extend"
            :path "web/docs/graphql/extend.md"}
           ;;
           {:route (->link "/iam")
            :topic "IAM"
            :path "web/docs/iam.md"}
           ;;
           {:route (->link "/data")
            :topic "Data Modeling"
            :path "web/docs/data.md"}
           ;;
           {:route (->link "/advanced")
            :topic "Advanced"
            :path "web/docs/advanced.md"}
           {:route (->link "/advanced/cli")
            :topic "Advanced CLI"
            :path "web/docs/advanced/cli.md"}
           {:route (->link "/advanced/encryption")
            :topic "Advanced Encryption"
            :path "web/docs/advanced/encryption.md"}
           {:route (->link "/advanced/environment")
            :topic "Advanced Environment"
            :path "web/docs/advanced/environment.md"}]}))

(defn release
  [_]
  ;; BUILD CSS
  (println "Cleaining target")
  (b/delete {:path "target/"})
  (b/process
   {:command-args ["clj" "-X:shadow:css" "salt" salt]})
  (let [command ["npx" "shadow-cljs" "-A:shadow:showcase" "--config-merge" (str (frontend-config)) "release" "production"]]
    (b/process
     {:command-args command
      :out :capture
      :err :capture}))
  ;;
  (println "Refreshing docs/index.html")
  (b/delete {:path "docs/index.html"})
  (template/process "index.html.tmp" "target/index.html" {:salt salt :root "/eywa"})
  ;;
  (println "Refreshing docs/404.html")
  (b/delete {:path "docs/404.html"})
  (template/process "index.html.tmp" "target/404.html" {:salt salt :root "/eywa"})
  (comment
    (println (slurp (clojure.java.io/resource "index.html.tmp"))))
  ;;
  (b/copy-dir {:src-dirs ["web/icons"]
               :target-dir "target/icons"})
  (b/copy-dir {:src-dirs ["web/img"]
               :target-dir "target/img"})

  (b/copy-file {:src "web/favicon.svg"
                :target "target/favicon.svg"})
  ;;
  (let [{:keys [mds output]} (search-index-config "/eywa/docs")
        _ (println "Refreshing index file at " (str output))
        {:keys [err]} (b/process
                       {:command-args ["clj" "-X:index" ":mds" (str mds) ":output" (str output)]
                        :out :capture
                        :err :capture})]
    (when (not-empty err)
      (println "[ERROR] " err))))
