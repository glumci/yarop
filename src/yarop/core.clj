(ns yarop.core
  "Core namespace of the YAROP library.")

(defn value->result
  "Returns a successful result with the given value `v`."
  [v]
  [v nil])

(defn error->result
  "Returns a result with the given error `e`.

  The value of the created result is set to `nil`."
  [e]
  [nil e])

(defn result
  "Constructs a result with the given value `v` and error `e`."
  [v e]
  [v e])

(defn value
  "Retrieves value from result."
  [res]
  (first res))

(defn error
  "Retrieves error from result."
  [res]
  (second res))

(defn success?
  "Checks to see if the result represents a success?

  A result is considered successful if the error is
  `nil`, regardless of the value."
  [res]
  (-> res error nil?))

(defn failure?
  "Checks to see if the result represents a failure?

   A result is considered failure if the error is
   not `nil`, regardless of the value."
  [res]
  (-> res success? not))

(defn apply-if-success
  "Given a function `f` (which returns result) and a result with
  `value` and `err`, applies the function to `value` if there is
  no error. Otherwise, passes the input result forward."
  [f [value err]]
  (if (nil? err)
    (f value)
    [value err]))

(defmacro =>
  "Railway thread first macro.

  Creates a success result from `value` and applies to each
  function conditionally, as long as the result is successful."
  [value & functions]
  (list 'try
        (cons '->> (cons [value nil] (for [f functions] (list 'apply-if-success f))))
        (list 'catch Exception 'e [nil 'e])))
