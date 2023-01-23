<div align="center">
  <h1 align="center">
  Clojure S expressions
  </h1>
</div>

## About The Project
"S expressions" operations in Clojure

## Contents

- [Object structure](#structure)
- [X-path like language to query elemets](#search)
- [Transform object into html](#transform)
- [Schema validation](#schema)

## Structure

Use `(tag <name> [children])` command to create tag.

**name** - tag name: *string* or *keyword* i.e `(tag :div)` `(tag "div")`

**children** - tag sub-nodes. Every tag can have any number of child items. Children items can be strings or others tags.

example object:

``` clojure
(tag :students
    (tag :student "name1")
    (tag :student "name2") 
    (tag :div 
        (tag :br "br")))
```

## Search

Search syntax is inspired by xpath.
examples of search queries:

- `students/*` - find all elements under students tag
- `~student[=bob]` - relative path; find student named bob
- `*/*[0]` - find all nested tags and take first nested element
- `~student[%bob]` - find student whose child nodes contain "bob" string

Syntax: tags are separated by `/`. 

Optional selector applied to tag - []
`*` - match all tags
`~` - use relative search (not from root node)

- `[="text"]` - text value equals to ..
- `[%"text"]` - one of values equals text
- `[number]` - select by index

usage examples:

``` clojure
(find-all use-list-sample "students/*") ;; find children of students nodes
(find-all use-list-sample "~student[%bob]")
(find-all use-list-sample "*/*[0]")
(find-one use-list-sample "br")
```

`find-all` and `find-one` accept tag as well as list of tags (objects do not need to have root node)

## Transform

Syntax is inspired by xslt.

Transform objects into html with methods:

- `to-string` - turn tag or list of tags into html string without any queries
- `transform` - turn tag or list of tags object into html with **custom query selectors**

example:

``` clojure
(transform 
    (tag :root
        (tag :scholar
            (tag-for "~student" {:values true})) ;; find all student tags and take child elemetns
        (tag :div
            (tag-for "~br"))) ;; find all br elements
    use-list-sample)
```

Metod: `(tag-for <query> [options])`

- `tag-for` select items from object using **query** (second parameter)
- **options** - optional dictionary of parameters. `:values` option extracts contents each found tag (see example below)

more examples:

``` clojure
(transform
          (tag :root
               (tag :scholar
                    (tag-for "~student" {:values true}))) ;; pick child values
          use-list-sample)

;; output:
;; <root>
;;   <scholar>
;;     "name1"
;;     "name2"
;;     "bob"
;;   </scholar>
;; </root>

(transform
          (tag :root
               (tag :scholar
                    (tag-for "~student")))
          use-list-sample)

;; output:
;; <root>
;;   <scholar>
;;     <student>
;;       "name1"
;;     </student>
;;     <student>
;;       "name2"
;;     </student>
;;     <student>
;;       "bob"
;;     </student>
;;   </scholar>
;; </root>
```

## Schema

Schema is very similar to normal objects, it costrains tag names, order and child nodes.

Using `"*"` as child item allow tag to have any number of child nodes

``` clojure
(tag :root
    (tag :tank
        (tag :t34 "*")
        (tag :abrams))
    (tag :plane))
```

To validate object using schema run
`(validate-by-schema tag schema)`

example

``` clojure
(validate-by-schema 
    (tag :root ;; validated object 
        (tag :tank
            (tag :t34 
                (tag :crew))
            (tag :abrams))
        (tag :plane))

    (tag :root ;; schema object
       (tag :tank
            (tag :t34 "*") ;; allows this tag to have number of child nodes
            (tag :abrams))
       (tag :plane)))
```
