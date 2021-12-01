# superheroes-api

ABM about Superheroes.

Superheroes are not created by api, but yes by the database (flyway).

## run

    docker build -t superheroes-app .

    docker run -p 8090:8080 superheroes-app

The api will be available on port 8090.

Example:

    http://localhost:8090/ping

Response:
    
    pong

## Api documentation

After executing the above instructions, you can access the documentation:

    http://localhost:8090/swagger-ui/#/

## Available tokens

* As Admin (permissions: VIEW, CREATE, UPDATE, DELETE):


    Token: TEST_ADMIN_ROLE
  

* As View (permission: VIEW): 


    Token: TEST_VIEW_ROLE


## Improvements to be made

1. Improve the authentication part.
2. Improve security on the IDs that are used in the URLs.
3. Improve dockerfile.
4. Improve test coverage.

## Requests

* Get all superheroes:


    curl --location --request GET 'localhost:8090/superheroes?size=5&page=0' \
    --header 'Token: TEST_VIEW_ROLE'



    curl --location --request GET 'localhost:8090/superheroes?size=5&page=1' \
    --header 'Token: TEST_VIEW_ROLE'


* Get superheroes with the filter for the name:


    curl --location --request GET 'localhost:8090/superheroes?size=5&name=1&page=0' \
    --header 'Token: TEST_VIEW_ROLE'


* Get superheroes by ID:


    curl --location --request GET 'localhost:8090/superheroes/2' \
    --header 'Token: TEST_VIEW_ROLE'

* Put a superhero:


    curl --location --request PUT 'localhost:8090/superheroes/2' \
    --header 'Token: TEST_ADMIN_ROLE' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "name": "new name for superhero 2"   
    }'



* Delete a superhero:


    curl --location --request DELETE 'localhost:8090/superheroes/11' \
    --header 'Token: TEST_ADMIN_ROLE'