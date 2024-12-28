var username = null;
var stompClient = null;

// Gérer la connexion
function connect(event) {
    username = document.querySelector('#name').value.trim();

    if (username) {
        const socket = new SockJS('/ws'); // Connecte au backend
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {
    document.querySelector('#username-page').classList.add('hidden');
    document.querySelector('#chat-page').classList.remove('hidden');

    // S'abonner à la conversation publique
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Informer les autres utilisateurs qu'un nouveau utilisateur est arrivé
    stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
}

function onError(error) {
    document.querySelector('.connecting').textContent = 'Error in connection!';
}

// Envoi d'un message
function sendMessage(event) {
    const messageContent = document.querySelector('#message').value.trim();

    if (messageContent && stompClient) {
        const chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        document.querySelector('#message').value = '';
    }
    event.preventDefault();
}

// Réception d'un message
function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    const messageArea = document.querySelector('#messageArea');

    const messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        messageElement.textContent = message.content;
    } else {
        messageElement.textContent = `${message.sender}: ${message.content}`;
    }

    messageArea.appendChild(messageElement);
}

document.querySelector('#usernameForm').addEventListener('submit', connect, true);
document.querySelector('#messageForm').addEventListener('submit', sendMessage, true);
