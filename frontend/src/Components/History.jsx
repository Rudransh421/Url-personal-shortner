import { useSelector } from "react-redux";
import { BACKEND_URL } from "../utils/constant.js";

function History() {
  const urls = useSelector((state) => state.url?.urls);

  if (!urls || urls.length === 0) {
    return (
      <p className="text-sm text-gray-500 text-center mt-4">No links yet</p>
    );
  }

  return (
    <div className="bg-white w-full h-full px-6 py-8 shadow sm:rounded-lg sm:px-8">
      <div className="border-b border-gray-200 pb-4 mb-6">
        <h2 className="text-xl font-bold text-gray-900">Link History</h2>
      </div>

      <ul className="space-y-3">
        {urls.map((url) => (
          <li
            key={url.shortId}
            className="flex flex-col gap-1 p-4 rounded-lg border border-gray-100 bg-gray-50 hover:bg-gray-100 transition-colors"
          >
            <div className="flex items-center justify-between">
              {/* Short Link */}
              <a
                href={`http://localhost:8080${url.shortUrl}`}
                target="_blank"
                rel="noopener noreferrer"
                title="Open short link"
                className="text-sm font-semibold text-indigo-600 hover:text-indigo-500 hover:underline"
              >
                {url.slug ? `${url.slug}-${url.shortId}` : url.shortId}
              </a>

              {/* Optional Badge for the Slug/ID */}
              <span className="inline-flex items-center rounded-md bg-indigo-50 px-2 py-1 text-xs font-medium text-indigo-700 ring-1 ring-inset ring-indigo-700/10">
                {`http://localhost:8080${url.shortUrl}`}
              </span>
            </div>

            {/* Original Long Link */}
            <div
              className="text-xs text-gray-500 truncate max-w-[250px] sm:max-w-[350px]"
              title={url.originalUrl}
            >
              {url.originalUrl}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default History;
