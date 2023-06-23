import './App.css';
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";

function App() {
    const [greeting, setGreeting] = useState("-");

    useEffect(() => {
        fetch('hello')
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
            <ul>
                <li>
                    <Link to={`contacts/1`}>Your Name</Link>
                </li>
                <li>
                    <Link to={`contacts/2`}>Your Friend</Link>
                </li>
            </ul>
        </div>
    );
}

export default App;
