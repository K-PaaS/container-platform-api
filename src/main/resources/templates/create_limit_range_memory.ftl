apiVersion: v1
kind: LimitRange
metadata:
  name: ${name}
spec:
  limits:
  - default:
      memory: ${limit_memory}
    defaultRequest:
      memory: ${request_memory}
    type: Container
