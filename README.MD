**Hello everyone!**

[This is the test task assignment](https://github.com/anderfred/assignmentTask).

REST CRUD services have been implemented for _Category_, _Product_, _OrderItem_, _Order_ _entities_.

I'm also writing some integration tests for rest endpoints, in memory h2 database is created for every test, not sure to what extent this is appropriate...
I've heard that - integration tests can be utilized in temporary docker containers, unfortunately, there is a lack of free time for such experiments.

The database is PostgreSQL, on the first application startup tables are created by _Liquibase_ in 4 changesets.

Report controller with a single endpoint is available on report/ URI, which returns JSONArray with income(total) for each day, in case of multiple orders on single day income will sum.

In the "Order" endpoint implemented search method witch is finding all orders by part of product name using the elastic search engine, btw methods save and delete are saving and deleting data of orders to the elastic engine.

All endpoints and methods are annotated with open API(swagger) annotations for generating definitions which are available on [/v3/api-docs](http://188.166.163.166:8787/v3/api-docs) endpoint.
Swagger ui available on [/swagger-ui.html](http://188.166.163.166:8787/swagger-ui.html)

Docker compose file is available in folder /docker for easy deployment of environment.
For local testing please change ElasticSearchConfig.java client configuration connection from es01:9200 to -> localhost:9200 or add to hosts redirection from es01 to localhost, also elastic search must be running on 9200 port

For testing purposes application is deployed on the DigitalOcean server and available at 188.166.163.166 address

[188.166.163.166:8787](http://188.166.163.166:8787) - web

[188.166.163.166:5555](http://188.166.163.166:5555) - Postgres

[188.166.163.166:9222](http://188.166.163.166:9222) - elastic
