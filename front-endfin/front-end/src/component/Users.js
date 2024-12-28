import { useSelector } from "react-redux";

const Users = () => {
    const messages = useSelector(state => state.messages || []);

    return (
        <div className="col-4 card">
            <br />
            {messages.map((message, index) => {
                if (message.status === "JOIN" && message.content == null) {
                    return (
                        <div key={index} className="d-flex align-items-start">
                            <div className="avatar">{message.user?.charAt(0) || '?'}</div>
                            <div className="flex-grow-1 ml-3">
                                {message.user}
                                <div className="small">
                                    <div className="badge bg-success float-right">{message.status}</div>
                                </div>
                            </div>
                        </div>
                    );
                } else if (message.status === "LEFT") {
                    return (
                        <div key={index} className="d-flex align-items-start">
                            <div className="avatar">{message.user?.charAt(0) || '?'}</div>
                            <div className="flex-grow-1 ml-3">
                                {message.user}
                                <div className="small">
                                    <div className="badge bg-danger float-right">{message.status}</div>
                                </div>
                            </div>
                        </div>
                    );
                }
                return null;
            })}
        </div>
    );
};

export default Users;
