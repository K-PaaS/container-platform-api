apiVersion: v1
kind: ResourceQuota
metadata:
  name: cp-resource-quota
spec:
  hard:
    limits.cpu: ${resource_quota_cpu}
    limits.memory: ${resource_quota_memory}
    requests.storage: ${resource_quota_disk}