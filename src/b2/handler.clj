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
    (prn ":::" request)
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
                   ;(wrap-my-auth) 
                   ) (assoc-in site-defaults [:security :anti-forgery] true)))
