(ns b2.handler
  (:use [hiccup.page :only [include-css html5]]
        [ring.middleware.cookies]
        [ring.middleware.session]
        [ring-auth.middleware]
        [ring.util.response]
        [compojure.response]
        )
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [resourceful :refer [resource]]
            [clojure.data.json :as json]
            [b2.index ]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            )
  (:gen-class))
(defn layout []
  (html5 [:div  "ha模板hah"]))
;认证判断
(defn authorized? [request]
             (prn request "释放人质")
             true)
;认证管理中间件
(defn wrap-my-auth [handler]
  (fn [request]
    ;(prn ":::" request)
    (if (authorized? request)
      (handler request)
      (-> (response "Access Denied")
          (status 403)))))

(defroutes app-routes
  ;(GET "/" [] (b2.index/index))
  (resource "Collection of the books of an author"
            "/authors/:author/books"
            (GET [author]
                 (str author))
            (POST [author title]
                  (str author)
                  (str author)))
  (resource "用户管理web api"
            "/users/:user"
            ;get
            (GET [user ]
                 (str user ))
            ;delete
            (DELETE [user ]
                 (str user ))
            ;partial update
            (PATCH [user title]
                 (str user)
                 (str user))
            ;update
            (PUT [user title]
                  (str user)
                  (str user)))
  (resource "用户管理web api"
            "/users"
            ;list
            (GET [page page-size]
                 (json/write-str {:total 1000 :page 2 :rows (into [
                                                             {:id 1 :name "im" :sex "男"}
                                                             {:id 2 :name "李四" :sex "女"}
                                                             {:id 4 :name "张三" :sex "男"}]
                                                                  (for [x (range 100)] {:id x :name (str "name" x) :sex "mail"}))  }))
            ;create
            (POST [user title]
                 (str user)
                 (str user)))
  (GET "/" request [name] (str request name))
  (GET "/sayHello"  [name sex ]  (str  name sex))
  (GET "/req"  request  (str  request))
  (GET "/resp"  request  (response "tttt"))
  (ANY "/form"  request  (prn request) (b2.index/comp-page request))
  (ANY "/session"  
       {session :session}
       (if (:my-var session)
         {:body "Session variable already set"
          :headers {"Content-Type" "text/html; charset=utf-8"}
          :content-type "text/html; charset=utf-8"
          :session (assoc session :my-var "namenew")
          }
         {:status 200
          :body "Nothing in session, setting the var" 
          :headers {"Content-Type" "text/html; charset=utf-8"}
          :session (assoc session :my-var "namen")
          }
          )
       )
  (route/not-found "Not Found"))
(def app
  (wrap-defaults (-> 
                   app-routes
                   ;(wrap-auth-session )
                   ;(wrap-session )
                   ;(wrap-cookies )
                   (wrap-my-auth) 
                   ) (assoc-in site-defaults [:security :anti-forgery] true)))
