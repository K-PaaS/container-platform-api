apiVersion: v1
kind: LimitRange
metadata:
  name: ${name}
spec:
  limits:
  - default:
      cpu: ${limit_cpu}
    defaultRequest:
      cpu: ${request_cpu}
    type: Container
