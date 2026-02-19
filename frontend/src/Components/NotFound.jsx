function NotFound() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-white px-6">
      <div className="text-center">
        {/* Broken Link Icon */}
        <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-slate-100 mb-6 ring-8 ring-slate-50">
          <svg
            className="w-8 h-8 text-slate-500"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"
            />
          </svg>
        </div>

        <h1 className="text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">
          Broken Slug
        </h1>

        <p className="mt-4 text-base text-slate-600 max-w-sm mx-auto">
          There are no links mapped to the provided slug.
          <span className="block mt-1 font-medium text-slate-800">
            Please cross-check your input.
          </span>
        </p>

        <div className="mt-8 flex items-center justify-center gap-x-4">
          <a
            href="/"
            className="rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
          >
            Return Home
          </a>
          <a
            href="#"
            className="text-sm font-semibold text-slate-900 hover:underline"
          >
            Contact Support <span aria-hidden="true">&rarr;</span>
          </a>
        </div>
      </div>
    </div>
  );
}

export default NotFound;
