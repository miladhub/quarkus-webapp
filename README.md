# Webapp with Quarkus backend and React.js frontend

## Prerequisites

When in dev mode, this project will use basic authentication, so no prerequisites.

When run in production mode, this projects needs:
* A [Keycloak](https://www.keycloak.org) server must be installed listening on <http://KC_HOST:KC_PORT/>,
where `KC_HOST` and `KC_PORT` are the Keycloak listening host and port (if using `localhost`, choose a port
different from 8080)
* OpenID connect (OIDC) client must be configured on it - we'll use id `my-client-id` here
* The client credential will be assumed to be `my-secret`

## Project structure

The frontend has been created with <https://create-react-app.dev/docs/getting-started>:

```shell
$ npx create-react-app frontend
```

The backend is a Quarkus app serving the backend as static resources, see
<https://quarkus.io/guides/http-reference#serving-static-resources>.

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
$ ./build.sh
$ java \ 
  -Dquarkus.oidc.auth-server-url=http://KC_HOST:KC_PORT/realms/quarkus \
  -Dquarkus.oidc.client-id=my-client-id \
  -Dquarkus.oidc.credentials.secret=my-secret \
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