(ns wcc
  	(:gen-class)
	(:use [clojure.contrib command-line 
			[duck-streams :only [read-lines writer]]
			[str-utils2 :only [split]]]))

(defn count-lines [file] 
	(count (read-lines file)))

(defn count-words [file]
	(count (filter not-empty (split (slurp file) #"\s"))))

(defn count-chars [file] 
	(count (slurp file)))

(defn print-count [file count-function]
	(println (format "Counted %s: %s" file (count-function file))))
	
(defn print-invalid-option [] 
	(println "Must specify one of -l, -w, or -c"))
	
(defn -main [& args]
	(with-command-line
	  args 
	  "Usage: wcc [-l|-w|-c] [-o out.txt] <file>..."
	  [[lines? l? "Count lines" true]
	   [words? w? "Count words" false]
	   [chars? c? "Count chars" false]
	   [out "The output file"]
	   files]
		
		(binding [*out* (if out (writer out) *out*)]
			(doall (map #(cond 
					lines? (print-count % count-lines)
					words? (print-count % count-words)
					chars? (print-count % count-chars)
					:else (print-invalid-option)) 
				files)))))
