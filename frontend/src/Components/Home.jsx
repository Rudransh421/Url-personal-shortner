import React, { useEffect } from "react";
import History from "./History.jsx";

import axios from "axios";
import { useDispatch } from "react-redux";
import { addUrl, setUrls } from "../redux/UrlSlice.js";
import { BACKEND_URL } from "../utils/constant.js";

function Home() {
  const [longUrl, setLongUrl] = React.useState("");
  const [slug, setSlug] = React.useState("");
  const [error, setError] = React.useState("");
  const [success, setSuccess] = React.useState("");
  const [loading, setLoading] = React.useState(false);

  const dispatch = useDispatch();

  const isDisable = !longUrl || !slug || loading;

  const shortLinkGenerate = async () => {
    setError("");
    setSuccess("");

    if (!longUrl || !slug) {
      setError("Both fields are required");
      return;
    }

    try {
      new URL(longUrl);
    } catch (err) {
      setError("Invalid URL");
      console.error("URL validation error:", err);
      return;
    }

    try {
      setLoading(true);

      const response = await axios.post(
        `${BACKEND_URL}/urls`,
        {
          slug,
          originalUrl: longUrl,
        },
        { withCredentials: true },
      );

      const newUrl = response.data.urlDto;

      dispatch(addUrl(newUrl));

      setSuccess(`Short URL created: ${newUrl.shortUrl}`);

      setLongUrl("");
      setSlug("");
    } catch (err) {
      if (err.response?.status === 409) {
        setError("Slug already exists");
      } else {
        setError("Failed to create short URL");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const fetchUrls = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/urls/my`, {
          withCredentials: true,
        });

        const extractedUrls = response.data.urls.map((item) => item.urlDto);

        dispatch(setUrls(extractedUrls));
        console.log("URLs set in Redux:", extractedUrls);
      } catch (error) {
        console.error("Failed to fetch URLs", error);
      }
    };

    fetchUrls();
  }, [dispatch]);

  return (
    <div className="flex min-h-screen flex-col bg-gray-50 py-12 sm:px-6 lg:px-8">
      {/* Page Header */}
      <div className="sm:mx-auto sm:w-full sm:max-w-6xl px-4">
        <h2 className="mt-6 mb-10 text-center text-3xl font-bold tracking-tight text-gray-900">
          Create a new short link
        </h2>
      </div>

      {/* Main Content Layout */}
      <div className="mx-auto flex w-full max-w-6xl flex-col items-start justify-center gap-8 px-4 lg:flex-row">
        {/* Create Form Section */}
        <div className="w-full max-w-[480px] mx-auto lg:mx-0 bg-white px-6 py-12 shadow sm:rounded-lg sm:px-12">
          <div className="space-y-6">
            {/* Long URL */}
            <div>
              <label className="block text-sm font-medium text-gray-900">
                Long URL
              </label>
              <input
                type="url"
                value={longUrl}
                onChange={(e) => setLongUrl(e.target.value)}
                placeholder="https://example.com/very-long-url..."
                className="mt-2 block w-full rounded-md border px-2 py-1.5 shadow-sm focus:ring-indigo-600 focus:border-indigo-600"
                required
              />
            </div>

            {/* Slug */}
            <div>
              <label className="block text-sm font-medium text-gray-900">
                Custom Slug
              </label>
              <input
                type="text"
                value={slug}
                onChange={(e) => setSlug(e.target.value)}
                placeholder="my-link"
                className="mt-2 block w-full rounded-md border px-2 py-1.5 shadow-sm focus:ring-indigo-600 focus:border-indigo-600"
                required
              />
            </div>

            {/* Button */}
            <button
              onClick={shortLinkGenerate}
              disabled={isDisable}
              onKeyDown={(e) => {
                if (e.key == "Enter" && !isDisable && !loading) {
                  shortLinkGenerate();
                }
              }}
              className="w-full rounded-md py-1.5 text-white font-semibold bg-indigo-600 hover:bg-indigo-500 disabled:bg-gray-400 disabled:hover:bg-gray-400 disabled:cursor-not-allowed transition-colors"
            >
              {loading ? "Creating..." : "Shorten URL"}
            </button>
          </div>

          {/* Error */}
          {error && (
            <div className="mt-6 rounded-md bg-red-50 p-4 border border-red-200">
              <p className="text-sm text-red-700 text-center">{error}</p>
            </div>
          )}

          {/* Success */}
          {success && (
            <div className="mt-6 rounded-md bg-green-50 p-4 border border-green-200">
              <p className="text-sm text-green-700 text-center">{success}</p>
            </div>
          )}
        </div>

        {/* History Section */}
        <div className="w-full max-w-[480px] mx-auto lg:mx-0 lg:max-w-none lg:flex-1">
          <History />
        </div>
      </div>
    </div>
  );
}

export default Home;
