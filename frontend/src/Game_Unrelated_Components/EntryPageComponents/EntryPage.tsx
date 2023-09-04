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
    // dispatch({type: "SET_SERVER_URL", payload: 'http://localhost:8000'});
<<<<<<< HEAD
    dispatch({type: "SET_SERVER_URL", payload: 'https://card-game-9dmt.onrender.com'});
=======
    dispatch({type: "SET_SERVER_URL", payload: 'https://13.48.247.47'}); //aws server
    // dispatch({type: "SET_SERVER_URL", payload: 'https://card-game-9dmt.onrender.com'}); //render servet
>>>>>>> features
    navigate("/Main");
  }  

  return (
    <div className="EntryPageBody">
      <div>
        <h1>
          Welcome to my card game!! <br />
        </h1>
<<<<<<< HEAD
        <h6>You can build deck, as well as play duel with yourself. If you click find enemy two times you will start game with yourself.
          I made it so that you can test playing duel because there arent many people that play my game. As this is hosted for free, all api calls are very slow,
          and sometimes don't work because of that, but i did it to simulate production and development enviroment and for myself. To enter deck 
          builder there is some waiting. To play game with yourself at duel page, you have to sometime do refreshing and loading data.
        </h6>
=======
        <h6>To test duel you have to first build your deck. When deck is read click find enemy two times and you will start game with yourself.</h6>
        <h6>Sometimes you have to refresh page or clock load data to load most recent updates</h6>
>>>>>>> features

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