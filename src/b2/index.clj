(ns b2.index
  (:use [hiccup.page :only [include-css html5 include-js]])
  (:require [clojure.java.jdbc :as jdbc]
            [ring.middleware.anti-forgery :as  anti-forgery]
            [clojure.data.json :as json]
            )
  )
(json/write-str [{:name "张三"} {:name "历史"}])

(let [db-protocol "tcp"            ; "file|mem|tcp"
      db-host     "localhost" ; "path|host:port"
      db-name     "~/mm"]

  (def db {:classname   "org.h2.Driver" ; must be in classpath
           :subprotocol "h2"
           :subname (str db-protocol "://" db-host "/" db-name)
           ; Any additional keys are passed to the driver
           ; as driver-specific properties.
           :user     "sa"
           :password ""}))

;HTML组件
(defn comp_box 
  [item]
  [:div 
   [:div {:class "1"} "姓名哈哈"  (:name item)]
   [:div "性别" (:sex item)]])
;显示用户列表的函数
(defn showUserLst []
  (jdbc/with-db-connection [db-con db]
    (let [;; fetch some rows using this connection
          rows (jdbc/query db-con ["SELECT * FROM user " ])]
      ;; insert a copy of the first row using the same connection
      (prn rows)
      (reduce #(conj %1 (comp_box %2)) 
        [:div] 
        rows)
      ))                    
  )
(jdbc/with-db-connection [db-con db]
  (jdbc/insert! db-con :user {:name "张三1" :sex 1}))
(reduce #(conj %1 [:div (%2 :name)]) 
        [:div] 
        (list {:name "abc" :sex 1})) 
(def btitle (str ".a{" "background-color"  "}"))
(defn index []
  (html5 [:head [:style btitle ".b"]] 
         [:body 
          [:div "主很没变异 简aaa单页"]
          (showUserLst)
          ] ))

;(auto-complete)
;表单字段
(defn comp-field [field]
  [:input field]
  )
;表单组件
(defn comp-form [{:keys [fields method action]}] 
  [:form {:method method :action action} 
   (vec (map   comp-field  fields))
   ]
  )
(comp-field {:name "1" :type "text"})
(comp-form {:fields 
            [{:name "1" :type "text"}
             {:name "2" :type "button"}]
            :method "GET"
            :action ""
            })

(defn field-text
 [field entity] 
  [:input.field-comm {:id (:name field) :name (:name field) :type "text" :value ((keyword (:name field)) entity) :style ""}]
  )


(defn td [field entity]
  (let [mField (merge {:type "text" :label "未定义" :render field-text} field)]
    (list
      [:td {:style "text-align:right"} [:label (:label mField)]] 
      [:td ((:render mField) field entity)] 
      )  
    )
  )
(defn tr [col entity]
  (reduce #(conj %1 (td %2 entity)) [:tr ] col)
  )
(defn row3-row [fields entity]
  (-> (partition-all 3  fields)
      ((partial map #(tr % entity)))
      )
  )
;3列的表单
(defn col3-form [entity]
  [:form {:method "post" :action "" :id "entityForm" } 
   [:input {:type "hidden" :name "__anti-forgery-token" :value anti-forgery/*anti-forgery-token*}]
   [:table  
    (row3-row (:fields (meta entity) entity) entity)
    [:tr 
     [:table
      [:td
       [:p
        ;[:button {:type "submit", :class "btn btn-info"} [:i {:class "glyphicon glyphicon-refresh"}] " 保存"]
        ;[:button {:type "submit", :class "btn btn-danger"} [:i {:class "glyphicon glyphicon-remove"}] " 删除"]
        ] 
       ]
      [:td
       ]]]]])
;bootstrap表单
(defn bt-form [form]
  [:div.row
   [:div {:class "col-md-12 panel-info"} 
    [:div {:class "content-box-header panel-heading"} [:div {:class "panel-title"} "案件信息"]
     [:div {:class "panel-options"} [:a {:shape "rect", :href "#", :data-rel "collapse"} [:i {:class "glyphicon glyphicon-refresh"}]] [:a {:shape "rect", :href "#", :data-rel "reload"} [:i {:class "glyphicon glyphicon-cog"}]]]
     ] 
    [:div {:class "content-box-large box-with-header"} 
     [:div.panel-body {} 
      [:div {:class "row"} 
       form
       ]]]]
   (include-js   "js/my-form.js" 
                        "/vendors/wdTree/src/Plugins/jquery.tree.js" "/vendors/wdTree/data/tree1.js")
   ]
  )
;标题栏
(def comp-header 
  [:div.header
           [:div.container
            [:div.row
             [:div.col-md-5
              [:div.logo
               [:h1 [:a {:href "ahdi" } "管理中心"]]]]
             [:div.col-md-5
              ]
             [:div {:class "col-md-2"} [:div {:class "navbar navbar-inverse", :role "banner"} [:nav {:class "collapse navbar-collapse bs-navbar-collapse navbar-right", :role "navigation"} [:ul {:class "nav navbar-nav"} [:li {:class "dropdown"} [:a {:shape "rect", :class "dropdown-toggle", :href "#", :data-toggle "dropdown"} "个人中心" [:b {:class "caret"}]] [:ul {:class "dropdown-menu animated fadeInUp"} [:li {} [:a {:shape "rect", :href "profile.html"} "修改密码"]] [:li {} [:a {:shape "rect", :href "login.html"} "注销"]]]]]]]]
             ]
            ]])
;左侧菜单
(def comp-left-menu
  [:div {:class "col-md-2"} 
   ;; [:div {:class "sidebar content-box", :style "display: block;"} 
   ;;  [:ul {:class "nav"} 
   ;;   [:li {:class "current"} [:a {:shape "rect", :href "index.html"} 
   ;;                            [:i {:class "glyphicon glyphicon-home"}] " Dashboard"]]
   ;;   [:li {} [:a {:shape "rect", :href "calendar.html"} [:i {:class "glyphicon glyphicon-calendar"}] " Calendar"]]
   ;;   [:li {} [:a {:shape "rect", :href "stats.html"} 
   ;;            [:i {:class "glyphicon glyphicon-stats"}] " Statistics (Charts)"]]
   ;;   [:li {} [:a {:shape "rect", :href "tables.html"} [:i {:class "glyphicon glyphicon-list"}] " Tables"]] 
   ;;   [:li {} [:a {:shape "rect", :href "buttons.html"} [:i {:class "glyphicon glyphicon-record"}] " Buttons"]] 
   ;;   [:li {} [:a {:shape "rect", :href "editors.html"} [:i {:class "glyphicon glyphicon-pencil"}] " 添加用户"]] 
   ;;   [:li {} [:a {:shape "rect", :href "forms.html"} [:i {:class "glyphicon glyphicon-tasks"}] " 查询用户"]] 
   ;;   [:li {:class "submenu"} [:a {:shape "rect", :href "#"} [:i {:class "glyphicon glyphicon-list"}] "报表统计" [:span {:class "caret pull-right"}]] 
   ;;    [:ul {} [:li {} [:a {:shape "rect", :href "login.html"} "Login"]] 
   ;;     [:li {} [:a {:shape "rect", :href "signup.html"} "Signup"]]]]]]
   [:div#accordion-menu
    [:h3 "基本信息"]
    [:div [:ul#menu-1 [:li [:span.ui-icon.ui-icon-disk] "用户信息 "]
           [:li [:span.ui-icon.ui-icon-zoomin] "添加用户 "]
           [:li [:span.ui-icon.ui-icon-zoomin] "添加角色 "][:li [:span.ui-icon.ui-icon-zoomin] "角色信息 "]]
     [:script  "$( '#menu-1' ).menu();"]]
    [:h3 "订单信息"]
    [:div "内容2"][:h3 "报表统计"]
    [:div "内容2"]]
   [:script "$( '#accordion-menu' ).accordion();"]
   ]
  )


;对话框组件
(defn comp-dlg
  [elementId content field entity]
  (let [data (:data field [])]
    [:div.field-comm {:style "position:relative;adding-right:20px"}
     (include-css "/vendors/wdTree/css/tree.css")
     [:input {:style "width:100%" :type "text2" :name (:name field) :value ((keyword (:name field)) entity) :id (:name field)}
      ]
     [:button {:style "position:absolute;padding:0px;margin:0px;box-sizing:border-box; width:20px; height:22px;top:0px;right:0px;z-index:1;" :class "btn btn-danger"
               :onclick (str "buildSingleSelectTreeField('" (:name field) "'," (json/write-str data) ");return false") :type "button" :role "button"} ]
     [:div {:style "display:none" :id elementId :title "测试"} 
      content
      ]]))
;树对话框字段
(defn field-tree-dlg
  [field entity]
  (let [elementId (str (:name field) "Dlg")]
  (comp-dlg elementId  [:div {:id (str elementId "Tree")}  "测试对话框哈勒"] field entity))
  )

(defn field-option
  [field entity]
  [:select.field-comm {:id (:name field) :name (:name field)}
    [:option {:value "hahah"} "hahd总ah"] 
    [:option {:value "中"} "hahah"]])
(defn field-autocomplete
  [field entity]
  (let [data (:data field []) ]
    [:input.field-comm {:id (:name field) :name (:name field) :type "text" :value ((keyword (:name field)) entity) :onfocus (str  "buildAutoComplete(" (json/write-str data)  ",'" (:name field) "')")}]
    ))
;单选按钮组，选项数据来自field的:data选项,数组类型
(defn field-radio
  ([field entity]
  (let [data (:data field [])]
    (reduce #(conj % (field-radio field entity %2)) [:div] data)  
    ))
    ([field entity item]
     [:label {:style "padding-left:5px"}
      [:input {:type "radio" :name (:name field) :value item :checked (= item ((keyword (:name field)) entity)) }]
      item] 
    )
  )




(defn field-password
 [field entity] 
  )
(defn field-datepicker
 [field entity] 
  [:input.field-comm {:id (:name field) :name (:name field) :type "text" :value ((keyword (:name field)) entity) :onfocus (str  "buildDatePicker('"  (:name field) "')")}]
  )
(defn comp-list-page
  []
  [:div.jq-ui 
   (include-css "/vendors/jqGrid/css/ui.jqgrid.css"
                ;"/vendors/jqGrid/css/ui.jqgrid-bootstrap-ui.css"
                ;"/vendors/jqGrid/css/ui.jqgrid-bootstrap.css" 
                )
   (include-js  "/vendors/jqGrid/js/i18n/grid.locale-cn.js" "/vendors/jqGrid/js/jquery.jqGrid.js" "/js/my-list-page.js" 
               )
   [:style "div[class*='ui'], a[class*='ui']{box-sizing: content-box} "]
   [:table#jqGrid1]
   [:div#jqGrid1-Pager]
   [:script
    (str "buildRemoteTable('jqGrid1'," (json/write-str {:caption "用户信息"}) ")")]
   [:button {:onclick "$('jqGrid1').jqGrid('options','loadonce',true)"} "测试"]
   ])

;数节点模型模板，id必须为string类型
(def nodeTemplate {:id "0",:text "root",:value "86",:showcheck true,:complete true,:isexpand true ,:checkstate  0 ,:hasChildren true})
;部门树结构
(def departmentTree [(merge nodeTemplate {:ChildNodes [(merge nodeTemplate  {:id "2" :text "财务部" :hasChildren false }) (merge nodeTemplate  {:id "1" :text "软件部" :hasChildren false })] :text "所有部门"})])

(defn  toolbar [entity]
  [:div [:div {:class "ui-widget-header " :style "border:0px;background:#ccc"}
         [:div#entity-toolbar.btn-group [:button {:class "save-btn "}  "保存" ] [:button {:class "print-btn"} "打印"] [:button "导入"] [:button "导出"]] 
         ]
   [:script "initToolbar({})"]])
(defn ui-form [form entity]
  [:div {:class "ui-dialog ui-widget" :style "width:98%;display:inline-block;z-index:0"}
   (include-js   "js/my-form.js" 
               "/vendors/wdTree/src/Plugins/jquery.tree.js" "/vendors/wdTree/data/tree1.js")
   [:style "div[class*='ui'], a[class*='ui'],button[class*='ui']{box-sizing: content-box} "]
   [:div {:class "ui-dialog-titlebar ui-widget-header ui-helper-clearfix"} [:span {:class "ui-dialog-title"} "标题"]]
  (toolbar entity)
   [:div {:style "padding:10px;background-color:white"} 
    form 
   ]])
(defn comp-page
  [request]
  (let [form (col3-form (with-meta (:params request) 
                                   {:fields [
                                             {:name "name" :label "用户名：" :render field-autocomplete :data [{:name "a" :value "儿童票" :label "儿童票(80元)"} {:name "b" :value "B" :label "MB"} {:name "c" :value "C"}]} {:name "path" :label "全名：" :data ["男" "女"] :render field-radio} {:name "password" :label "密码："}
                                             {:name "email" :label "邮箱：" :render field-option } {:name "mobile" :label "手机：" :render field-datepicker} {:name "qq" :label "QQ："}
                                             {:name "sex" :label "性别：" :render field-tree-dlg :data departmentTree} {:name "address" :label "地址："  }
                                             ]})) {entity :params} request]
    (html5 [:head 
            (include-css 
                        ; "/css/jquery-ui.css" 
                        ; "http://www.guriddo.net/demo/css/jquery-ui.css"
             ; "/css/ui/1.11.4/themes/Cupertino/jquery-ui.css"
              ;"/css/ui/1.11.4/themes/Flick/jquery-ui.css"
              "/bootstrap/css/bootstrap.min.css" 
              "http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"
                         "/css/styles.css" 
                         "/css/buttons.css" 
                         "/css/my-form.css")
            (include-js  "/js/jquery.js" "/bootstrap/js/bootstrap.min.js"  "/js/jquery-ui.js"   )]
           [:body
            comp-header 
            [:div.page-content
             [:div.row
              comp-left-menu 
              [:div.col-md-10
               ;(bt-form form)
               ;(comp-list-page)
               [:div.row
                (ui-form form {})
                ] [:div.row
                (include-js "/js/main-frame.js")
                [:iframe {:width "100%" :height "100px" :id "dlgFrame"  :frameborder "0" :src "/req" :onload "fixFrameSize(this)"}]
                ]]]]
            (include-js  "js/custom.js" )
            [:script 
             (str
               "$(function(){"

               "buildCategoryAutoComplete(" (json/write-str [{:name "a" :value "A" :label "MA脏" :category "ABC"} {:name "b" :value "B" :label "MB" :category "AB"} {:name "c" :value "C" :label "MC" :category "ABC"}]) "," (json/write-str {:valueField "name" :elementId "address"} ) ");"

               "});"
               )]])))

(defn form
  []
  (html5 [:header
          (include-css "/css/buttons.css")
          ]
         [:body
          [:form
           [:input {:type "text" :name ":name" :value "上"}]
           [:input {:type "submit" :value "btn"}]]]))
;(use 'pl.danieljanus.tagsoup)
;(parse "file:///z/workspace/clojure/b2/resources/public/temp.html")


