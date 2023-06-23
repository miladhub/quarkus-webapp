# Webapp with Quarkus backend and React.js frontend

## Prerequisites

* A [Keycloak](https://www.keycloak.org) server must be installed listening on <http://localhost:8180/>
* OpenID connect client must be configured on it - we'll use id `my-client-id` here
* The client credential will be assumed to be `my-secret`

## Project structure

The frontend has been created with <https://create-react-app.dev/docs/getting-started>:

```shell
$ npx create-react-app frontend
```

The backend is a Quarkus app serving the backend as static resources, see
<https://quarkus.io/guides/http-reference#serving-static-resources>.

## Build & run

```shell
$ ./build.sh
$ java \ 
  -Dquarkus.oidc.auth-server-url=http://localhost:8180/realms/quarkus
  -Dquarkus.oidc.client-id=my-client-id
  -Dquarkus.oidc.credentials.secret=my-secret \
  -jar backend/target/quarkus-app/quarkus-run.jar
```

Open <http://localhost:8080/> to see the app.

# Dev mode

Backend:

```shell
$ cd backend
$ mvn quarkus:dev \
  -Dquarkus.oidc.auth-server-url=http://localhost:8180/realms/quarkus
  -Dquarkus.oidc.client-id=my-client-id
  -Dquarkus.oidc.credentials.secret=my-secret
```

Frontend:

```shell
$ cd frontend
$ npm start
```

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
* <https://quarkus.io/guides/all-config#quarkus-vertx-http_quarkus.http.auth.permission.-permissions-.policy>