import './App.css';
import {useEffect, useState} from "react";

function App() {
  const [greeting, setGreeting] = useState("-");

  useEffect(() => {
    fetch('http://localhost:8080/hello')
        .then((response) => response.text())
        .then((greeting) => {
          console.log(greeting);
          setGreeting(greeting);
        })
        .catch((err) => {
          console.log(err.message);
        });
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <p>
          {greeting}
        </p>
      </header>
    </div>
  );
}

export default App;
