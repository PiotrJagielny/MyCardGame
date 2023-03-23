import React from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import EntryPage from './Game_Unrelated_Components/EntryPageComponents/EntryPage';
import MainPage from './Game_Related_Components/MainPageComponents/MainPage';

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/Main" element={<MainPage />} />
          <Route path="/" element={<EntryPage />} />
        </Routes>
      </Router>
    </div>
  );
};

export default App;
