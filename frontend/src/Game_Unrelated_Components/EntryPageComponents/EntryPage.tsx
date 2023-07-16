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
    dispatch({type: "SET_SERVER_URL", payload: 'http://localhost:8000'});
    navigate("/Main");
  }



  return (
    <div className="EntryPageBody">
        Welcome to my card game! <br />
        <div>
         <input id='user-name' placeholder='enter user name' value={userName} name='username' onChange={(event:any) => {setUserName(event.target.value)}}/>
         <button onClick={connectToServer}>connect to server</button>
        </div>
    </div>
    
  )
}

export default EntryPage