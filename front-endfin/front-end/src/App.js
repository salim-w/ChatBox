import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import JoinChat from './component/JoinChat';
import ChatConsole from './component/ChatConsole';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<JoinChat />} />
        <Route path="/chat-console/:username" element={<ChatConsole />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </Router>
  );
};

export default App;

