
import React, {useState} from 'react'
import {useSelector} from 'react-redux';
import './AdminPanel.css'
import Modal from 'react-modal';
import {User} from './User';
import StateData from './../reactRedux/reducer';
import {Card} from '../../Game_Related_Components/Interfaces/Card'


const AdminPanel = () => {

    const [isUserDeleteModalOpen, setUserDeleteModalOpen] = useState<boolean>(false);

    const [users, setUsers] = useState<User[]>([]);

    const serverURL= useSelector<StateData, string>((state) => state.serverURL);

    const fetchUsers = () => {
        fetch(`${serverURL}/Users/getUsers`)
        .then((res) => res.json())
        .then((usersResponse: User[]) => {
            setUsers(usersResponse);
            setUserDeleteModalOpen(true);
        })
        .catch(console.error);
    }

    const saveCards = () => {
        fetch(`${serverURL}/DeckBuilder/saveCards`)
        .catch(console.error);
    }

    const getAllCards = () => {
        fetch(`${serverURL}/DeckBuilder/getAllCards`)
        .then((res) => res.json())
        .then((usersResponse: Card[]) => {
          console.log(usersResponse)
        })
        .catch(console.error);
    }

    const deleteUser = (username: string) => {
        console.log("delelete " + username);
        fetch(`${serverURL}/Users/deleteUser/${username}`)
        .then(() => {
            setUserDeleteModalOpen(false);
        })
        .catch(console.error);
    }



  return (
    <div className="AdminPanelBody">
      <Modal isOpen={isUserDeleteModalOpen} style={{content: { background:'gray',},}}>
        <h2>Delte user</h2>
        {users.map((user, index) =>(
            <div>
                <button className="connectBtn" onClick= { () => {deleteUser(user.name)} }>id: {user.id} | name: {user.name}</button>
            </div>
        ))}
        <br/>
        <br/>
        <button className="connectBtn" onClick={() => {setUserDeleteModalOpen(false)}}>Close</button>
      </Modal>
      <div>
        <h1>
            Admin panel
        </h1>
      </div>
      <div>
        <button className="connectBtn" onClick={fetchUsers}>Delete user</button>
      </div>
      {/* <div>
        <button className="connectBtn" onClick={saveCards}>Save cards</button>
      </div> */}
      <div>
        <button className="connectBtn" onClick={getAllCards}>Get all cards</button>
      </div>
    </div>
    
  )
}

export default AdminPanel