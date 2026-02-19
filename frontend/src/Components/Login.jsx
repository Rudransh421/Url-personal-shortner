import React, {  useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useDispatch } from "react-redux";
import { setUser } from "../redux/UserSlice.js";
import { BACKEND_URL } from "../utils/constant.js";
import parsePhoneNumberFromString from "libphonenumber-js";

function Login({ onSwitchToRegister }) {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError("");
  };

  const checkPhoneNumber = (input) => {
    const phoneNumber = parsePhoneNumberFromString(input, "IN");
    return phoneNumber ? phoneNumber.isValid() : false;
  };

  const isEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.username);

  const dispatch = useDispatch();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    if (formData.username.trim() === "" || formData.password.trim() === "") {
      setError("Please fill in all fields.");
      setLoading(false);
      return;
    }

    if (!isEmail && !checkPhoneNumber(formData.username)) {
      setError("Please enter a valid email address or a phone number.");
      setLoading(false);
      return;
    }
    try {
      const payload = {
        username: formData.username,
        password: formData.password,
      };

      const res = await axios.post(`${BACKEND_URL}/auth/login`, payload, {
        withCredentials: true,
      });

      const user = res.data.user;

      dispatch(setUser(user));

      console.log("Login successful:", user);

      navigate("/");
    } catch (err) {
      console.error(err);
      setError(
        err.response?.data?.message || "Login failed. Please try again.",
      );
    } finally {
      setLoading(false);
    }
  };

  // LIGHT THEME INPUT STYLES
  // Matches Registration.jsx exactly: White bg, dark text, light border
  const inputClasses =
    "w-full px-3 py-2.5 rounded-md bg-white border border-gray-300 text-gray-900 placeholder-gray-500 text-sm outline-none transition-all duration-200 focus:border-indigo-600 focus:ring-4 focus:ring-indigo-600/10";

  return (
    // Container: Light background (gray-50)
    <div className="min-h-screen w-full flex items-center justify-center bg-gray-50 p-4">
      {/* Card: White background, max-w-md for compact login view */}
      <div className="w-full max-w-md bg-white rounded-xl shadow-xl p-8 border border-gray-200">
        {/* Logo Section */}
        <div className="flex flex-col items-center mb-8">
          <div className="h-16 w-16 bg-indigo-50 text-indigo-600 rounded-full flex items-center justify-center mb-2 shadow-sm border border-indigo-100">
            <span className="font-bold text-sm">Photo</span>
          </div>
          <h2 className="text-2xl font-bold text-gray-900">Welcome Back</h2>
          <p className="text-gray-500 text-sm">Login to your account</p>
        </div>

        {/* Login Form */}
        <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
          {error && (
            <div className="bg-red-50 text-red-600 text-sm p-3 rounded-md border border-red-200">
              {error}
            </div>
          )}

          <div className="flex flex-col">
            <input
              type="text"
              name="username"
              placeholder="Email or Phone Number"
              value={formData.username}
              onChange={handleChange}
              className={inputClasses}
              required
            />
          </div>

          <div className="flex flex-col">
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              className={inputClasses}
              required
            />
          </div>

          {/* Submit Button */}
          <button
            type="submit"
            disabled={loading}
            className={`w-full py-3 px-4 rounded-md text-white font-medium transition-all duration-200 mt-2 ${
              loading
                ? "bg-indigo-100 cursor-not-allowed text-indigo-300"
                : "bg-indigo-600 hover:bg-indigo-700 shadow-lg shadow-indigo-600/20"
            }`}
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

        {/* Switch to Register */}
        <div className="mt-6 text-center text-sm text-gray-500">
          <p>
            Don't have an account?{" "}
            <button
              type="button"
              onClick={onSwitchToRegister}
              className="text-indigo-600 font-medium hover:text-indigo-700 hover:underline focus:outline-none transition-colors"
            >
              Register here
            </button>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;
