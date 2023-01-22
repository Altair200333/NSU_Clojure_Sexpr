(ns src.plg)
(require '[clojure.string :as str] 
         '[clojure.edn :as edn]
         '[src.lang :refer :all]
         '[src.utils :refer :all]
         '[src.samples :refer :all])



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

(def s-filter-is {:tag "div" :is "name1"})
(def s-filter-id {:tag "div" :id 1})

(= (first (second s-filter-id)) :id) 
(first (second match-all-q))

(process-filters use-students {:tag "div" :is "name1"})
(process-filters use-students {:tag "div" :id 5})



(find-query-abs use-list-sample (list match-div-q {:tag "br"}))
(find-query-abs use-list-sample (list {:tag "students"} {:tag "student"}))

(find-query-abs use-list-sample 
                (list {:tag "students"} {:tag "student" :id 1}))
(find-query-abs use-list-sample (list {:tag "*" :id 2}))
(find-query-abs use-list-sample (list {:tag "*"} {:tag "*" :id 0}))




(find-query-rel use-list-sample (list {:tag "student" :id 1}))
(find-all-query use-list-sample {:tag "student" :rel true})
(find-all-query use-list-sample {:tag "student"})

(find-all-query use-list-sample (list {:tag "*"} {:tag "*"}))
(find-all-query use-list-sample (list {:tag "*" :rel true} {:tag "*" :id 0}))
(find-all-query use-list-sample (list {:tag "*"} {:tag "*" :id 0}))


(list-tag-args use-list-sample)

(str/includes? "asd" "d")

(def use-schema
  (tag :root
       (tag :tank
            (tag :t34 "*")
            (tag :abrams))
       (tag :plane)))

(defn validate-tag-name [tag node]
  (= (tag-name tag) (tag-name node)))

(validate-tag-name (tag :s) (tag "s"))

(defn schema-validation-impl [tags schema]
  (let [first-tag (first tags) first-node (first schema)]
    (if (and (empty? tags) (empty? schema))
      true 
      (and
       (and 
        (validate-tag-name first-tag first-node)
        (if (value-is first-node "*")
          true
          (schema-validation-impl
           (list-tag-args (list first-tag))
           (list-tag-args (list first-node)))))
       (schema-validation-impl (rest tags) (rest schema))))))

(defn validate-by-schema [tags schema]
  (let [tag-list (turn-into-list tags) schema-list (turn-into-list schema)]
    (schema-validation-impl tag-list schema-list)))

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
(find-all use-list-sample "~student[%name1]")
(find-all use-list-sample "*/*[0]")
(find-all use-list-sample "div")




































