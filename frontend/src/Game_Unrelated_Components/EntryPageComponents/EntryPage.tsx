import React, {useState} from 'react'
import {useNavigate} from "react-router-dom";
import {useDispatch} from 'react-redux';
import './EntryPage.css'



const EntryPage = () => {


  const [userName, setUserName] = useState<string>("");

  const dispatch = useDispatch();
  let navigate = useNavigate();

  const connectToServer = () => {



    // dispatch({type: "SET_SERVER_URL", payload: 'http://localhost:8000'});
    dispatch({type: "SET_SERVER_URL", payload: 'https://card-game-9dmt.onrender.com'});
    navigate("/Main");
  }



  return (
    <div className="EntryPageBody">
      <div>
        <h1>
          Welcome to my card game! <br />
        </h1>
        <h6>You can build deck, as well as play duel with yourself. If you click find enemy two times you will start game with yourself.
          I made it so that you can test playing duel because there arent many people that play my game. As this is hosted for free, all api calls are very slow. To enter deck 
          builder there is some waiting, you have to click button a few times. To play game with yourself at duel page, you have to sometime do refreshing and loading data.
        </h6>

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