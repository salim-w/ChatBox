import React from "react";

const Profile = ({ username }) => {
  return (
    <div className="border-bottom">
      <div className="d-flex align-items-center p-3">
        <div className="position-relative">
          <div className="avatar bg-primary text-white rounded-circle d-flex align-items-center justify-content-center"
               style={{ width: "40px", height: "40px", fontSize: "1.2rem" }}>
            {username?.charAt(0).toUpperCase()}
          </div>
        </div>
        <div className="flex-grow-1 ms-3">
          <strong>{username}</strong>
          <div className="text-muted small">
            <em>Online</em>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;

