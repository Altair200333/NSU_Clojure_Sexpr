(ns src.plg)
(require '[clojure.string :as str] 
         '[clojure.edn :as edn]
         '[src.lang :refer :all]
         '[src.utils :refer :all]
         '[src.samples :refer :all]
         '[src.schema :refer :all])



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
(find-all use-list-sample "~student[%name1]")
(find-all use-list-sample "*/*[0]")
(find-all use-list-sample "div")
(find-all use-list-sample "br")

(find-one use-list-sample "br")
(find-one use-list-sample "hr")

(defn tag-for [query & options]
  {:doc "Create named tag with children"}
  (if options
    (list ::tag-for query options)
    (list ::tag-for query)))

(defn tag-for? [expr]
  {:doc "Check if expr is tag"}
  (= (first expr) ::tag-for))

(defn tag-query [expr]
  (second expr))

(tag-for? (tag-for "div"))

(def nes (list 1 (list 1 2 3)))

(defn strf [s]
  (if (seq? s)
    (str
     "<" (str/join " - " (map (fn [x] (strf x)) s)) ">")
    (str s)))

(strf nes)

(defn repeat-str [x n]
  (str/join "" (repeat n x)))

(defn pad [depth]
  (repeat-str "  " (max depth 0)))

(defn to-string-impl [val depth]
  {:doc "Convert given tags into html string"}
  (if (tag? val)
    (let [name (subs (str (tag-name val)) 1) values (tag-args val)]
      (if (empty? values)
        (str (pad depth) "<" name "/>")
        (str (pad depth) "<" name ">\n"
             (to-string-impl values (inc depth)) "\n"
             (pad depth) "</" name ">")))
    (if (seq? val)
      (str/join "\n" (map (fn [x] (to-string-impl x depth)) val))
      (str (pad depth) "\"" val "\""))))

(defn to-string [val]
  (to-string-impl val 0))

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

(defn get-options [val]
  (let [len (count val)]
    (if (= len 3) 
       (let [opts (first (last val))] 
         (if opts
           opts
           {}))
       {})))

(get-options (tag-for "div" {:val "dsad"}))
(get-options (tag-for "div" {}))
(get-options (tag-for "div"))

(defn process-selector [schema tags]
  (let [values ((get-options schema) :values)]
    (if values
      (list-tag-args tags)
      tags)))

(process-selector (tag-for "*" {:values false}) use-list-sample)
(process-selector (tag-for "*" {:values true}) use-list-sample)

(defn transform-impl [schema val depth]
  (if (tag? schema)
    (let [name (subs (str (tag-name schema)) 1) values (tag-args schema)]
      (if (empty? values)
        (str (pad depth) "<" name "/>")
        (str (pad depth) "<" name ">\n"
             (transform-impl values val (inc depth)) "\n"
             (pad depth) "</" name ">"))) 
    (if (tag-for? schema)
      (transform-impl 
       (process-selector schema (find-all val (tag-query schema))) 
       val depth)
      (if (seq? schema)
        (str/join "\n" (map (fn [x] (transform-impl x val depth)) schema))
        (str (pad depth) "\"" schema "\"")))))

(def use-tr-schema 
  (tag-for "*/*"))

(println (transform-impl use-tr-schema use-list-sample 0))

(println (transform-impl 
          (tag :root
               (tag :scholar
                    (tag-for "~students" {:values true}))
               (tag :div
                    (tag-for "~br"))) 
          use-list-sample 0))













