import { configureStore } from "@reduxjs/toolkit";
import userReducer from "./UserSlice.js";
import urlReducer from "./UrlSlice.js";

export const store = configureStore({
  reducer: {
    user: userReducer,
    url: urlReducer,
  },
});
