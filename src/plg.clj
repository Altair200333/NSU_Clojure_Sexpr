(ns src.plg)
(require '[clojure.string :as str] 
         '[clojure.edn :as edn]
         '[src.lang :refer :all]
         '[src.utils :refer :all]
         '[src.samples :refer :all]
         '[src.schema :refer :all]
         '[src.spath :refer :all])



;; This file is for testing things it may even be invalid 

(element-type (tag :div))
(element-type "sd")

(string? (::tag "asd"))

(tag? (::tag "asd"))

(tag? (tag :div "ds"))
(tag? "sda")
(tag? (tag :div))

(tag-args (tag :div "asd"))
(second (tag-args (tag :div "1" "2" "3" "4")))
(tag-args (tag :div "asd" "dsa" (tag :br)))
(tag-name (tag :div "asd" "dsa" (tag :br)))

;; matchers

(name-is (tag :div "aa") :div)
(name-is (tag :div "aa") "div")
(name-is (tag :div "aa") :br)

(value-is (tag :div "x") "x")
(value-is (tag :div "x") "d")
(value-is (tag :div "x" "d") "x")

(values-contain (tag :div "x" "d") "x")
(values-contain (tag :div "x" "d") "x2")
(values-contain (tag :div "x" (tag :br)) (tag :div))

(first (tag-args (last (tag-args use-sample))))

(tag :br)

;; ---

(tag-args use-sample)
(tag-args (tag :key))

(str/split "path/d/te" #"/")
(re-matches #"(a+)(b+)(\d+)" "abb234")

;; list of tag predicates (...)
;; {:tag "name of tag" :matcher *condition on value of tag (fn)*}




(filter-value-is use-students "bob")
(filter-values-contain use-students "tom")
(filter-by-index use-students 0)
(filter-by-index use-students 10)

(= (tag :s "s" (tag :q)) (tag :s "s" (tag :q)))

(def s-filter-is {:tag "div" :is "name1"})
(def s-filter-id {:tag "div" :id 1})

(= (first (second s-filter-id)) :id) 
(first (second match-all-q))

(process-filters use-students {:tag "div" :is "name1"})
(process-filters use-students {:tag "div" :id 1})



(find-query-abs use-list-sample (list match-div-q {:tag "br"}))
(find-query-abs use-list-sample (list {:tag "students"} {:tag "student"}))

(find-query-abs use-list-sample 
                (list {:tag "students"} {:tag "student" :id 1}))
(find-query-abs use-list-sample (list {:tag "*" :id 2}))
(find-query-abs use-list-sample (list {:tag "*"} {:tag "*" :id 0}))

(find-query-abs use-list-sample (list {:tag "br"}))



(find-query-rel use-list-sample (list {:tag "student" :id 1}))
(find-query-rel use-list-sample (list {:tag "div"}))
(find-query-rel use-list-sample (list {:tag "br"}))


(find-all-query use-list-sample {:tag "student" :rel true})
(find-all-query use-list-sample {:tag "student"})

(find-all-query use-list-sample (list {:tag "*"} {:tag "*"}))
(find-all-query use-list-sample (list {:tag "*" :rel true} {:tag "*" :id 0}))
(find-all-query use-list-sample (list {:tag "*"} {:tag "*" :id 0}))


(list-tag-args use-list-sample)

(str/includes? "asd" "d")


(validate-by-schema (tag :root
                         (tag :tank
                              (tag :t34 
                                   (tag :crew))
                              (tag :abrams))
                         (tag :plane))
                    use-schema)


(get-name-and-content "div[=text]")
(get-name-and-content "div")

(get-q-params "div[=text]")
(get-q-params "div[%text]")
(get-q-params "div[1]")
(get-q-params "div")


(transform-into-dict "~div[=text]")
(transform-into-dict "div[=text]")
(transform-into-dict "~div")
(transform-into-dict "div")


(find-all use-list-sample "students/*")
(find-all use-list-sample "~student")
(find-all use-list-sample "~student[=name1]")
(find-all use-list-sample "~student[%bob]")
(find-all use-list-sample "*/*[0]")
(find-all use-list-sample "div")
(find-all use-list-sample "div[2]")
(find-all use-list-sample "br")
(find-all use-list-sample "ad")

(list-tag-args-agnostic (find-all use-list-sample "~student"))

(find-one use-list-sample "br")
(find-one use-list-sample "hr")


(tag-for? (tag-for "div"))


(repeat-str " x " 1 )
(str/join " " (repeat 2 " x "))

(println (to-string
          (tag :br 
               "t34" 
               (tag :div
                    (tag :br
                         (tag :div))
                    "AA"
                    (tag :br)) 
               "abrams" 
               (tag :div "1"))))

(println (to-string use-list-sample))

(find-all (tag :br
               "t34"
               (tag :div
                    (tag :br
                         (tag :div))
                    "AA"
                    (tag :br))
               "abrams"
               (tag :div "1")) "~div")

(println (to-string (list
            (tag :br)
            (tag :div "a"))))

(get-options (tag-for "div" {:val "dsad"}))
(get-options (tag-for "div" {}))
(get-options (tag-for "div"))

(process-selector (tag-for "*" {:values false}) use-list-sample)
(process-selector (tag-for "student" {:values true}) use-list-sample)

(def use-tr-schema 
  (tag-for "*/*"))

(println (transform use-tr-schema use-list-sample))

(println (transform 
          (tag :root
               (tag :scholar
                    (tag-for "~student" {:values true}))
               (tag :div
                    (tag-for "~br"))) 
          use-list-sample))

(println (transform
          (tag :root
               (tag :scholar
                    (tag-for "~student/div"))
               (tag :div
                    (tag-for "~br")))
          use-list-sample))

(println (transform
          (tag :root
               (tag :scholar
                    (tag-for "~student" {:values true})))
          use-list-sample))

(println (transform
          (tag :root
               (tag :scholar
                    (tag-for "~student")))
          use-list-sample))





