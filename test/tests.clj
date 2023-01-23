(ns test.tests)
(require '[clojure.string :as str]
         '[clojure.edn :as edn]
         '[src.lang :refer :all]
         '[src.utils :refer :all]
         '[src.samples :refer :all]
         '[src.schema :refer :all]
         '[src.spath :refer :all]
         '[clojure.test :refer :all])

(deftest test-lang
  (testing "lang-types-tests"
    (is (= (element-type (tag :div)) :src.lang/tag))
    (is (= (element-type "str") :src.lang/string))
    )
  
  (testing "tag-types"
    (is (= (tag? (tag :div "data")) true))
    (is (= (tag? "totally not a tag") false))
    (is (= (first (tag-args (tag :div "asd"))) "asd"))
    (is (= (first (tag-args (tag :div ""))) ""))
    (is (= (tag-name (tag :div (tag :br))) :div))
    (is (not (= (tag-name (tag :br (tag :br))) :div)))
    )
  
  (testing "value-is"
    (is (value-is (tag :div "x") "x"))
    (is (not (value-is (tag :div "x") "d")))
    (is (not (value-is (tag :div "x" "d") "x")))
  )
  
  (testing "value-contains"
    (is (values-contain (tag :div "x" "d") "x"))
    (is (values-contain (tag :div "x" "d") "d"))
    (is (not (values-contain (tag :div) "d")))
    )
  
  (testing "index-filter"
    (is (= (filter-by-index use-students 0) (list (tag :student "name1"))))
    (is (= (filter-by-index use-students 10) (list)))
    (is (= (filter-values-contain use-students "tom") (list (tag :student "bob" "tom"))))
    )
  )

(run-tests `test.tests)