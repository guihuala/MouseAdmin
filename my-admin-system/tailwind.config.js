import daisyui from 'daisyui';

/** @type {import('tailwindcss').Config} */
export default {
  daisyui: {
    themes: ["pastel","aqua"],
  },
  content: ['./src/**/*.{vue,js,ts}'],
  theme: {
    extend: {},
  },
  plugins: [require('daisyui'),],
}

