import "./App.css";
import { Routes, Route, useNavigate } from "react-router-dom";

import AuthPage from "./Components/AuthPage.jsx";

import { useDispatch } from "react-redux";
import { useEffect } from "react";
import axios from "axios";
import { setLoading, setUser } from "./redux/UserSlice.js";
import { BACKEND_URL } from "./utils/constant.js";
import Home from "./Components/Home.jsx";
import NotFound from "./Components/NotFound.jsx";
import ProtectedRoute from "./Components/ProtectedRoute.jsx";


function App() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  useEffect(() => {
    const checkAuth = async () => {
      try {
        const res = await axios.get(`${BACKEND_URL}/auth/get-user`, {
          withCredentials: true,
        });

        // this specific route provide whole user object so nothing to worry

        console.log("in app.jsx user dispatched:", res.data.user);
        dispatch(setUser(res.data.user));
        navigate("/");
      } catch (err) {
        console.error("Auth check failed:", err.message);
        try {
          console.log("Attempting to refresh token...");
          const res = await axios.post(
            `${BACKEND_URL}/auth/refresh`,
            {},
            {
              withCredentials: true,
            },
          );
          console.log(
            "in app.jsx user dispatched after refresh:",
            res.data.user,
          );
          dispatch(setUser(res.data.user));
        } catch (error) {
          console.error("Auth check failed:", error.message);
          dispatch(setUser(null));
        }
      } finally {
        dispatch(setLoading(false));
      }
    };
    checkAuth();
  }, []);
  
  return (
    <Routes>
      <Route path="/auth" element={<AuthPage />} />
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <Home />
          </ProtectedRoute>
        }
      />
      <Route path="/not-found" element={<NotFound />} />
    </Routes>
  );
}

export default App;
