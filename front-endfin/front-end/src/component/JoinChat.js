import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMessageContext } from "../proxy/StompProvider";

const JoinChat = () => {
  const [name, setName] = useState('');
  const navigate = useNavigate();
  const { publishMessage } = useMessageContext();

  const handleSubmit = (event) => {
    event.preventDefault();
    if (name.trim()) {
      publishMessage('/app/sendStatus', { user: name.trim(), status: 'JOIN' });
      navigate(`/chat-console/${name.trim()}`);
    }
  };

  return (
    <div className="container">
      <div className="row justify-content-center mt-5">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h4 className="text-center">Join Chat</h4>
            </div>
            <div className="card-body">
              <form onSubmit={handleSubmit}>
                <div className="input-group mb-3">
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Type your username"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                  />
                  <button type="submit" className="btn btn-primary">
                    Join
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default JoinChat;

