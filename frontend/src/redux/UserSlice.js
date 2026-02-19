import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: null,
  loading:true,

};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.user = action.payload;
      console.log("set user worked fine and have user :", state.user);
    },
    clearUser: (state) => {
      state.user = null;
      state.cartCount = 0;
    },
    setLoading: (state, action) => {
      state.loading = action.payload;
    },
  },
});

export const { setUser, clearUser, setLoading } = userSlice.actions;
export default userSlice.reducer;
