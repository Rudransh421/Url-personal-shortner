import React, { useState } from "react";
import axios from "axios";
import { useDispatch } from "react-redux";
import { setUser } from "../redux/UserSlice.js";
import { BACKEND_URL } from "../utils/constant.js";

function Registration({ onSwitchToLogin, onRegisterSuccess }) {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
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

  const dispatch = useDispatch();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const payload = {
        name: `${formData.firstName} ${formData.lastName}`.trim(),
        email: formData.email,
        phoneNo: formData.phone,
        password: formData.password,
      };

      const res = await axios.post(`${BACKEND_URL}/auth/register`, payload, {
        withCredentials: true,
      });

      const user = res.data.user;

      dispatch(setUser(user));

      console.log("Registration successful:", user);

      onRegisterSuccess?.();
      onSwitchToLogin();
    } catch (err) {
      console.error(err);
      setError(
        err.response?.data?.message || "Registration failed. Please try again.",
      );
    } finally {
      setLoading(false);
    }
  };
  // Updated for Light Theme: White background, gray text, lighter borders
  const inputClasses =
    "w-full px-3 py-2.5 rounded-md bg-white border border-gray-300 text-gray-900 placeholder-gray-500 text-sm outline-none transition-all duration-200 focus:border-indigo-600 focus:ring-4 focus:ring-indigo-600/10 resize-none";

  return (
    // Container: Light background (gray-50)
    <div className="min-h-screen w-full flex items-center justify-center bg-gray-50 p-4">
      {/* Card: White background, shadow, light border */}
      <div className="w-full max-w-lg bg-white rounded-xl shadow-xl p-8 border border-gray-200">
        {/* Logo Section */}
        <div className="flex flex-col items-center mb-8">
          {/* Logo Circle: Light Indigo background */}
          <div className="h-16 w-16 bg-indigo-50 text-indigo-600 rounded-full flex items-center justify-center mb-2 shadow-sm border border-indigo-100">
            <span className="font-bold text-sm">Photo</span>
          </div>
          <h2 className="text-2xl font-bold text-gray-900">Create Account</h2>
          <p className="text-gray-500 text-sm">Sign up to get started</p>
        </div>

        {/* Registration Form */}
        <form className="flex flex-col w-full gap-4" onSubmit={handleSubmit}>
          {error && (
            <div className="bg-red-50 text-red-600 text-sm p-3 rounded-md border border-red-200">
              {error}
            </div>
          )}

          {/* Row: Name (Responsive: Stacks on mobile, row on sm screens) */}
          <div className="flex flex-col sm:flex-row gap-3 w-full">
            <div className="flex-1 flex flex-col">
              <input
                type="text"
                name="firstName"
                placeholder="First Name"
                value={formData.firstName}
                onChange={handleChange}
                className={inputClasses}
                required
              />
            </div>
            <div className="flex-1 flex flex-col">
              <input
                type="text"
                name="lastName"
                placeholder="Last Name"
                value={formData.lastName}
                onChange={handleChange}
                className={inputClasses}
                required
              />
            </div>
          </div>

          {/* Row: Contact */}
          <div className="flex flex-col sm:flex-row gap-3 w-full">
            <div className="flex-1 flex flex-col">
              <input
                type="email"
                name="email"
                placeholder="Email Address"
                value={formData.email}
                onChange={handleChange}
                className={inputClasses}
                required
              />
            </div>
            <div className="flex-1 flex flex-col">
              <input
                type="tel"
                name="phone"
                placeholder="Phone Number"
                value={formData.phone}
                onChange={handleChange}
                className={inputClasses}
                required
              />
            </div>
          </div>

          {/* Password */}
          <div className="flex flex-col flex-1">
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
            {loading ? "Registering..." : "Register"}
          </button>
        </form>

        {/* Switch to Login */}
        <div className="mt-6 text-center text-sm text-gray-500">
          <p>
            Already have an account?{" "}
            <button
              type="button"
              onClick={onSwitchToLogin}
              className="text-indigo-600 font-medium hover:text-indigo-700 hover:underline focus:outline-none transition-colors"
            >
              Login here
            </button>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Registration;
