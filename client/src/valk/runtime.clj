(ns valk.runtime)

(defmacro defs [name args & rest]
  `(defn ~name [callback# ~@args]
     (valk.runtimejs/send-request
      ~(format "server-functions/%s" name)
      {:body (hash-map ~@(flatten (map #(do [(keyword %) %]) args)))}
      callback#)))

(defmacro startd [name interval & rest])
