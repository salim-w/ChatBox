import React from 'react';

const FileMessage = ({ message, timestamp, sender }) => {
  return (
    <div className="flex flex-col">
      {/* Sender info */}
      <div className="flex items-center gap-2 mb-1">
        <div className="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center text-white">
          {sender?.charAt(0)?.toUpperCase()}
        </div>
        <span className="text-gray-500 text-sm">Profile</span>
      </div>

      {/* Message container */}
      <div className="flex items-center space-x-2 bg-gray-50 rounded-lg p-3 max-w-sm">
        <div className="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center text-white">
          {sender?.charAt(0)?.toUpperCase()}
        </div>

        <div className="flex flex-col flex-grow">
          <span className="text-sm text-gray-900">{sender}</span>
          <a href="#" className="text-blue-500 hover:underline text-sm">
            Télécharger le fichier
          </a>
          <span className="text-xs text-gray-500 mt-1">
            {timestamp}
          </span>
        </div>
      </div>
    </div>
  );
};

export default FileMessage;