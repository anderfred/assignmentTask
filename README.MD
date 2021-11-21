Hello everyone!
This is the test task assignment.

REST CRUD services has been implemented for Category,Product,OrderItem,Order entities.
I'm also write some integration test for rest endpoints, in memory h2 database is created for every test, not sure to what extent this is appropriate...
I've heard that - for integration tests can be utilized temporary docker containers, unfortunately there is lack of free time for such experiments.
Database is PostgreSQL, on first application startup tables are created by Liquibase in 4 change sets.
Report controller with single endpoint is available on report/ uri, which returns JSONArray with income(total) for each day ,in case of multiple orders on single day income will sum.
In "Order" endpoint implemented search method witch is finding all orders by part of product name using elasticSearch engine, btw methods save and delete are saving and deleting data of orders to elastic engine.
All endpoints and methods are annotated with openApi(swagger) annotations for generating definitions which are available on /v3/api-docs endpoint.

Docker compose file is available in folder /docker for easy deployment of environment.
For local testing please change ElasticSearchConfig.java clientConfiguration connection from es01:9200 to -> localhost:9200 or add to hosts redirection from es01 to localhost

For testing purposes application is deployed on DigitalOcean server and available by 188.166.163.166 address
188.166.163.166:8787 - web
188.166.163.166:5555 - postgres
188.166.163.166:9222 - elastic
