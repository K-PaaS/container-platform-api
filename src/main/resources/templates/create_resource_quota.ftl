apiVersion: v1
kind: ResourceQuota
metadata:
  name: ${name}
spec:
  hard:
    requests.cpu: ${request_cpu}
    requests.memory: ${request_memory}
    limits.cpu: ${limits_cpu}
    limits.memory: ${limits_memory}