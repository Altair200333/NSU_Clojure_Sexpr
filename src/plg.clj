(ns plg)

;; Testing stuff

(defn test1 [x] (+ x 1))

(test1 2)

(defn array [& values]
  (cons ::array values))

(defn tag-args [tag-expr] 
  (first (rest (rest tag-expr))))

(defn tag-name [tag-expr]
  (first (rest tag-expr)))

(defn tag [name & children]
  {:doc "Create named tag with children"}
  (if children 
    (list ::tag name children)
    (list ::tag children)))

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

(tag-args (array "asda" "dsad"))
(tag-name (array "asda" "dsad"))

(string? (::tag "asd"))


(tag? (::tag "asd"))

(tag? (tag :div "ds"))
(tag? "sda")
(tag? (tag :div))

(tag-args (tag :div "asd"))
(second (tag-args (tag :div "1" "2" "3" "4")))
(tag-args (tag :div "asd" "dsa" (tag :br)))
(tag-name (tag :div "asd" "dsa" (tag :br)))


(defn match-name [expr name]
  {:doc "check if tag name is equal to @name"}
  (= (tag-name expr) name))
(match-name (tag :div "aa") :div)
(match-name (tag :div "aa") :br)
































































