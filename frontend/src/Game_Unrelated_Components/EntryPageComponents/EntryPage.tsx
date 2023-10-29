import React, {useState} from 'react'
import {useNavigate} from "react-router-dom";
import {useDispatch} from 'react-redux';
import './EntryPage.css'



const EntryPage = () => {


  const [userName, setUserName] = useState<string>("");

  const dispatch = useDispatch();
  let navigate = useNavigate();

  const connectToServer = () => {


    dispatch({type:"SET_USERNAME", payload: userName});
    dispatch({type: "SET_SERVER_URL", payload: 'http://localhost:8000'});
    // dispatch({type: "SET_SERVER_URL", payload: 'https://fsv0gtenbe.execute-api.eu-north-1.amazonaws.com'});
    
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