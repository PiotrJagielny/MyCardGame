import React from 'react'
import {useNavigate} from "react-router-dom";

const EntryPage = () => {

  let navigate = useNavigate();
  const RedirectToMainPage = () =>{
      navigate("/Main");
  }

  return (
    <div>
        Welcome to my card game!
        <button onClick={RedirectToMainPage}>Press to enter</button>
    </div>
    
  )
}

export default EntryPage