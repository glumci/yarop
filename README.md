# YAROP

Yet Another Railway Oriented Programming library for Clojure.

## Usage

**YAROP** assumes that results of all operations are represented
as pairs (2-vectors) in which the first element represents the 
**value** of the operation and the second element is the **error**.
Both value and error may be of arbitrary types.

Results can be created in several ways. Although a result can be
created directly, several utility factory functions are provided
for improved readability. The following equalities are all `true`,
for every value `v` and error `e`:

```clojure
(= [v e] (result v e))
(= [v nil] (result v nil) (value->result v))
(= [nil e] (result nil e) (error->result e))
```

Considering that a result is just a 2-vector, once it is created 
the value and error can be constructed in the usual way, using 
standard functions: `first`, `second`, `nth`, etc. Again, to improve
readability, utility functions `value` and `error` are
provided. The following equalities all evaluate to `true`:

```clojure
(= (first res) (nth res 0) (res 0) (value res))
(= (second res) (nth res 1) (res 1) (error res))
```

Given a result, it is considered successful if its error is `nil`,
and failure otherwise, irrespective of the value. Two utility
functions are provided to check correctness of the result `success?`
and `failure?`. The following evaluate to `true`:

```clojure
(success? (result 2 nil))
(success? (result nil nil))
(failure? (result nil 2))
(failure? (result 2 2))
```

The most important operator defined in **YAROP** is the
**railway thread first** (`=>`) operator which is used to chain 
functions producing results (i.e. 2-vectors with first element 
representing the value and the second element representing the 
error). The operator is used in much the same way as the 
conventional thread first operator (`->`).

The following listing assumes that all functions from 
`closure.math` namespace are imported

```clojure
(defn my-reciprocal [x] 
  (if (not= x 0) 
    (value->result (/ 1 x))
    (error->result "division by zero")))

(defn my-sqrt [x]
  (if (>= x 0)
    (value->result (sqrt x))
    (error->result "sqrt of negative number")))

(=> 4 my-sqrt my-reciprocal) ; [0.5 nil]
(=> -4 my-sqrt my-reciprocal) ; [nil "sqrt of negative number"]
(=> 0 my-sqrt my-reciprocal) ; [nil "division by zero"]
```

The `=>` operator swallows exceptions. If any of the threaded
functions raises an exception during execution, the exception
will be caught and return as an erroneous result (a failure).

```clojure
(defn throwing-fcn [x] (throw (Exception. "random error")))

(=> "random value" throwing-fcn) ; [nil (Exception "random error")]
```

**Warning:** At present the railway thread first operator operates
correctly only on functions which accept a single argument. This
behavior will be amended in the future releases.

## License

Copyright © 2022 Milan R. Rapaić

This program and the accompanying materials are made available under the
terms of the MIT license which is available at
https://choosealicense.com/licenses/mit.
