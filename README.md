# Webapp with Quarkus backend and React.js frontend

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
$ java -jar backend/target/quarkus-app/quarkus-run.jar
```

Open <http://localhost:8080/> to see the app.

## References

* <https://docs.npmjs.com/cli/v7/using-npm/config#prefix>