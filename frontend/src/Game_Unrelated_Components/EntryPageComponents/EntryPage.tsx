import React, {useState} from 'react'
import {useNavigate} from "react-router-dom";


const EntryPage = () => {



  let navigate = useNavigate();
  const RedirectToMainPage = () =>{
      navigate("/Main");
  }



  return (
    <div>
        Welcome to my card game! <br />
        <button onClick={RedirectToMainPage}>Press to enter</button>
    </div>
    
  )
}

export default EntryPage