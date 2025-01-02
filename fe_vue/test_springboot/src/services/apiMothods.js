import axios from 'axios';

const baseUrl = import.meta.env.VITE_BASE_API_URL; 
const axiosInstanceBasic = axios.create({
  baseURL: baseUrl,
  headers: {
    'Content-Type': 'application/json',
  },
});

const axiosInstanceFormData = axios.create({
  baseURL: baseUrl,
  headers: {
    'Content-Type': 'multipart/form-data', 
  },
});

const addAuthorizationHeader = (instance) => {
  instance.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('token'); 
      if (token) {
        config.headers.Authorization = `${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );
};

// Tambahkan interceptor ke instance
addAuthorizationHeader(axiosInstanceBasic);
addAuthorizationHeader(axiosInstanceFormData);

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
      localStorage.clear();
      throw new Error(error.response.data);
    }else{
      throw new Error(error?.response?.data?.msg || 'API Error');
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