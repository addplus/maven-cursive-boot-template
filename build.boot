; To inform IntelliJ explicitely about deftask, set-env!, task-options!
(require '[boot.core :refer :all]
  '[boot.task.built-in :refer :all])

(set-env! :source-paths #{"src"})
(require 'boot.maven)
(set-env! :dependencies (boot.maven/import))

(require
  '[adzerk.bootlaces :refer :all])

(def +version+ "1.1")
(bootlaces! +version+)

(task-options!
  pom {:project 'group-id/artifact-id
       :version +version+})

(deftask run 
  "Run the app"
  [a args ARG [str] "the arguments for the application."]
  (with-pre-wrap [fs]
    (require 'app.core)
    (apply (resolve 'app.core/-main) args)
    fs))

(deftask dev
  "Development mode"
  []
  (comp
    (repl :server true)
    (watch :verbose true)
    (run)
    (speak :theme "ordinance")))
