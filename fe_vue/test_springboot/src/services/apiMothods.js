import axios from 'axios';

const baseUrl = import.meta.env.VITE_BASE_API_URL; // Base URL dari environment variable
const BearerToken = localStorage.getItem('token'); // Token dari localStorage

const axiosInstanceBasic = axios.create({
  baseURL: baseUrl,
  headers: {
    Authorization: `${BearerToken}`,
    'Content-Type': 'application/json',
  },
});

const axiosInstanceFormData = axios.create({
  baseURL: baseUrl,
  headers: {
    Authorization: `${BearerToken}`,
    'Content-Type': 'multipart/form-data', 
  },
});

const getData = async (endpoint, params) => {
  try {
    const { data } = await axiosInstanceBasic.get(endpoint, { params });
    return data
  } catch (error) {
    handleError(error);
  }
};

const getDataById = async (endpoint, params) => {
  try {
    const { data } = await axiosInstanceBasic.get(endpoint, { params });
    return data
  } catch (error) {
    handleError(error);
  }
};

const postData = async (endpoint, payload) => {
  try {
    const { data } = await axiosInstanceBasic.post(endpoint, payload);
    return data
  } catch (error) {
    handleError(error);
  }
};

const postDataFormData = async (endpoint, payload) => {
  try {
    const { data } = await axiosInstanceFormData.post(endpoint, payload);
    return data
  } catch (error) {
    handleError(error);
  }
};

const putData = async (endpoint, payload) => {
  try {
    const { data } = await axiosInstanceBasic.put(endpoint, payload);
    return data;
  } catch (error) {
    handleError(error);
  }
};

const putDataFormData = async (endpoint, payload) => {
  try {
    const { data } = await axiosInstanceFormData.put(endpoint, payload);
    return data;
  } catch (error) {
    handleError(error);
  }
};

const deleteData = async (endpoint) => {
  try {
    const { data } = await axiosInstanceBasic.delete(endpoint);
    return data;
  } catch (error) {
    handleError(error);
  }
};

const handleError = (error) => {
  if (error.response) {
    console.error('API Error:', error.response.status, error.response.data);
    if(error.response.data === "Invalid or expired token"){
      throw new Error(error.response.data);
    }else{
      throw new Error(error.response.data.message || 'API Error');
    }
  } else if (error.request) {
    console.error('No response received:', error.request);
    throw new Error('No response from server');
  } else {
    console.error('Error setting up request:', error.message);
    throw new Error('Request setup error');
  }
};

export default {
  getData,
  getDataById,
  postData,
  putData,
  deleteData,
  postDataFormData,
  putDataFormData,
};