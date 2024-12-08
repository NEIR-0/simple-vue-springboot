import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import NodePolyfillPlugin from 'node-polyfill-webpack-plugin'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  define: {
    'global': 'window', // Ini memastikan `global` didefinisikan sebagai `window`
  },
  build: {
    rollupOptions: {
      plugins: [
        new NodePolyfillPlugin(),
      ],
    },
  },
})