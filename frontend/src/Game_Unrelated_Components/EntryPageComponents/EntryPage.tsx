import React, {useState} from 'react'
import {useNavigate} from "react-router-dom";
import {useDispatch} from 'react-redux';
import './EntryPage.css'



const EntryPage = () => {


  let serverURL = "http://localhost:8000"
  const [userName, setUserName] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [message, setMessage] = useState<string>("");

  const dispatch = useDispatch();
  let navigate = useNavigate();

  const registerUser = () => {
    fetch(`${serverURL}/Users/registerUser/${userName}/${password}`)
    .then((res) => res.json())
    .then((isRegistered: boolean) => {
      if(isRegistered) {
        setMessage("Uzytkownik zarejestrowany");
      } else {
        setMessage("Uzytkownik o tej nazwie juz istnieje");
      }
    })
    .catch(console.error);

  }

  const registerAdmin = () => {
    fetch(`${serverURL}/Users/registerAdmin/${userName}/${password}`)
    .then((res) => res.json())
    .then((isRegistered: boolean) => {
      if(isRegistered) {
        setMessage("Admin zarejestrowany");
      } else {
        setMessage("Admin o tej nazwie juz istnieje");
      }
    })
    .catch(console.error);
  }

  const loginAdmin = () => {
    fetch(`${serverURL}/Users/loginAdmin/${userName}/${password}`)
    .then((res) => res.json())
    .then((isLogged: boolean) => {
      if(isLogged) {
        dispatch({type:"SET_USERNAME", payload: userName});
        dispatch({type: "SET_SERVER_URL", payload: serverURL});
        navigate("/Admin");
      } else {
        setMessage("Niepoprawne dane");
      }
    })
    .catch(console.error);
  }

  const loginUser = () => {
    fetch(`${serverURL}/Users/loginUser/${userName}/${password}`)
    .then((res) => res.json())
    .then((isLogged: boolean) => {
      if(isLogged) {
        connectToServer();
      } else {
        setMessage("Niepoprawne dane");
      }
    })
    .catch(console.error);
  }

  const connectToServer = () => {
    dispatch({type:"SET_USERNAME", payload: userName});
    dispatch({type: "SET_SERVER_URL", payload: serverURL});
    navigate("/Main");
  }  

  return (
    <div className="EntryPageBody">
      <div>
        <h1>
          Welcome to my card game!! <br />
        </h1>
        <h6>To test duel you have to first build your deck. When deck is read click find enemy two times and you will start game with yourself.</h6>
        <h6>Sometimes you have to refresh page and click load data if there are any bugs to load most recent updates</h6>
        <h6>I work on this project almost every day so if something doesn't work that means i am trying to fix that</h6>
        {/* <h6>(I am so sorry but i have problems with server and i am fixing this right now)</h6> */}

      </div>
        <div className="inputBox">

          <div>
            <input id='user-name'  type="text" required placeholder="username"
            value={userName} name='username' 
            onChange={(event:any) => {setUserName(event.target.value)}}/>
          </div>

          <div>
            <input id='password'  type="text" required placeholder="password"
            value={password} name='password' 
            onChange={(event:any) => {setPassword(event.target.value)}}/>
          </div>
        </div>

         <button className="connectBtn" onClick={loginUser}>login as user</button>
         <button className="connectBtn" onClick={loginAdmin}>login as admin</button>
         <button className="connectBtn" onClick={registerUser}>register as user</button>
         <button className="connectBtn" onClick={registerAdmin}>register as admin</button>
         <div>
          {message}
         </div>
    </div>
    
  )
}

export default EntryPage