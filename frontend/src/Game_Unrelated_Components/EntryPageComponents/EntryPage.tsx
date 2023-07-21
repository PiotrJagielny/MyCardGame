import React, {useState} from 'react'
import {useNavigate} from "react-router-dom";
import {useDispatch} from 'react-redux';
import './EntryPage.css'



const EntryPage = () => {


  const [userName, setUserName] = useState<string>("");

  const dispatch = useDispatch();
  let navigate = useNavigate();

  const connectToServer = () => {


    dispatch({type: "SET_USERNAME", payload: userName});
    // dispatch({type: "SET_SERVER_URL", payload: 'http://localhost:8000'});
    dispatch({type: "SET_SERVER_URL", payload: 'https://card-game-9dmt.onrender.com'});
    navigate("/Main");
  }



  return (
    <div className="EntryPageBody">
        <h1>
          Welcome to my card game! <br />
        </h1>
        <div className="inputBox">

          <input id='user-name'  type="text" required 
          value={userName} name='username' 
          onChange={(event:any) => {setUserName(event.target.value)}}/>

          <span>enter user name</span>
        </div>

         <button className="connectBtn" onClick={connectToServer}>connect to server</button>
    </div>
    
  )
}

export default EntryPage