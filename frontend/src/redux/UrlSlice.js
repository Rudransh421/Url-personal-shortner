import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  urls: [],
  urlLoading: false,
};

const urlSlice = createSlice({
  name: "url",
  initialState,
  reducers: {
    setUrls: (state, action) => {
      state.urls = action.payload;
    },

    addUrl: (state, action) => {
      state.urls.unshift(action.payload);
    },

    // 🔹 Loading state
    setUrlLoading: (state, action) => {
      state.urlLoading = action.payload;
    },
  },
});

export const { setUrls, addUrl, setUrlLoading } = urlSlice.actions;

export default urlSlice.reducer;
