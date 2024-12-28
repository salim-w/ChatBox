import React, { useState, useEffect } from 'react';
import { uploadFile, getAllFiles, downloadFile } from '../component/fileService';

const FileUpload = () => {
  const [file, setFile] = useState(null);
  const [files, setFiles] = useState([]);

  useEffect(() => {
    fetchFiles();
  }, []);

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      alert('Veuillez sélectionner un fichier');
      return;
    }

    try {
      const response = await uploadFile(file);
      alert(response.data);
      fetchFiles();  // Récupérer la liste des fichiers après l'upload
    } catch (error) {
      alert('Erreur lors de l\'envoi du fichier');
    }
  };

  const fetchFiles = async () => {
    try {
      const response = await getAllFiles();
      // Adapter la réponse selon la structure du backend
      const filesList = response.data.map(fileInfo => ({
        name: fileInfo.fileName,
        downloadUri: fileInfo.fileDownloadUri
      }));
      setFiles(filesList);
    } catch (error) {
      console.error('Erreur lors de la récupération des fichiers');
    }
  };

  const handleDownload = async (filename) => {
    try {
      const response = await downloadFile(filename);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
    } catch (error) {
      console.error('Erreur lors du téléchargement du fichier');
    }
  };

  return (
    <div>
      <h2>Envoyer un Fichier</h2>
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload}>Envoyer</button>

      <h2>Liste des Fichiers</h2>
      <ul>
        {files.map((fileInfo, index) => (
          <li key={index}>
            {fileInfo.name} -{' '}
            <button onClick={() => handleDownload(fileInfo.name)}>
              Télécharger
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default FileUpload;
