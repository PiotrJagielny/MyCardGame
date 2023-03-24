import React from 'react'
import {useNavigate} from "react-router-dom";

const MainPage = () => {

  let navigate = useNavigate();
  const RedirectToDeckBuilder = () =>{
      navigate("/DeckBuilder");
  }
  const RedirectToDuel = () =>{
    navigate("/Duel");
  }

  return (
    <div>
      This is game main page <br />
      <button onClick={RedirectToDeckBuilder}>Build your deck</button> <br />
      <button onClick={RedirectToDuel}>Duel</button> <br />
    </div>
    
  )
}

export default MainPage