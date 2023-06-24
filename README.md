# Webapp with Quarkus backend and React.js frontend

## Prerequisites

* Docker
* Java >= 17
* Node.js

## Project structure

The frontend has been created with <https://create-react-app.dev/docs/getting-started>:

```shell
$ npx create-react-app frontend
```

The backend is a Quarkus app serving the backend as static resources, see
<https://quarkus.io/guides/http-reference#serving-static-resources>.

## Installing Keycloak

This section takes inspiration from <https://quarkus.io/guides/security-oidc-code-flow-authentication-tutorial>:

```shell
docker run --name keycloak -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -p 8180:8080 quay.io/keycloak/keycloak:21.1.1 start-dev
```

Enter Keycloak at <http://localhost:8180/admin/> with credentials `admin` / `admin`, create a realm
from the top-left realms menu, and import [this file](backend/config/realm-export.json).

From the realm Users page, create a user, e.g.:
* Username: `alice`
* Email: `alice@fake.com`
* Email verified: `true`

From the user's Credentials tab, choose "Set Password":
* Password / Password confirmation: `alice`
* Temporary: `false`

## Setting up React

```shell
$ cd frontend/
$ npm install
```

## Build & run

### Dev mode

In dev mode,
* both the backend and the frontend support hot reload
* basic authentication is used, with a default user `admin` / `admin`

Backend:

```shell
$ cd backend
$ mvn quarkus:dev
```

Frontend:

```shell
$ cd frontend
$ npm start
```

Open <http://localhost:3000/> to see the app.

### Production mode

In production mode,
* the frontend build is bundled into the Quarkus backend, under `META-INF/resources`
* OIDC is used for authentication

```shell
$ npm --prefix frontend run build && \
  rm -rf backend/src/main/resources/META-INF/resources/ && \
  cp -r frontend/build/ backend/src/main/resources/META-INF/resources/ && \
  mvn clean install -f backend -DskipTests && \
  java \
  -Dquarkus.oidc.auth-server-url=http://localhost:8180/realms/frontend \
  -Dquarkus.oidc.client-id=frontend \
  -Dquarkus.oidc.credentials.secret=vpoqXFHXDBLN4qfVSTt7kODg4weRgZ2b \
  -jar backend/target/quarkus-app/quarkus-run.jar
```

Open <http://localhost:8080/> to see the app.

## References

* <https://docs.npmjs.com/cli/v7/using-npm/config#prefix>
* <https://quarkus.io/guides/config-reference>
* <https://quarkus.io/guides/http-reference#support-all-origins-in-devmode>
* <https://create-react-app.dev/docs/proxying-api-requests-in-development/>
* <https://create-react-app.dev/docs/deployment/#serving-the-same-build-from-different-paths>
* <https://stackoverflow.com/questions/76435306/babel-preset-react-app-is-importing-the-babel-plugin-proposal-private-propert>
* <https://reactrouter.com/en/main/start/tutorial>
* <https://quarkus.io/guides/security-oidc-code-flow-authentication-tutorial>
* <https://github.com/quarkusio/quarkus-quickstarts/tree/main/security-openid-connect-web-authentication-quickstart>
* <https://quarkus.io/guides/config-reference#system-properties>
* <https://quarkus.io/guides/security-oidc-code-flow-authentication-concept>
* <https://quarkus.io/guides/security-oidc-bearer-token-authentication-tutorial>
* <https://quarkus.io/guides/security-oidc-bearer-authentication-concept>
* <https://quarkus.io/guides/all-config#quarkus-vertx-http_quarkus.http.auth.permission.-permissions-.policy>
* <https://quarkus.io/guides/security-oidc-configuration-properties-reference>
* <https://quarkus.io/guides/security-properties>
* <https://quarkus.io/guides/security-overview-concept>
* <https://quarkus.io/guides/security-basic-authentication-tutorial>
* <https://quarkus.io/guides/security-basic-authentication-howto>
* <https://quarkus.io/guides/security-basic-authentication-concept>