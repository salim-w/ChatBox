import axios from 'axios';

const API_URL = '/api/files';

// Upload file
export const uploadFile = async (file) => {
  const formData = new FormData();
  formData.append('file', file);

  try {
    const response = await axios.post(`${API_URL}/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de l\'envoi du fichier :', error.message);
    throw error;
  }
};

// Get all files
export const getAllFiles = async () => {
  try {
    const response = await axios.get(`${API_URL}`);
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des fichiers :', error.message);
    throw error;
  }
};

// Download file
export const downloadFile = async (filename) => {
  try {
    const response = await axios.get(`${API_URL}/download/${filename}`, {
      responseType: 'blob', // Blob pour les téléchargements
    });
    return response;
  } catch (error) {
    console.error('Erreur lors du téléchargement du fichier :', error.message);
    throw error;
  }
};
