import React, {useState} from 'react';
import logo from './logo.svg';
import './App.css';
import InputField from './Components/InputField'


function App() {

  const [todo, setTodo] = useState<string>("");

  return (
    <div className="App">
      Hello world
      <InputField todo={todo} setTodo={setTodo}/>

    </div>
  );
};

export default App;
