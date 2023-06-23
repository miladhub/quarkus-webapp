import {useEffect, useState} from "react";
import {useParams} from 'react-router-dom';

export default function Contact() {
    let {contactId} = useParams();
    const [name, setName] = useState("-");

    useEffect(() => {
        fetch('name')
            .then((response) => response.text())
            .then(setName)
            .catch((err) => {
                console.log(err.message);
            });
    }, []);

    return (
        <div>
            <header>
                <p>
                    You are {name} - #{contactId}.
                </p>
            </header>
        </div>
    );
}
