apiVersion: v1
kind: LimitRange
metadata:
  name: cp-limit-range
spec:
  limits:
  - default:
      cpu: ${limit_range_cpu}
      memory: ${limit_range_memory}
    type: Container
