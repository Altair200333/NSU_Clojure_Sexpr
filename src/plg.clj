(ns plg)
 (require '[clojure.string :as str])

;; Testing stuff

(defn test1 [x] (+ x 1))

(test1 2)

(defn array [& values]
  (cons ::array values))

(defn tag-args [tag-expr]
  {:doc "Return tag arguments"}
  (first (drop 2 tag-expr)))

(defn tag-name [tag-expr]
  {:doc "Return tag name"}
  (first (rest tag-expr)))

(defn tag [name & children]
  {:doc "Create named tag with children"}
  (if children 
    (list ::tag name children)
    (list ::tag name)))

(defn tag? [expr]
  {:doc "Check if expr is tag"}
  (= (key expr) ::tag))

(defn element-type [expr]
  {:doc "what is type of this element"}
  (if (tag? expr)
    ::tag
    (if (string? expr)
      ::sring
      ::rest)))

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


(defn name-is [expr name]
  {:doc "Check if tag name is equal to @name
         typeof name: [string, keyword]"}
  (if (keyword? name)
    (= (tag-name expr) name)
    (= (tag-name expr) (keyword name))))

(defn value-is [expr value] 
  {:doc "Check if (first) argument of expr is equal to value"}
  (let [values (tag-args expr)]
    (if (or (= values nil) (> (count values) 1))
      false
      (= (first values) value))))

(defn value-contains [expr value] 
  {:doc "Check if arguments of expr contain value"}
  (let [values (tag-args expr)]
    (some (fn [x] (= x value)) values)))

(name-is (tag :div "aa") :div)
(name-is (tag :div "aa") "div")
(name-is (tag :div "aa") :br)

(value-is (tag :div "x") "x")
(value-is (tag :div "x") "d")
(value-is (tag :div "x" "d") "x")

(value-contains (tag :div "x" "d") "x")
(value-contains (tag :div "x" "d") "x2")
(value-contains (tag :div "x" (tag :br)) (tag :div))

(def use-sample
  (tag :root
       (tag :div "empty")
       (tag :students
            (tag :student "name1")
            (tag :student "name2")
            (tag :student "name3")
            (tag :student "name4"))))

(first (tag-args (last (tag-args use-sample))))

(def use-list-sample
  (list 
   (tag :div "empty") 
   (tag :students 
        (tag :student "name4")
        (tag :student "name2"))
   (tag :div "element")
   (tag :div 
        (tag :br "br"))
   (tag :br)
   (tag :br "br here")))
(tag :br)

(tag-args use-sample)
(tag-args (tag :key))

(str/split "path/d/te" #"/")
(re-matches #"(a+)(b+)(\d+)" "abb234")

(def dict-data {:name "name" :tag value-is})
(dict-data :name)

;; list of tag predicates (...)
;; {:tag "name of tag" :matcher *condition on value of tag*}

(def match-all-q {:tag "*"})
(def match-div-q {:tag "div"})
(def match-br-q {:tag "br"})

(defn match-query [expr query]
  (let [name (query :tag)]
    (if (= name "*")
      true
      (name-is expr name))))

(match-query (tag :div "value") match-div-q)
(match-query (tag :div "value") match-br-q)
(match-query (tag :div "value") match-all-q)

(defn turn-into-list [expr]
  {:doc "Make sure expr is list so it's iterable"}
  (if (tag? expr)
    (list expr)
    (if (seq? expr)
      expr
      (list expr))))
(turn-into-list (list (tag :name "value") "12"))

(defn query-matching-expressions [expr query]
  (loop [list-exprs (turn-into-list expr) results []] 
    (if (empty? list-exprs) 
      results
      (let [first-expr (first list-exprs)]
        (if (match-query first-expr query)
          (recur (rest list-exprs) (conj results first-expr))
          (recur (rest list-exprs) results))))))

(query-matching-expressions use-list-sample match-div-q)
(query-matching-expressions use-list-sample match-br-q)
(query-matching-expressions use-list-sample match-all-q)
(query-matching-expressions use-list-sample {:tag "none"})

(defn list-tag-args [tags]
  (filter tag? (reduce 
    (fn [acc, x] 
      (concat acc (tag-args x))) 
    (list)
    tags)))

(seq? (list-tag-args use-list-sample))
(turn-into-list (list-tag-args use-list-sample))

(seq? (list-tag-args use-list-sample))
(seq? (list 1 2 3))
(query-matching-expressions (list-tag-args use-list-sample) {:tag "div"})

(defn find-query-abs-impl [expr queries]
  (loop [tags expr q queries results []] 
    (if (empty? tags) 
      results ;; looked over all tags - nothing to look for
      (let [matches (query-matching-expressions tags (first q))]
        (if (<= (count q) 1)
          (concat results matches)
          (recur (list-tag-args matches) (rest q) results))))))

(defn find-query-abs [expr query]
  (let [list-exprs (turn-into-list expr) queries (turn-into-list query)]
    (find-query-abs-impl list-exprs queries)))

(find-query-abs use-list-sample match-div-q)
(find-query-abs use-list-sample (list {:tag "students"} {:tag "student"}))











































