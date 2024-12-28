import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useSocket } from "../hooks/useSocket";
import axios from "axios";
import Profile from "./Profile";

const ChatConsole = () => {
  const { username } = useParams();
  const navigate = useNavigate();
  const { messages, sendMessage } = useSocket(username);
  const [content, setContent] = useState("");
  const [file, setFile] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!username) {
      navigate('/');
      return;
    }

    const fetchMessages = async () => {
      try {
        const response = await axios.get(`/api/messages/user/${username}`);
        // Assuming the API returns an array of messages
        setMessages((prevMessages) => [...response.data, ...prevMessages]);
      } catch (error) {
        console.error("Error fetching messages:", error);
        setError("Failed to fetch messages");
      }
    };

    fetchMessages();
  }, [username, navigate]);

  const handleFileUpload = async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await axios.post('/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      return response.data;
    } catch (error) {
      throw new Error("File upload failed");
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);

    try {
      let fileUrl = null;
      let fileType = null;

      if (file) {
        const uploadResponse = await handleFileUpload(file);
        fileUrl = uploadResponse.fileUrl;
        fileType = uploadResponse.fileType;
      }

      const chatMessage = {
        type: file ? "FILE" : "CHAT",
        sender: username,
        content: file ? file.name : content.trim(),
        timestamp: new Date().toISOString(),
        fileContent: fileUrl,
        fileType: fileType
      };

      if (content.trim() || file) {
        sendMessage(chatMessage);
        setContent("");
        setFile(null);
      }
    } catch (error) {
      setError("Failed to send message: " + error.message);
    }
  };

  const renderMessage = (message) => (
    <div className="chat-message-wrapper pb-2" key={message.id || message.timestamp}>
      <div className="message-content">
        <div className="avatar bg-primary text-white rounded-circle d-flex align-items-center justify-content-center"
             style={{ width: "32px", height: "32px", fontSize: "1rem" }}>
          {message.sender?.charAt(0).toUpperCase()}
        </div>
        <div className="message-bubble bg-light rounded py-2 px-3 mb-2">
          <div className="message-sender fw-bold">{message.sender}</div>
          {message.type === "FILE" ? (
            <div className="file-content">
              {message.fileType === "image" ? (
                <img
                  src={message.fileContent}
                  alt={message.content}
                  className="img-fluid"
                  style={{ maxWidth: "200px" }}
                />
              ) : (
                <a
                  href={message.fileContent}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="btn btn-sm btn-primary"
                >
                  Download {message.content}
                </a>
              )}
            </div>
          ) : (
            <div className="message-text">{message.content}</div>
          )}
          <div className="message-time text-muted small">
            {new Date(message.timestamp).toLocaleTimeString()}
          </div>
        </div>
      </div>
    </div>
  );

  return (
    <div className="container-fluid h-100">
      <div className="row h-100">
        <div className="col-md-8 mx-auto h-100">
          <div className="card h-100">
            <Profile username={username} />
            <div className="chat-messages p-4" style={{ height: "calc(100vh - 200px)", overflowY: "auto" }}>
              {messages.map(renderMessage)}
            </div>
            {error && <div className="alert alert-danger mx-4">{error}</div>}
            <form onSubmit={handleSubmit} className="chat-input p-3 border-top">
              <div className="input-group">
                <input
                  type="text"
                  className="form-control"
                  placeholder="Type your message"
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                />
                <input
                  type="file"
                  className="form-control"
                  onChange={(e) => setFile(e.target.files[0])}
                  accept=".jpg,.jpeg,.png,.gif,.pdf"
                />
                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={!content.trim() && !file}
                >
                  Send
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChatConsole;

