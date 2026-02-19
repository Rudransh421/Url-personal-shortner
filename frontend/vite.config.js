import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },


      "^/[a-zA-Z0-9-]+$": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => `/r${path}`,
      },
    },
  },
});
