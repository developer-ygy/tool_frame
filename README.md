
## ES 版本：7.10.2
## 创建 ES 索引 hotel
```shell script
curl -H "Content-Type: application/json" -XPUT http://127.0.0.1:9200/hotel -d {\"mappings\":{\"properties\":{\"title\":{\"type\":\"text\"},\"city\":{\"type\":\"keyword\"},\"price\":{\"type\":\"double\"},\"createTime\":{\"type\":\"date\"}}}}
```

