- Create a model/db schema for each rule
    Rule{
        title: String,
        description: String,
        <!-- url: String, -->
        createdAt: Date,
        updatedAt: Date,
        embedding: placeholder array for now
    }
- create a dao layer for all CRUD operations
 - 

```bash
docker run -d --name neo4j -p 7474:7474 -p 7687:7687 -e NEO4J_AUTH=neo4j/neo4j123 neo4j
```