(defproject b2 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring-auth "0.1.0"]
                 [ring/ring-defaults "0.1.2"]
                 ;;以下为数据库相关
                 [com.h2database/h2 "1.4.188"]
                 [org.clojure/java.jdbc "0.3.0"]
                 [korma "0.4.0"]
                 [org.clojure/data.json "0.2.6"];json工具
                 [clj-tagsoup/clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]];htmltohiccp
                 ]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler b2.handler/app
         :nrepl {:start? true
                 :port 9998}}
  :profiles
  {
;   :uberjar {:omit-source true
;             :env {:production true}
;             :aot :all}
   :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}
  :uberjar-name "b2.jar"
  )
