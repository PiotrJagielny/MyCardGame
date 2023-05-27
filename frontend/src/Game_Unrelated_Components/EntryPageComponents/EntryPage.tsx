import React, {useState} from 'react'
import {useNavigate} from "react-router-dom";
import Modal from 'react-modal';

const EntryPage = () => {

  const [isModalOpen, setIsModalOpen] = useState(false);

  let navigate = useNavigate();
  const RedirectToMainPage = () =>{
      navigate("/Main");
  }

  const handleModalOpen = () => {
    setIsModalOpen(true);
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
  };


  return (
    <div>
      <button onClick={handleModalOpen}>Open Modal</button>
      <Modal isOpen={isModalOpen} onRequestClose={handleModalClose}>
        <p>This is some info</p>
        <button onClick={handleModalClose}></button>
      </Modal>
        Welcome to my card game! <br />
        <button onClick={RedirectToMainPage}>Press to enter</button>
    </div>
    
  )
}

export default EntryPage