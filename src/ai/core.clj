(ns ai.core
  (:gen-class))

;; Abhaya S Rawal
;; Timothy C Hoff

;; quick-bench; used for benchmarking
(use 'criterium.core)

;; clojure zipper library
(require '(clojure [zip :as z]))

;; (filter-node [2 2 true]) => true
;; (filter-node [1 3 true]) => false
(defn filter-node
  "Validates is a given node is acceptable"
	[node]
	(let [[m c _] node
				[m1 c1] [(- 3 m) (- 3 c)]
				st (and (>= m 0) (>= c 0) (< m 4) (< c 4))]
		(if st
			(cond
				(or (= m 0) (= m1 0)) true
				:else (and (>= m c) (>= m1 c1)))
			false)))

;; (build-node [3 3 true])
;; => [[2 3 false] [3 2 false] [1 3 false] [3 1 false] [2 2 false]]
(defn build-node
  "Builds series of nodes based on operations"
	[node]
	(let [[m c s] node
				f (if s - +)]
		(conj []
			[(f m 1) c (not s)]
			[m (f c 1) (not s)]
			[(f m 2) c (not s)]
			[m (f c 2) (not s)]
			[(f m 1) (f c 1) (not s)])))

;; (find-node [3 3 true])
;; => ([3 2 false] [3 1 false] [2 2 false])
(defn find-node
	"Builds, filters, and returns all possible nodes for given argument"
	[node]
	(filter filter-node (build-node node)))

;; (permute 0 [3 2 1 0] true)
;; => ([0 3 true] [0 2 true] [0 1 true] [0 0 true])
(defn permute
	"Returns all possible permutations"
	[i l s]
	(map #(vector i % s) l))

;; (gen-state [3 2 1 0] true)
;; => ([3 3 true] [3 2 true] [3 1 true] [3 0 true] [2 3 true] ....
(defn gen-state
	"Generates all possible states based on permutations"
	[rng side]
	(reduce #(concat %1 (permute %2 rng side)) [] rng))

;; Generate all states and filter invalids
(let [rng (range 3 -1 -1)]
	(def left-state (filter filter-node (gen-state rng true)))
	(def right-state (filter filter-node (gen-state rng false))))

;; (build-tree)
;; => ["33true" ["32false"] ["31false" ["32true" ["22false"] ["30false" ["31true" ....
(defn build-tree
	"Builds the entire tree sequence e.g. (1 (2 (3) (4)) (5 (6)))"
	([] (build-tree [3 3 true] #{(clojure.string/join "" [3 3 true])}))
	([node acc]
		(let [nodes (find-node node)
					filtered (filter #(not (contains? acc (clojure.string/join ""%))) nodes)]
			(apply conj [(clojure.string/join "" node)]
				(if-not (empty? filtered)
					(map #(build-tree % (conj acc (clojure.string/join "" %))) filtered))))))

;; (bfs-flatten (build-tree))
;; => ["33true" "32false" "31false" "22false" "32true" "32true" "22false" "30false" "31false" ....
(defn bfs-flatten
	"Flattens a bfs tree sequence. Note: only used for debugging"
	[nodes]
	(loop [acc []
				 queue (conj clojure.lang.PersistentQueue/EMPTY nodes)]
		(if-not (seq queue)
			acc
			(do
				(let [[h & t] (peek queue)]
					(recur (conj acc h) (into (pop queue) t)))))))

;; (walk (z/vector-zip (build-tree)))
;; => too long; not included
(defn walk
	"walks the vector tree and returns location of all nodes ending w/ '00false'"
	([loc] (walk loc []))
	([loc acc]
		(if (z/end? loc)
			acc
		  (do
		  	(recur (z/next loc) 
		  		(if (= "00false" (z/node loc))
		  			(conj acc loc)
		  			acc))))))

;; (-> (z/vector-zip (build-tree)) walk first z/path get-all-fs)
;; => ("33true" "31false" "32true" "30false" "31true" "11false" "22true" "02false" "03true" "01false" "11true" "00false")
(defn get-all-fs
	"Returns a list of the first items of a zipper path"
	[loc]
	(map #(first %) loc))

;; (find-path)
(defn find-path	
	"Creates the vector tree, walks it, and runs the result through (get-all-fs) to filter result"
	[]
	(-> (z/vector-zip (build-tree)) walk first z/path get-all-fs))

(defn queue
	"Builds a clojure persistent queue for pop and peek"
	[& vals]
  (apply merge (clojure.lang.PersistentQueue/EMPTY) vals))

;; (bfs-inner (build-tree) "00false")
;; ["33true" "31false" "32true" "30false" "31true" "11false" "22true" "02false" "03true" "01false" "11true"]
(defn bfs-inner 
	"Walks the entire tree and returns a vector path to given parameter"
	[tree end]
  (loop [expanding (queue {:node tree
                           :path []})]
    (if (empty? expanding)
      nil
      (let [{[val & childs] :node
             p :path}
            (peek expanding)
            curr-path (conj p val)]
        (if (= val end)
          p
          (recur
           (apply merge (pop expanding)
                  (map #(hash-map :node %
                                  :path curr-path)
                       childs))))))))

;; (find-path-due)
(defn find-path-due
	"Wrapper for (bfs-inner) function"
	[]
	(conj (bfs-inner (build-tree) "00false") "00false"))

;; ----- main -----
(defn -main
  "AI\nAssignment 1: Missionary & Cannibals"
  [& args]
  (println "\nAll possible state w/ boat on left side")
  (println left-state)
  (println "\nAll possible state w/ boat on right side")
  (println right-state)
  (println "\nList of states -> ")
  (println (find-path))
  (println "\nList of states (secondary) -> ")
  (println (find-path-due))
  (println))






