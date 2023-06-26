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
from the top-left realms menu, and import [this JSON file](backend/config/realm-export.json).

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

Open <http://localhost:3000/> to see the app, using credentials `admin` / `admin`.

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

Open <http://localhost:8080/> to see the app, using credentials `alice` / `alice`.

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

# Reverse proxying via Apache / TLS

*Disclaimer* - Reverse proxying so far has these limitations / notes:
* The final app URL needs a final slash ("/"), otherwise it won't work
* I couldn't find a way to reverse proxy _and_ change the context path at the same time
* I had to add `ProxyPreserveHost` to the Apache configuration because otherwise the redirect_uri sent
by Kecloak pointed at the internal app address

Start Keycloak in [reverse proxy](https://www.keycloak.org/server/reverseproxy) mode:

```shell
$ docker run --name keycloak-proxy -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -p 8180:8080 quay.io/keycloak/keycloak:21.1.1 start-dev \
  --proxy edge --http-relative-path=/kc --hostname-strict=false --hostname-url=https://localhost/kc
```

Also remember to import [this JSON file](backend/config/realm-export.json) on top of that.

Clone <https://github.com/miladhub/apache-https> to a folder of your choice - e.g., `/path/to/apache-https/` -
and add the following lines to file `http-ssl.conf` right before the closing `</VirtualHost>`:

```shell
<Location /kc>
  ProxyPass http://host.docker.internal:8180/kc
  ProxyPassReverse http://host.docker.internal:8180/kc
  ProxyPreserveHost On
</Location>

<Location /app>
  ProxyPass http://host.docker.internal:8080/app
  ProxyPassReverse http://host.docker.internal:8080/app
  ProxyPreserveHost On
</Location>
```

Copy this file to the Apache container and restart it:

```shell
$ docker cp httpd-ssl.conf apache-https:/usr/local/apache2/conf/extra
$ docker restart apache-https
```

Create a TLS trust store to allow Quarkus to connect to Keycloak over HTTPS,
starting from the certificate used to configure the Apache server - here we are
choosing demo password `changeit`:

```shell
keytool -importcert -alias keycloak -keystore cacerts \
  -file /path/to/apache-https/localhost.crt
```

Then, add this to `application.properties` (I haven't been able to reverse proxy
the app and change its context path at the same time):

```properties
quarkus.http.root-path=app
```

Finally, re-package and run the app following guide <https://quarkus.io/guides/http-reference#reverse-proxy>:

```shell
$ mvn clean package -f backend
$ java \
  -Dquarkus.oidc.auth-server-url=https://localhost/kc/realms/frontend \
  -Dquarkus.oidc.client-id=frontend \
  -Dquarkus.oidc.credentials.secret=vpoqXFHXDBLN4qfVSTt7kODg4weRgZ2b \
  -Dquarkus.oidc.authentication.force-redirect-https-scheme=true \
  -Dquarkus.http.proxy.proxy-address-forwarding=true \
  -Dquarkus.http.proxy.allow-forwarded=false \
  -Dquarkus.http.proxy.enable-forwarded-host=true \
  -Dquarkus.http.proxy.forwarded-host-header=X-Forwarded-Host \
  -Djavax.net.ssl.trustStore=cacerts \
  -Djavax.net.ssl.trustStorePassword=changeit \
  -jar backend/target/quarkus-app/quarkus-run.jar
```

This will allow using <https://localhost/app/> (mind the trailing slash!) with
both Keycloak and the webapp behind a TLS-enabled reverse proxy.

See
* <https://blog.sebastian-daschner.com/entries/quarkus-ssl-url-behind-reverse-proxy>
* <https://quarkus.io/guides/all-config>
* <https://quarkus.io/guides/resteasy-reactive#accessing-request-parameters>
* <https://httpd.apache.org/docs/2.4/mod/mod_proxy.html#proxypreservehost>
* <https://www.keycloak.org/server/hostname>
